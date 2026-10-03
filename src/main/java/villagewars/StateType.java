package villagewars;

import com.mojang.serialization.Codec;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.util.StringIdentifiable;

public enum StateType implements StringIdentifiable {
    AI("ai"),
    PLAYER("player");

    public static final Codec<StateType> CODEC = StringIdentifiable.createCodec(StateType::values);



    private final String id;

    StateType(String id){
        this.id = id;
    }

    @Override
    public String asString() {
        return id;
    }
}