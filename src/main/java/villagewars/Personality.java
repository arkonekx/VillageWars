package villagewars;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum Personality implements StringIdentifiable{
    AGGRESSIVE("aggressive",1.5),
    DEFENSIVE("defensive",0.5),
    TRADING("trading",0.3),
    EXPANSIVE("expansive",1.2);

    public final double attackMultiplier;
    private final String id;
    public static final Codec<Personality> CODEC = StringIdentifiable.createCodec(Personality::values);
    Personality(String id,double attack){
        this.attackMultiplier = attack;
        this.id = id;
    }
    @Override
    public String asString(){
        return id;
    }

}
