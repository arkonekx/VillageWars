package villagewars;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.structure.Structures;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;


public class VillageWars implements ModInitializer {
	public static Map<UUID,State> states = new HashMap<>();
	public static Map<VillageKey,UUID> owners = new HashMap<>();
	public static Map<VillageKey, Village> villages = new HashMap<>();
	public static Map<TerritoryKey,TerritoryAssignment> territories = new HashMap<>();
	public static Set<TerritoryKey> checkedCandidates = new HashSet<>();
	public static Set<TerritoryKey> candidates = new LinkedHashSet<>();
	public static Map<TerritoryKey,Set<TerritoryKey>> pendingTerritories = new HashMap<>();
	public static Map<TerritoryKey, Long> retryAfterTick = new HashMap<>();
	public static boolean checkingCandidate = false;
	public static final String MOD_ID = "villagewars";
//	public static final AttachmentType<String> OWNER_STATE = AttachmentRegistry.create(
//			Identifier.of(MOD_ID, "owner_state"),
//			builder -> builder.persistent(Codec.STRING)
//	);
	public static final AttachmentType<VillageKey> Village_ATTACHMENT = AttachmentRegistry.create(
			Identifier.of(MOD_ID, "village"),
			builder -> builder.persistent(VillageKey.CODEC)
	);
	public static MinecraftServer SERVER;
	public static long currentTick = 0;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static VillageWarsData getData(MinecraftServer server) {
		return server.getOverworld()
				.getPersistentStateManager()
				.getOrCreate(VillageWarsData.TYPE);
	}



	public static void registerVillage(StructureStart start,ServerWorld world ){

		BlockPos blockPosVillage = start.getBoundingBox().getCenter();
		ChunkPos startChunk = start.getPos();
		VillageWarsData data = getData(world.getServer());
		VillageKey villageKey = new VillageKey(world.getRegistryKey(), startChunk);

		Village village = villages.get(villageKey);

		if (village == null) {
			village = new Village(blockPosVillage, world);
			data.putVillage(villageKey, village);
			villages.put(villageKey, village);

			for(var entity : world.iterateEntities()){
				if(entity instanceof VillagerEntity villager){
					registerVillager(villager,world);
				}
			}
		}

		UUID stateId = owners.get(villageKey);
		State state = null;

		if (stateId != null) {
			state = states.get(stateId);

			if (state == null) {
				throw new IllegalStateException(
						"Brak państwa " + stateId + " dla wioski " + villageKey
				);

			}
		} else {
			for (State existingState : states.values()) {
				if (existingState.getVillages().contains(village)) {
					state = existingState;
					break;
				}
			}

			if (state == null) {
				state = new AIState(village.getName() + " State");
			}

			stateId = state.getStateId();
		}

		if (!state.getVillages().contains(village)) {
			state.addVillage(village);
		}

		data.putState(stateId, state);
		states.put(stateId, state);

		if (!owners.containsKey(villageKey)) {
			data.putOwner(villageKey, stateId);
			owners.put(villageKey, stateId);
		}




	}


	public static void scheduleTerritoryCheck(TerritoryKey key,ServerWorld world){
		ChunkPos chunkPos = key.position();
		double MAX_DISTANCE = 800D;

		var structureSets = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE_SET);
		var structureSet = structureSets.getValueOrThrow(StructureSetKeys.VILLAGES);
		if (structureSet.placement() instanceof RandomSpreadStructurePlacement placement) {

			List<RegionPos> regiony = new ArrayList<>();
			List<TerritoryKey> candidates = new ArrayList<>();

			boolean finish = false;
			int spacing = placement.getSpacing();
			int startX = Math.floorDiv(chunkPos.x - 58, spacing) * spacing;
			int startZ = Math.floorDiv(chunkPos.z + 58, spacing) * spacing;
			int minZ = Math.floorDiv(chunkPos.z - 58, spacing) * spacing;
			int x = startX;
			int z = startZ;
			while(!finish){
				regiony.add(new RegionPos(Math.floorDiv(x, placement.getSpacing()),Math.floorDiv(z, placement.getSpacing())));
				x+=spacing;
				if(x-chunkPos.x >58){
					x=startX;
					z-=spacing;
				}
				if(z < minZ){
					finish = true;
				}
			}
			ChunkPos startChunk;
			double chunkX = chunkPos.x;
			double chunkZ = chunkPos.z;

			double distance;
			for(RegionPos regions : regiony){
				startChunk = placement.getStartChunk(world.getSeed(),regions.x()*spacing,regions.z()*spacing);

				double dx = chunkX - startChunk.x;
				double dz = chunkZ - startChunk.z;
				distance = dx* dx + dz * dz;
				if(distance>58*58){
					continue;
				}
				candidates.add(new TerritoryKey(world.getRegistryKey(),startChunk));

			}

			candidates.removeIf(c ->checkedCandidates.contains(c));

			TerritoryStatus status;
			if(!candidates.isEmpty()){
				status = TerritoryStatus.PROVISIONAL;
				VillageWars.candidates.addAll(candidates);
				Set<TerritoryKey> pending = new LinkedHashSet<>();
				pending.addAll(candidates);
				pendingTerritories.put(key, pending);
			}else{
				status = TerritoryStatus.CONFIRMED;
			}

			VillageKey villageKey = findVillageForChunk(key,world);


			if(villageKey != null){
				TerritoryKey newKey = new TerritoryKey(world.getRegistryKey(),chunkPos);
				TerritoryAssignment newAssignment = new TerritoryAssignment(Optional.of(villageKey),status);
				getData(SERVER).putTerritory(newKey,newAssignment);
				territories.put(newKey,newAssignment);

			}else{
				TerritoryKey newKey = new TerritoryKey(world.getRegistryKey(),chunkPos);
				TerritoryAssignment newAssignment = new TerritoryAssignment(Optional.empty(),status);
				getData(SERVER).putTerritory(newKey,newAssignment);
				territories.put(newKey,newAssignment );

			}

		}
	}

	public static VillageKey findVillageForChunk(TerritoryKey key,ServerWorld world){
		double maxDistance = 800D;
		ChunkPos chunkPos = key.position();

		Village village = findNearestVillage(new BlockPos(chunkPos.x * 16 + 8,0,chunkPos.z * 16 + 8),maxDistance,world,true);
		if(village == null){
			return null;
		}
		BlockPos villagePosition = village.getPosition();
		BlockPos chunkPosition = new BlockPos(chunkPos.x * 16 + 8, 0, chunkPos.z * 16 + 8);
		double dx = villagePosition.getX() - (double) chunkPosition.getX();
		double dz = villagePosition.getZ() - (double) chunkPosition.getZ();
		double distanceSquared = dx * dx + dz * dz;
		VillageKey villageKey = null;
		if(distanceSquared > 768D*768D){
			long seed = world.getSeed();
			seed = 31L * seed + key.dimension().getValue().toString().hashCode();
			seed = 31L * seed + key.position().x;
			seed = 31L * seed + key.position().z;

			Random rand = new Random(seed);
			if(distanceSquared > 780D*780D){

				if(rand.nextDouble()<1/ (double)3){
					villageKey = findVillageKey(village);
				}
			}else{
				if(rand.nextDouble()<2/ (double)3){
					villageKey = findVillageKey(village);
				}
			}
		}else{
			villageKey = findVillageKey(village);
		}

		return villageKey;
	}

	public static boolean confirmTerritory(TerritoryKey key,MinecraftServer server){
		ServerWorld territoryWorld = server.getWorld(key.dimension());
		TerritoryAssignment assignment = territories.get(key);
		if(territoryWorld==null || assignment == null){
			return false;
		}

		TerritoryStatus status = assignment.status();
		VillageWarsData data = getData(server);
		if(status == TerritoryStatus.PROVISIONAL){
			VillageKey villageKey = findVillageForChunk(key,territoryWorld);
			TerritoryAssignment newAssignment = new TerritoryAssignment(Optional.ofNullable(villageKey),TerritoryStatus.CONFIRMED);
			data.putTerritory(key,newAssignment);
			territories.put(key,newAssignment);

			return true;



		}
		return false;
	}

	public static void registerChunk(ChunkPos chunkPos,ServerWorld world){
		TerritoryKey key = new TerritoryKey(world.getRegistryKey(),chunkPos);
		if(territories.containsKey(key)){
			return;
		}

			scheduleTerritoryCheck(key,world);

	}

	public static void registerVillager(
			VillagerEntity villager, ServerWorld world) {

		VillageKey key = villager.getAttached(Village_ATTACHMENT);
		Village village = villages.get(key);

		if (village == null) {
			village = findNearestVillage(villager.getBlockPos(), 150D, world,false);
			if (village == null) return;

			key = findVillageKey(village);
			if (key == null) return;

			villager.setAttached(Village_ATTACHMENT, key);
		}

		village.addLoadedVillager(villager);

		if (village.addResidentId(villager.getUuid())) {
			getData(world.getServer()).putVillage(key, village);
		}
	}

	public static Village findNearestVillage(BlockPos startPosition, double maxDistance,ServerWorld world, boolean horizontalOnly){
		double max = maxDistance * maxDistance;
		Village village = null;
		for(Village v: villages.values()){
			double dx = v.getPosition().getX() - startPosition.getX();
			double dz = v.getPosition().getZ() - startPosition.getZ();

			double distance = horizontalOnly ? dx * dx + dz * dz : v.getPosition().getSquaredDistance(startPosition);
			if(!v.getWorld().getRegistryKey().equals(world.getRegistryKey())){
				continue;
			}

			if(max > distance){
				max = distance;
				village = v;
			}
		}
		return village;
	}

	@Nullable
	public static VillageKey findVillageKey(Village village) {
		for (var entry : villages.entrySet()) {
			if (entry.getValue() == village) {
				return entry.getKey();
			}
		}
		return null;
	}

	public Village RebuildVillageFromVillageEntry(VillageEntry villageEntry, World world){
		return new Village(villageEntry.pozycja(),world,villageEntry.name(),villageEntry.residentId());
	}
	public TerritoryAssignment RebuildTerritoryAssignmentFromTerritoryEntry(TerritoryEntry entry, RegistryKey<World> world){
		return new TerritoryAssignment(entry.villageKey(),entry.status());
	}

	public State RebuildStateFromStateEntry(StateEntry stateEntry, Map<VillageKey,UUID> owners, Map<VillageKey, Village> villages){

		State newState = switch (stateEntry.stateType()){
            case AI -> new AIState(stateEntry.id(),stateEntry.name(),stateEntry.emeralds(),stateEntry.warList(),stateEntry.stateType(),stateEntry.personality().orElseThrow(() -> new IllegalStateException("AiState nie ma personality")));
			case PLAYER ->
				new PlayerState(stateEntry.id(), stateEntry.name(),stateEntry.emeralds(),stateEntry.warList(),stateEntry.stateType(), stateEntry.playerId().orElseThrow(() -> new IllegalStateException("PlayerState nie ma playerId")));

		};

		List<VillageKey> villageKeys = new ArrayList<>();
		for (var entry : owners.entrySet()){
			if(entry.getValue().equals(newState.getStateId())){
				villageKeys.add(entry.getKey());
			}
		}
		for (VillageKey v: villageKeys){
			newState.addVillage(villages.get(v));
		}

		return newState;


	}


	@Override
	public void onInitialize() {
		ModEntities.registerAll();



		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			villages.clear();
			states.clear();
			owners.clear();
			territories.clear();
			SERVER = server;
			checkedCandidates.clear();
			candidates.clear();
			pendingTerritories.clear();
			checkingCandidate = false;
			currentTick = 0;
			retryAfterTick.clear();
		});


		ServerLifecycleEvents.SERVER_STARTED.register(server ->{
			VillageWarsData data = getData(server);

			checkedCandidates.addAll(data.getCheckedCandidates());
			owners = new HashMap<>(data.getOwnerEntries());


			for(VillageEntry v : data.getVillageEntries()){
				villages.put(v.village(),RebuildVillageFromVillageEntry(v,server.getWorld(v.village().dimension())));
				LOGGER.info("Wioska "+v.pozycja());
			}

			for (TerritoryEntry t: data.getTerritoryEntries() ){
				territories.put(new TerritoryKey(t.territoryKey().dimension(), t.territoryKey().position()),new TerritoryAssignment(t.villageKey(),t.status()));
			}


			for (StateEntry s : data.getStateEntries()){
				states.put(s.id(),RebuildStateFromStateEntry(s,owners,villages));
			}

			LOGGER.info("Wczytano {} państw i {} wiosek",states.size(), villages.size());

			for (ServerWorld world : server.getWorlds()) {
				for (var entity : world.iterateEntities()) {
					if (entity instanceof VillagerEntity villager) {
						registerVillager(villager, world);
					}
				}
			}
			for(var entry: new HashMap<>(territories).entrySet()){
				if(entry.getValue().status() != TerritoryStatus.PROVISIONAL){
					continue;
				}

				ServerWorld world = server.getWorld(entry.getKey().dimension());
				if(world!=null){
					scheduleTerritoryCheck(entry.getKey(),world);
				}

			}



		});

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if(entity instanceof VillagerEntity villager){
				registerVillager(villager,world);



			}


		});
		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if(!(entity instanceof VillagerEntity villager) ){return;}

				VillageKey villageKey = villager.getAttached(Village_ATTACHMENT);
				if(villageKey == null){return;}
			Village village = villages.get(villageKey);
				if(village!=null){village.removeLoadedVillager(villager);}




		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			currentTick++;
			if(!checkingCandidate && !candidates.isEmpty()){
				TerritoryKey candidate = candidates.iterator().next();
				ServerWorld world = server.getWorld(candidate.dimension());
				if(retryAfterTick.getOrDefault(candidate, 0L) > currentTick){
						candidates.remove(candidate);
						candidates.add(candidate);
				}else {


					if (world == null) {
						LOGGER.error("Brak świata dla kandydata {}", candidate);
						candidates.remove(candidate);
					} else {
						checkingCandidate = true;

						try{
							var future = world.getChunkManager().getChunkFutureSyncOnMainThread(
									candidate.position().x,
									candidate.position().z,
									ChunkStatus.STRUCTURE_STARTS,
									true
							);

							future.whenComplete((result, error) -> {
								server.execute(() -> {
									try {
										if (error != null) {
											LOGGER.error("Candidate {} couldn't load properly.", candidate.position(), error);
											retryAfterTick.put(candidate, currentTick + 100);
											return;
										}

										var structures = world.getRegistryManager()
												.getOrThrow(RegistryKeys.STRUCTURE);
										Chunk chunk = result.orElseThrow(() -> new RuntimeException("Nie udało się pobrać chunka"));
										for (var entry : chunk.getStructureStarts().entrySet()) {
											if (!structures.getEntry(entry.getKey()).isIn(StructureTags.VILLAGE)) {
												continue;
											}
											StructureStart start = entry.getValue();
											if (!start.hasChildren()) {
												continue;
											}
											registerVillage(start,world);
										}
										TerritoryKey key = new TerritoryKey(world.getRegistryKey(), chunk.getPos());
										getData(server).putCheckedCandidates(key);
										checkedCandidates.add(key);


										for (Set<TerritoryKey> pending : pendingTerritories.values()) {
											pending.remove(candidate);
										}
										candidates.remove(candidate);
										retryAfterTick.remove(candidate);


									} catch (Exception exception) {
										LOGGER.error("Błąd przetwarzania kandydata {}", candidate, exception);
										retryAfterTick.put(candidate, currentTick + 100);
									}finally {
										checkingCandidate = false;
									}








								});


							});

						}catch(Exception exception){
							LOGGER.error("Nie udało się rozpocząć sprawdzania {}",candidate,exception);
							checkingCandidate = false;
							retryAfterTick.put(candidate, currentTick + 100);
						}







					}



				}}
			var iterator = pendingTerritories.entrySet().iterator();
			while (iterator.hasNext()) {
				var entry = iterator.next();
				if (entry.getValue().isEmpty()) {
					try{
						if (confirmTerritory(entry.getKey(), server)) {
							iterator.remove();
						}
					}catch(Exception exception){
						LOGGER.error("Błąd zatwierdzania terytorium {}",entry.getKey(),exception);
					}
				}
			}
			for (State s : states.values()) {
				s.tick();

			}

		});

		ServerChunkEvents.CHUNK_LOAD.register((world, chunk)-> {
			var structures = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE);



			var villageStarts = world.getStructureAccessor().getStructureStarts(
					chunk.getPos(),
					structure -> structures.getEntry(structure).isIn(StructureTags.VILLAGE)
			);

			for (StructureStart start : villageStarts) {
				registerVillage(start,world);
			}
			registerChunk(chunk.getPos(),world);


		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			SERVER = null;
		});

	}











	}
