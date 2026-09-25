package villagewars;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.render.entity.VillagerEntityRenderer;

public class VillageWarsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererFactories.register(ModEntities.LIGHT_INFANTRY, SoldierEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModEntities.SOLDIER, SoldierEntityModel::getTexturedModelData);
        EntityRendererFactories.register(ModEntities.LIGHT_INFANTRY, SoldierEntityRenderer::new);
    }
}
