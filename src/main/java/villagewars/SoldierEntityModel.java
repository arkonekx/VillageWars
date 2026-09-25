package villagewars;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class SoldierEntityModel extends BipedEntityModel<SoldierRenderState> {
    private final ModelPart nose;

    public SoldierEntityModel(ModelPart root) {
        super(root);
        this.nose = root.getChild("nose");
    }

    @Override
    public void setAngles(SoldierRenderState state) {
        super.setAngles(state);

        // A villager-like silhouette without changing the existing 64x64 UV layout.
        this.body.xScale = 1.25F; // 8 -> 10 model units wide
        this.body.zScale = 1.5F;  // 4 -> 6 model units deep
        this.rightArm.originX = -6.0F;
        this.leftArm.originX = 6.0F;

        this.head.xScale = 1.15F;
        this.head.yScale = 1.2F;
        this.head.zScale = 1.15F;
        this.hat.xScale = this.head.xScale;
        this.hat.yScale = this.head.yScale;
        this.hat.zScale = this.head.zScale;

        // The nose follows the head's motion but does not inherit its scale.
        this.nose.setOrigin(this.head.originX, this.head.originY, this.head.originZ);
        this.nose.setAngles(this.head.pitch, this.head.yaw, this.head.roll);
    }



    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F), 0.0F);

        modelData.getRoot().addChild(
                "nose",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0F, -3.0F, -6.6F, 2.0F, 4.0F, 2.0F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 64);
    }
}
