package villagewars;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public record VillageKey(RegistryKey<World> Dimension, ChunkPos pozycja) {
}
