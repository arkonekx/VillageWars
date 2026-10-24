package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public record TerritoryKey( RegistryKey<World> dimension,ChunkPos position) {
    public static final Codec<TerritoryKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryKey.createCodec(RegistryKeys.WORLD).fieldOf("dimension").forGetter(TerritoryKey::dimension),
            ChunkPos.CODEC.fieldOf("position").forGetter(TerritoryKey::position)
    ).apply(instance,TerritoryKey::new)
    );
}
