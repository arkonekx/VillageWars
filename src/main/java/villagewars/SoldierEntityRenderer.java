package villagewars;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.util.Identifier;

public class SoldierEntityRenderer<T extends Soldier> extends MobEntityRenderer<T, SoldierRenderState, VillagerResemblingModel> {

    public SoldierEntityRenderer(EntityRendererFactory.Context context){
        super(context, new VillagerResemblingModel(context.getPart(EntityModelLayers.VILLAGER)),0.5F);

    }

    @Override
    public SoldierRenderState createRenderState(){
        return new SoldierRenderState();
    }
    @Override
    public void updateRenderState(T entity,SoldierRenderState state,float tickDelta ){
        super.updateRenderState(entity,state,tickDelta);
        state.texture = entity.getTexture();
    }

    @Override
    public Identifier getTexture(SoldierRenderState state){
        return state.texture;
    }


}
