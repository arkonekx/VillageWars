package villagewars;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum TerritoryStatus implements StringIdentifiable {
    PROVISIONAL("Provisional"),
    CONFIRMED("Confirmed");

    public static final Codec<TerritoryStatus> CODEC =  StringIdentifiable.createCodec(TerritoryStatus::values);

    private final String id;

    TerritoryStatus(String id){this.id = id;}

    @Override
    public String asString() {
        return id;
    }
}
