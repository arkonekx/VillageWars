package villagewars;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class SoldierEntityModel extends BipedEntityModel<SoldierRenderState> {
    private final ModelPart nose;

    public SoldierEntityModel(ModelPart root) {
        super(root);
        this.nose = root.getChild("head").getChild("nose");
        this.head.xScale = 1.4F;
        this.head.yScale = 1.4F;
        this.head.zScale = 1.4F;

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F),0.0F);

        ModelPartData headData = modelData.getRoot().getChild("head");

        headData.addChild(
                "nose",
                ModelPartBuilder.create().uv(0,0).cuboid(-1.0F,-2.0F,-6.0F, 2.0F, 2.0F, 2.0F,new Dilation(0.0F)),
                ModelTransform.NONE

        );
        return TexturedModelData.of(modelData,64,64);
    }
}