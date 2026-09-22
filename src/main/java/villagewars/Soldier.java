package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Soldier extends PathAwareEntity {


    public Soldier(EntityType<? extends PathAwareEntity> entityType, World world) {

        super(entityType, world);

    }
    public void tick(){
        super.tick();
        // System.out.println(this.isCustomNameVisible());
    }


    public abstract Identifier getTexture();
    public abstract double getBaseMaxHealth();
    public abstract double getBaseMovementSpeed();
    public abstract double getBaseArmor();
    public abstract double getBaseAttackDamage();


}
