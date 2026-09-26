package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class HeavyInfantry extends Soldier{
    public final static double BASE_ARMOR = 10;
    public final static double BASE_SPEED = 0.15;
    public final static double BASE_ATTACK = 4;
    public final static double BASE_HEALTH = 30;


    public HeavyInfantry (EntityType<HeavyInfantry> entityType, World world){
        super(entityType,world);
    }


    @Override
    public Identifier getTexture() {
        return Identifier.of(VillageWars.MOD_ID,"textures/entity/light_infantry_biped_5.png");
    }
}
