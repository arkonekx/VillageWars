package villagewars;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class VillageWars implements ModInitializer {

	public static Map<State,UUID> states = new HashMap<>();
	public static Map<VillageKey,UUID> owners = new HashMap<>();
	public static Map<VillageKey, Village> villages = new HashMap<>();
	public static final String MOD_ID = "villagewars";
	public static final AttachmentType<String> OWNER_STATE = AttachmentRegistry.create(
			Identifier.of(MOD_ID, "owner_state"),
			builder -> builder.persistent(Codec.STRING)
	);
	public static MinecraftServer SERVER;
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {
		ModEntities.registerAll();


//		ServerTickEvents.END_SERVER_TICK.register(server -> {
//
//			for (State s : owners) {
//				s.tick();
//			}
//
//		});

		ServerChunkEvents.CHUNK_LOAD.register((world, chunk)-> {

			var structures = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE);

			var villageStarts = world.getStructureAccessor().getStructureStarts(
					chunk.getPos(),
					structure -> structures.getEntry(structure).isIn(StructureTags.VILLAGE)
			);

			for (StructureStart start : villageStarts) {


    VillageKey key = new VillageKey(world.getRegistryKey(), start.getPos());
    if (villages.containsKey(key)) continue;

    BlockPos center = start.getBoundingBox().getCenter();



    Village village = new Village(center, world);
    villages.put(key, village);
	UUID stateId = UUID.randomUUID();
	State state = new AIState(stateId,village.getName()+" State");
	owners.put(key,stateId);
	states.put(state,stateId);
	state.addVillage(village);
}

		});

	}











	}
