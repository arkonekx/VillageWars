package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Soldier extends PathAwareEntity {


    public Soldier(EntityType<? extends PathAwareEntity> entityType, World world) {

        super(entityType, world);

    }
    public void tick(){
        super.tick();

    }
    @Override
    protected void initGoals(){
        this.goalSelector.add(0,new SwimGoal(this));
        this.goalSelector.add(1,new MeleeAttackGoal(this,1.1D,false));
        this.goalSelector.add(5,new WanderAroundFarGoal(this,0.8D));
        this.goalSelector.add(6,new LookAtEntityGoal(this, PlayerEntity.class,8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1,new ActiveTargetGoal<>(this, HostileEntity.class,true));

    }


    public abstract Identifier getTexture();
    public abstract double getBaseMaxHealth();
    public abstract double getBaseMovementSpeed();
    public abstract double getBaseArmor();
    public abstract double getBaseAttackDamage();


}
