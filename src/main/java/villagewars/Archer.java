package villagewars;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Archer extends Soldier implements RangedAttackMob {

    public final static double BASE_ARMOR = 0;
    public final static double BASE_SPEED = 0.35;
    public final static double BASE_HEALTH = 12;
    public final static double BASE_ATTACK = 1;


    public Archer(EntityType<Archer> entityType, World world){
        super(entityType,world);

    }
    private boolean hasBow(){
        return this.isHolding(stack -> stack.isOf(Items.BOW));
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        if (!(this.getEntityWorld() instanceof ServerWorld world)) {
            return;
        }

        ArrowEntity arrow = new ArrowEntity(this.getEntityWorld(),this,new ItemStack(Items.ARROW),null);

        double dx = target.getX() - this.getX();
        double dy = target.getBodyY(0.5) - arrow.getY();
        double dz = target.getZ() - arrow.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

        arrow.setVelocity(dx,dy+horizontalDistance*0.2,dz,1.6F,1.0F);

        world.spawnEntity(arrow);

    }


    public Identifier getTexture(){
        return Identifier.of(VillageWars.MOD_ID,"textures/entity/archer.png");
    }
    @Override
    protected void initGoals(){
        this.goalSelector.add(0,new SwimGoal(this));
        this.goalSelector.add(1,new ProjectileAttackGoal(this,1.1D,20,15F){
                    @Override
                    public boolean canStart(){
                        return Archer.this.hasBow() && super.canStart();
                    }

                    @Override
                    public boolean shouldContinue() {
                        return Archer.this.hasBow() && super.shouldContinue();
                    }
                }
        );
        this.goalSelector.add(1,new FleeEntityGoal<>(this, HostileEntity.class,12.0F,1.0D,1.2D){
            @Override
            public boolean canStart() {
                return !Archer.this.hasBow() && super.canStart();
            }

            @Override
            public boolean shouldContinue() {
                return !Archer.this.hasBow() && super.shouldContinue();
            }
        });
        this.goalSelector.add(5,new WanderAroundFarGoal(this,0.8D));
        this.goalSelector.add(6,new LookAtEntityGoal(this, PlayerEntity.class,8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1,new ActiveTargetGoal<>(this, HostileEntity.class,true));

    }

}
