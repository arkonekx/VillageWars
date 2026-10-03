package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record OwnerEntry(VillageKey village, UUID stateId){
    public static final Codec<OwnerEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VillageKey.CODEC.fieldOf("village").forGetter(OwnerEntry::village),
            Uuids.STRING_CODEC.fieldOf("state_id").forGetter(OwnerEntry::stateId)
    ).apply(instance,OwnerEntry::new));
}