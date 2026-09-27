package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Spearman extends Soldier{

    public static final double BASE_SPEED = 0.2;
    public static final double BASE_ARMOR = 6.5;
    public static final double BASE_HEALTH = 24;
    public static final double BASE_ATTACK = 2;

    public Spearman(EntityType<Spearman> entityType, World world){
        super(entityType,world);
    }
    public Identifier getTexture(){
        return Identifier.of(VillageWars.MOD_ID,"textures/entity/spearman.png");
    }
}
