package villagewars;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;

/** Model klienta; wspolpracuje z SoldierEntityRenderer i SoldierRenderState. */
public class HeavyInfantryModel extends BipedEntityModel<SoldierRenderState> {
    private final ModelPart nose;

    public HeavyInfantryModel(ModelPart root) {
        super(root);
        this.nose = root.getChild("nose");
    }

    @Override
    public void setAngles(SoldierRenderState state) {
        // BipedEntityModel resetuje transformacje i ustawia animacje ruchu.
        super.setAngles(state);
        this.body.xScale = 1.50F;
        this.body.zScale = 1.65F;
        this.rightArm.originX = -7.0F;
        this.leftArm.originX = 7.0F;
        this.rightArm.xScale = this.leftArm.xScale = 1.20F;
        this.rightArm.zScale = this.leftArm.zScale = 1.25F;
        this.rightLeg.xScale = this.leftLeg.xScale = 1.10F;
        this.rightLeg.zScale = this.leftLeg.zScale = 1.15F;

        // Proporcje glowy i nos jak u lekkiej piechoty; bez czapki.
        this.head.xScale = 1.15F;
        this.head.yScale = 1.20F;
        this.head.zScale = 1.15F;
        this.hat.visible = false;
        this.nose.setOrigin(this.head.originX, this.head.originY, this.head.originZ);
        this.nose.setAngles(this.head.pitch, this.head.yaw, this.head.roll);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = BipedEntityModel.getModelData(new Dilation(0.0F), 0.0F);
        data.getRoot().addChild("nose",
                ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-1.0F, -3.0F, -6.6F, 2.0F, 4.0F, 2.0F),
                ModelTransform.NONE);
        return TexturedModelData.of(data, 64, 64);
    }
}
