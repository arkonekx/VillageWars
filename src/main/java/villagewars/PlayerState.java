package villagewars;

import java.util.UUID;

public class PlayerState extends State{
    private UUID owner;

    public PlayerState(UUID owner, String name) {
        super(name);
        this.owner = owner;

    }
    public UUID getOwner(){
        return this.owner;
    }

    @Override
    public void tick(){

    }
}
