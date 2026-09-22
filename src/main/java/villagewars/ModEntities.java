package villagewars;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {


    public static final RegistryKey<EntityType<?>> LIGHT_INFANTRY_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"light_infantry")

    );


    public static final EntityType<LightInfantry> LIGHT_INFANTRY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(VillageWars.MOD_ID,"light_infantry"),
            EntityType.Builder.create(LightInfantry::new,SpawnGroup.MISC).dimensions(0.6F,1.95F).build(LIGHT_INFANTRY_KEY)

    );


    public static void registerAll(){
        FabricDefaultAttributeRegistry.register(LIGHT_INFANTRY, LightInfantry.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH,20.0)
                .add(EntityAttributes.MOVEMENT_SPEED,0.2)
                .add(EntityAttributes.ARMOR,5.0)
                .add(EntityAttributes.ATTACK_DAMAGE,1.4)



        );

    }


}
