package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.UUID;

public record VillageEntry(VillageKey village, String name, BlockPos pozycja, Set<UUID> residentId) {
    public static final Codec<VillageEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VillageKey.CODEC.fieldOf("village").forGetter(VillageEntry::village),
            Codec.STRING.fieldOf("name").forGetter(VillageEntry::name),
            BlockPos.CODEC.fieldOf("pozycja").forGetter(VillageEntry::pozycja),
            Uuids.SET_CODEC.fieldOf("residentId").forGetter(VillageEntry::residentId)
    ).apply(instance,VillageEntry::new));
}
