package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record StateEntry(UUID id, String name, int emeralds, Set<UUID> warList, StateType stateType, Optional<UUID> playerId)  {
    public static final Codec<StateEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Uuids.STRING_CODEC.fieldOf("state_id").forGetter(StateEntry::id),
            Codec.STRING.fieldOf("name").forGetter(StateEntry::name),
            Codec.INT.fieldOf("emeralds").forGetter(StateEntry::emeralds),
            Uuids.SET_CODEC.fieldOf("warList").forGetter(StateEntry::warList),
            StateType.CODEC.fieldOf("stateType").forGetter(StateEntry::stateType),
            Uuids.STRING_CODEC.optionalFieldOf("playerId").forGetter(StateEntry::playerId)

    ).apply(instance,StateEntry::new));

}
