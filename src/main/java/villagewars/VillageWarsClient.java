package villagewars;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class VillageWarsClient implements ClientModInitializer {
    public static final EntityModelLayer SOLDIER =
            new EntityModelLayer(Identifier.of(VillageWars.MOD_ID, "soldier"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererFactories.register(ModEntities.LIGHT_INFANTRY,
                ctx -> new SoldierEntityRenderer<>(
                        ctx,
                        new SoldierEntityModel(ctx.getPart(LIGHT_MODEL_LAYER))
                )
        );
        EntityRendererFactories.register(ModEntities.HEAVY_INFANTRY,
                ctx -> new SoldierEntityRenderer<>(
                        ctx,
                        new HeavyInfantryModel(ctx.getPart(HEAVY_MODEL_LAYER))
                )
        );
        EntityRendererFactories.register(ModEntities.ARCHER,
                ctx -> new SoldierEntityRenderer<>(
                        ctx,
                        new ArcherModel(ctx.getPart(ARCHER_MODEL_LAYER))
                )
        );
        EntityModelLayerRegistry.registerModelLayer(SOLDIER, SoldierEntityModel::getTexturedModelData);

    }
}
