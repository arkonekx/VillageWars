package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.ChunkPos;

import java.util.Optional;

public record TerritoryEntry(Optional<VillageKey> villageKey, TerritoryKey territoryKey, TerritoryStatus status) {
    public static final Codec<TerritoryEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VillageKey.CODEC.optionalFieldOf("villageKey").forGetter(TerritoryEntry::villageKey),
            TerritoryKey.CODEC.fieldOf("TerritoryKey").forGetter(TerritoryEntry::territoryKey),
            TerritoryStatus.CODEC.fieldOf("status").forGetter(TerritoryEntry::status)
    ).apply(instance, TerritoryEntry::new)
    );
}
