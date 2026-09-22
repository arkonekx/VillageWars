package villagewars;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class VillageWars implements ModInitializer {
	public static List<State> allStates = new ArrayList<>();
	public static List<Village> allVillages = new ArrayList<>();

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


		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			SERVER = server;
			handler.player.sendMessage(
					Text.literal("VillagerWars załadowany!"), false
			);
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {

			for (State s : allStates) {
				s.tick();
			}

		});





		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			SERVER.getPlayerManager().broadcast(
					Text.literal(entity.getName().getString() + " died"),false

			);
			LOGGER.info("Typ encji: " + entity.getType());
			LOGGER.info("Identifier: " + EntityType.getId(entity.getType()));

		});


		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {



			if(!player.isSpectator()){
				if(entity instanceof VillagerEntity && hand == Hand.MAIN_HAND && !world.isClient() && hitResult != null){
					BlockPos pozycjawiochy = new BlockPos((int) entity.getX(),(int) entity.getY(),(int) entity.getZ());
					Village wiocha = findNearbyVillage(pozycjawiochy,50);
					if(wiocha ==null){
						wiocha = new Village(pozycjawiochy,world);
						State nowyKraj = new AIState(wiocha.getName() + " State");
						allStates.add(nowyKraj);
						nowyKraj.addVillage(wiocha);
						allVillages.add(wiocha);
						LOGGER.info("Liczba państw: " + VillageWars.allStates.size());
					}
					UUID owner = wiocha.getOwner();
					if(owner == null){
						owner = player.getUuid();
						wiocha.setOwner(owner);
						player.sendMessage(Text.literal("You just claimed this village"), false);

					}else{
						PlayerEntity ownerPlayer = SERVER.getPlayerManager().getPlayer(owner);
						String nick;
						if(ownerPlayer!=null){
							nick = ownerPlayer.getName().getString();
						}else{
							nick = "nieznany";
						}

						player.sendMessage(Text.literal("This village is owned by " + nick), false);
					}



					LOGGER.info(wiocha.getName() +" ma "+wiocha.getVillagers().size()+" villagerów");





				SERVER.getPlayerManager().broadcast(
						Text.literal(entity.getName().getString()+ " is on coordinates " + (int) entity.getX()+" "+ (int) entity.getY()+" "+ (int) entity.getZ()+" "),false
				);
			}}


			return ActionResult.PASS;
		});

		}

	public static Village findNearbyVillage(BlockPos pos, double maxDistance) {
		for(Village v : VillageWars.allVillages){
			if(pos.getSquaredDistance(v.getPosition())<maxDistance*maxDistance){
				return v;
			}

		}
		return null;
	}
	}
