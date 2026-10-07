package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;


public record VillageKey(RegistryKey<World> Dimension, ChunkPos pozycja) {
    public static final Codec<VillageKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryKey.createCodec(RegistryKeys.WORLD).fieldOf("dimension").forGetter(VillageKey::Dimension),
            Codec.INT.fieldOf("chunk_x").forGetter(key -> key.pozycja().x),
            Codec.INT.fieldOf("chunk_z").forGetter(key -> key.pozycja().z)
    ).apply(instance, (dimension,x,z) -> new VillageKey(dimension,new ChunkPos(x,z))));

}
