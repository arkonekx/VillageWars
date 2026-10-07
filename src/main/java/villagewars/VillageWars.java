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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class VillageWars implements ModInitializer {
	public static Map<UUID,State> states = new HashMap<>();
	public static Map<VillageKey,UUID> owners = new HashMap<>();
	public static Map<VillageKey, Village> villages = new HashMap<>();

	public static final String MOD_ID = "villagewars";
	public static final AttachmentType<String> OWNER_STATE = AttachmentRegistry.create(
			Identifier.of(MOD_ID, "owner_state"),
			builder -> builder.persistent(Codec.STRING)
	);
	public static final AttachmentType<VillageKey> Village_ATTACHMENT = AttachmentRegistry.create(
			Identifier.of(MOD_ID, "village"),
			builder -> builder.persistent(VillageKey.CODEC)
	);
	public static MinecraftServer SERVER;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	public static void registerVillager(VillagerEntity villager,ServerWorld world ){
		if(villager.hasAttached(VillageWars.Village_ATTACHMENT)){
			Village village = villages.get(villager.getAttached(Village_ATTACHMENT));
			if(village!=null){
				village.addLoadedVillager(villager);
				village.addResidentId(villager.getUuid());
			}
		}else{
			Village village = findNearestVillage(villager.getBlockPos(),150D,world);
			if(village!=null){
				village.addLoadedVillager(villager);
				village.addResidentId(villager.getUuid());
				villager.setAttached(Village_ATTACHMENT,findVillageKey(village));
			}

		}
	}

	public static Village findNearestVillage(BlockPos startPosition, double maxDistance,ServerWorld world){
		double max = maxDistance * maxDistance;
		Village village = null;
		for(Village v: villages.values()){
			double distance = v.getPosition().getSquaredDistance(startPosition);
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
		});


		ServerLifecycleEvents.SERVER_STARTED.register(server ->{
			VillageWarsData data = server.getOverworld().getPersistentStateManager().getOrCreate(VillageWarsData.TYPE);


			owners = new HashMap<>(data.getOwnerEntries());


			for(VillageEntry v : data.getVillageEntries()){
				villages.put(v.village(),RebuildVillageFromVillageEntry(v,server.getWorld(v.village().Dimension())));
				LOGGER.info("Wioska "+v.pozycja());
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



		});

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if(entity instanceof VillagerEntity villager){
				registerVillager(villager,world);
				for(Village v: villages.values()){
					for(UUID v1 : v.getResidentId()){
						if(v1.equals(villager.getUuid())){
							LOGGER.info(villager.getUuidAsString()+" - "+v.getName());
						}
					}
				}


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


    VillageKey key = new VillageKey(world.getRegistryKey(), start.getPos());
				VillageWarsData data = world.getServer().getOverworld()
						.getPersistentStateManager()
						.getOrCreate(VillageWarsData.TYPE);
    if (villages.containsKey(key) || data.getVillage(key) != null) continue;

    BlockPos center = start.getBoundingBox().getCenter();



    Village village = new Village(center, world);
    villages.put(key, village);


	data.putVillage(key,village);
	State state = new AIState(village.getName()+" State");
	UUID stateId = state.getStateId();

	owners.put(key,stateId);
	data.putOwner(key,stateId);
	states.put(stateId,state);
	state.addVillage(village);
	data.putState(stateId,state);
	for (var entity : world.iterateEntities()){
		if(entity instanceof VillagerEntity villager){
			registerVillager(villager,world);
		}
	}
	LOGGER.debug("Utworzono wioskę: key={}, stateId={}", key, stateId);
}

		});

	}











	}
