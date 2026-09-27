package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Militia extends Soldier{

    public final static double BASE_ATTACK = 0.5;
    public final static double BASE_HEALTH = 16;
    public final static double BASE_ARMOR = 3;
    public final static double BASE_SPEED = 0.4;

    public Militia(EntityType<Militia> entityType, World world){
        super(entityType,world);
    }
    public Identifier getTexture(){
        return Identifier.of(VillageWars.MOD_ID,"textures/entity/militia.png");
    }
}
