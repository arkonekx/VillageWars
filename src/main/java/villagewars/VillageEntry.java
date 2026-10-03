package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;

public record VillageEntry(VillageKey village,String name, BlockPos pozycja) {
    public static final Codec<VillageEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VillageKey.CODEC.fieldOf("village").forGetter(VillageEntry::village),
            Codec.STRING.fieldOf("name").forGetter(VillageEntry::name),
            BlockPos.CODEC.fieldOf("pozycja").forGetter(VillageEntry::pozycja)
    ).apply(instance,VillageEntry::new));
}
