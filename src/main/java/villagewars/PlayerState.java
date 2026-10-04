package villagewars;

import java.util.Set;
import java.util.UUID;

public class PlayerState extends State{
    private UUID owner;


    public PlayerState( String name,UUID owner) {
        super(name);
        this.owner = owner;
        setStateType(StateType.PLAYER);

    }
    public PlayerState(UUID stateId, String name, int emeralds, Set<UUID> warList, StateType stateType,UUID owner){
        super(stateId,name,emeralds,warList,stateType);
        this.owner = owner;
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
