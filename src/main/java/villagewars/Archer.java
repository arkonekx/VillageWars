package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Archer extends Soldier{

    public final static double BASE_ARMOR = 0;
    public final static double BASE_SPEED = 0.35;
    public final static double BASE_HEALTH = 12;
    public final static double BASE_ATTACK = 1;


    public Archer(EntityType<Archer> entityType, World world){
        super(entityType,world);

    }
    public Identifier getTexture(){
        return Identifier.of(VillageWars.MOD_ID,"textures/entity/archer.png");
    }

}
