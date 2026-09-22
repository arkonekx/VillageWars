package villagewars;



import java.util.ArrayList;
import java.util.List;


public abstract class State {
    private int emeralds;
    private String name;
    private List<Village> villages;
    private boolean isAtWar;
    private static double TERRITORY_RADIUS;
    public State( String name){
        this.name = name;
        this.TERRITORY_RADIUS = 150;
        this.emeralds = 10;
        this.villages = new ArrayList<>();
        this.isAtWar = false;
    }



    public abstract void tick();
    public boolean isNeighbour(State target){
        List<Village> mojeWioski = this.villages;
        for(Village v: target.getVillages()){
            for(Village v1: mojeWioski){
                if(v.getPosition().getSquaredDistance(v1.getPosition())<TERRITORY_RADIUS*TERRITORY_RADIUS){
                    return true;
                }
            }



    }return false;}

    public List<State> findNeighbours(List<State> allStates){
        List<State> neighbours = new ArrayList<>();
        for (State s: allStates){
            if(s==this){
                continue;
            }
            if(this.isNeighbour(s)){
                neighbours.add(s);
            }


        }
        return neighbours;
    }
    public String getName(){
        return this.name;
    }
    public void addEmeralds(Integer amount){
        this.emeralds += amount;
    }
    public void removeEmeralds(Integer amount){
        this.emeralds -= amount;
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
        return this.villages;
    }

}
