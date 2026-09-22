package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LightInfantry extends Soldier{

    public LightInfantry(EntityType<LightInfantry> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public double getBaseArmor(){
        return 5;
    }
    @Override
    public double getBaseMaxHealth(){
        return 20;
    }
    @Override
    public double getBaseMovementSpeed(){
        return 0.2;
    }
    @Override
    public double getBaseAttackDamage(){
        return 1.4;
    }
    @Override
    public Identifier getTexture(){
        return Identifier.of(VillageWars.MOD_ID,"textures/entity/light_infantry_3.png");
    }


}
