package villagewars;



import com.llamalad7.mixinextras.lib.antlr.runtime.misc.Array2DHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public abstract class State {
    private int emeralds;
    private String name;
    private List<Village> villages;
    private boolean isAtWar;
    private Set<UUID> warList;
//    private static double TERRITORY_RADIUS;
    private StateType stateType;
    private UUID stateId;


    public State(UUID stateId, String name){
        this.stateId = stateId;
        this.name = name;
//        this.TERRITORY_RADIUS = 150;
        this.emeralds = 10;
        this.villages = new ArrayList<>();
        this.isAtWar = false;
        this.warList = new HashSet<>();
        this.stateType = this instanceof AIState ? StateType.AI : StateType.PLAYER;

    }



    public abstract void tick();
//    public boolean isNeighbour(State target){
//        List<Village> mojeWioski = this.villages;
//        for(Village v: target.getVillages()){
//            for(Village v1: mojeWioski){
//                if(v.getPosition().getSquaredDistance(v1.getPosition())<TERRITORY_RADIUS*TERRITORY_RADIUS){
//                    return true;
//                }
//            }
//
//
//
//    }return false;}
//
//    public List<State> findNeighbours(List<State> allStates){
//        List<State> neighbours = new ArrayList<>();
//        for (State s: allStates){
//            if(s==this){
//                continue;
//            }
//            if(this.isNeighbour(s)){
//                neighbours.add(s);
//            }
//
//
//        }
//        return neighbours;
//    }
    public String getName(){
        return this.name;
    }
    public void setEmeralds(Integer amount){
        this.emeralds = amount;
    }

    public int getTotalVillagers(){
        int total = 0;
        for(Village v: this.villages){
            total += v.getVillagers().size();
        }
        return total;
    }
    public int getEmeralds(){
        return this.emeralds;
    }
    public void addVillage(Village v){
        villages.add(v);
    }
    public void removeVillage(Village v){
        villages.remove(v);
    }
    public List<Village> getVillages(){
        return Collections.unmodifiableList(villages);
    }

    public void setWarList(Set<UUID> warList) {
        this.warList = warList;
    }
    public Set<UUID> getWarList() {
        return warList;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public StateType getStateType() {
        return stateType;
    }

    public UUID getStateId() {
        return stateId;
    }

    public void setStateId(UUID stateId) {
        this.stateId = stateId;
    }
    public boolean getIsAtWar(){
        return this.isAtWar;
    }
    public void setIsAtWar(boolean isAtWar){
        this.isAtWar = isAtWar;
    }
}
