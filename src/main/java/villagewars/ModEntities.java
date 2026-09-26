package villagewars;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {


    public static final RegistryKey<EntityType<?>> ARCHER_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"archer")

    );

    public static final EntityType<Archer> ARCHER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"archer"),
            EntityType.Builder.create(Archer::new,SpawnGroup.MISC).dimensions(0.6F,1.95F).build(ARCHER_KEY)

    );


    public static final RegistryKey<EntityType<?>> LIGHT_INFANTRY_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"light_infantry")

    );

    public static final RegistryKey<EntityType<?>> HEAVY_INFANTRY_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"heavy_infantry")

    );

    public static final EntityType<HeavyInfantry> HEAVY_INFANTRY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"heavy_infantry"),
            EntityType.Builder.create(HeavyInfantry::new,SpawnGroup.MISC).dimensions(0.6F,1.95F).build(HEAVY_INFANTRY_KEY)

    );

    public static final EntityType<LightInfantry> LIGHT_INFANTRY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"light_infantry"),
            EntityType.Builder.create(LightInfantry::new,SpawnGroup.MISC).dimensions(0.6F,1.95F).build(LIGHT_INFANTRY_KEY)

    );


    public static void registerAll(){
        FabricDefaultAttributeRegistry.register(LIGHT_INFANTRY, LightInfantry.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH,LightInfantry.BASE_HEALTH)
                .add(EntityAttributes.MOVEMENT_SPEED,LightInfantry.BASE_SPEED)
                .add(EntityAttributes.ARMOR,LightInfantry.BASE_ARMOR)
                .add(EntityAttributes.ATTACK_DAMAGE,LightInfantry.BASE_ATTACK)



        );
        FabricDefaultAttributeRegistry.register(HEAVY_INFANTRY, HeavyInfantry.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH,HeavyInfantry.BASE_HEALTH)
                .add(EntityAttributes.MOVEMENT_SPEED,HeavyInfantry.BASE_SPEED)
                .add(EntityAttributes.ARMOR,HeavyInfantry.BASE_ARMOR)
                .add(EntityAttributes.ATTACK_DAMAGE,HeavyInfantry.BASE_ATTACK)



        );
        FabricDefaultAttributeRegistry.register(ARCHER, Archer.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH,Archer.BASE_HEALTH)
                .add(EntityAttributes.MOVEMENT_SPEED,Archer.BASE_SPEED)
                .add(EntityAttributes.ARMOR,Archer.BASE_ARMOR)
                .add(EntityAttributes.ATTACK_DAMAGE,Archer.BASE_ATTACK)



        );

    }


}
