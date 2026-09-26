package villagewars;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;

/** Model klienta; wspolpracuje z SoldierEntityRenderer i SoldierRenderState. */
public class ArcherModel extends BipedEntityModel<SoldierRenderState> {
    private final ModelPart nose;

    public ArcherModel(ModelPart root) {
        super(root);
        this.nose = root.getChild("nose");
    }

    @Override
    public void setAngles(SoldierRenderState state) {
        // BipedEntityModel resetuje transformacje i ustawia animacje ruchu.
        super.setAngles(state);
        // Krotszy tulow i nogi; stopy nadal na poziomie y=24.
        this.body.xScale = 0.95F;
        this.body.yScale = 0.875F;
        this.body.zScale = 1.10F;
        this.body.originY += 3.0F;
        this.head.originY += 3.0F;
        this.rightArm.originX = -4.6F;
        this.leftArm.originX = 4.6F;
        this.rightArm.originY += 3.0F;
        this.leftArm.originY += 3.0F;
        this.rightArm.xScale = this.leftArm.xScale = 0.85F;
        this.rightArm.yScale = this.leftArm.yScale = 0.875F;
        this.rightArm.zScale = this.leftArm.zScale = 0.90F;
        this.rightLeg.originY += 1.5F;
        this.leftLeg.originY += 1.5F;
        this.rightLeg.xScale = this.leftLeg.xScale = 0.90F;
        this.rightLeg.yScale = this.leftLeg.yScale = 0.875F;
        this.rightLeg.zScale = this.leftLeg.zScale = 0.90F;

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
