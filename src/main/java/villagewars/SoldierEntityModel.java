package villagewars;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class SoldierEntityModel extends BipedEntityModel<SoldierRenderState> {
    private final ModelPart nose;

    public SoldierEntityModel(ModelPart root) {
        super(root);

        this.nose = root.getChild("nose");

    }

    @Override
    public void setAngles(SoldierRenderState state){
        super.setAngles(state);

        this.head.yScale = 1.4F;
        this.head.xScale = 1.4F;
        this.head.zScale = 1.4F;

        this.nose.setOrigin(
                this.head.originX,
                this.head.originY,
                this.head.originZ


        );
        this.nose.setAngles(this.head.pitch,this.head.yaw,this.head.roll);
    }



    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F),0.0F);



        modelData.getRoot().addChild(
                "nose",ModelPartBuilder.create().uv(56,16).cuboid(-1.0F, -3.0F, -7.6F, 2.0F, 4.0F, 2.0F),ModelTransform.NONE
        );

        return TexturedModelData.of(modelData,64,64);
    }
}