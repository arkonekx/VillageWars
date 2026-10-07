package villagewars;




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



    public State(String name){
        this.stateId = UUID.randomUUID();
        this.name = name;
//        this.TERRITORY_RADIUS = 150;
        this.emeralds = 10;
        this.villages = new ArrayList<>();
        this.isAtWar = false;
        this.warList = new HashSet<>();
        this.stateType = this instanceof AIState ? StateType.AI : StateType.PLAYER;

    }
    public State(UUID stateId,String name, int emeralds, Set<UUID> warList, StateType stateType){
        this.stateId = stateId;
        this.name = name;
        this.emeralds = emeralds;
        this.warList = new HashSet<>(warList);
        this.stateType = stateType;
        this.villages = new ArrayList<>();
        this.isAtWar = !warList.isEmpty();
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
            total += v.getResidentId().size();
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
        this.warList = new HashSet<>(warList);
    }
    public Set<UUID> getWarList() {
        return Set.copyOf(warList);
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
