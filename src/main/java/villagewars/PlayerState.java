package villagewars;

import java.util.UUID;

public class PlayerState extends State{
    private UUID owner;


    public PlayerState(UUID stateId, String name,UUID owner) {
        super(stateId,name);
        this.owner = owner;
        setStateType(StateType.PLAYER);

    }
    public UUID getOwner(){
        return this.owner;
    }

    @Override
    public void tick(){

    }
    public void setOwner(UUID owner){
        this.owner = owner;
    }
}
