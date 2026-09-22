package villagewars;

public enum Personality {
    AGGRESSIVE(1.5),
    DEFENSIVE(0.5),
    TRADING(0.3),
    EXPANSIVE(1.2);

    public final double attackMultiplier;

    Personality(double attack){
        this.attackMultiplier = attack;
    }

}
