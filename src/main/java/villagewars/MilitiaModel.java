package villagewars;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;

/** Model klienta; wspolpracuje z SoldierEntityRenderer i SoldierRenderState. */
public class MilitiaModel extends BipedEntityModel<SoldierRenderState> {
    private final ModelPart nose;

    public MilitiaModel(ModelPart root) {
        super(root);
        this.nose = root.getChild("nose");
    }

    @Override
    public void setAngles(SoldierRenderState state) {
        // BipedEntityModel resetuje transformacje i ustawia animacje ruchu.
        super.setAngles(state);
        // Nieco mniejszy od Light Infantry, nadal postawny.
        this.body.xScale = 1.15F;
        this.body.yScale = 0.95F;
        this.body.zScale = 1.30F;
        this.body.originY += 1.20F;
        this.head.originY += 1.20F;
        this.rightArm.originX = -5.60F;
        this.leftArm.originX = 5.60F;
        this.rightArm.originY += 1.20F;
        this.leftArm.originY += 1.20F;
        this.rightArm.yScale = this.leftArm.yScale = 0.95F;
        this.rightLeg.originY += 0.60F;
        this.leftLeg.originY += 0.60F;
        this.rightLeg.yScale = this.leftLeg.yScale = 0.95F;

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
