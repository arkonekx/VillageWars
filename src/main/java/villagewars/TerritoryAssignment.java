package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record TerritoryAssignment(Optional<VillageKey> villageKey, TerritoryStatus status) {
    public static final Codec<TerritoryAssignment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VillageKey.CODEC.optionalFieldOf("territoryKey").forGetter(TerritoryAssignment::villageKey),
            TerritoryStatus.CODEC.fieldOf("TerritoryStatus").forGetter(TerritoryAssignment::status)
    ).apply(instance, TerritoryAssignment::new));


}
