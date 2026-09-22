package villagewars;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


public class AIState extends State{
    private Random czynnikLosowy = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger("AIState");
    private int ticksUntilNextAction  = 0;
    private Personality personality;






    public AIState(String name) {
        super(name);

        Personality[] personalities = Personality.values();
        this.personality = personalities[ czynnikLosowy.nextInt(personalities.length)];
    }
    private int randomInterval(){

        return 10 + czynnikLosowy.nextInt(10);
    }
    public void tick(){

        ticksUntilNextAction--;
        if(ticksUntilNextAction>0){
            return;
        }

            List<State> neighbours = this.findNeighbours(VillageWars.allStates);
            State target = findBestTarget(neighbours);
        LOGGER.info("Target: " + (target != null ? target.getName() : "NULL"));
            if (target != null) {
                double score = calculateAttackScore(neighbours, target);
                LOGGER.info(String.valueOf(score));

                if (score > 15) {  // próg do dostrojenia później
                    // atakuj
                    LOGGER.info(this.getName() + " atakuje " + target.getName());

                }
            }
            ticksUntilNextAction = randomInterval();

    }


    public Double averageEmeralds(List<State> neighbours){
        double total = 0;
        for(State s: neighbours){
            total += s.getEmeralds();
        }
        return total / neighbours.size();


    }

    public Double averageVillagers(List<State> neighbours){
        int total = 0;
        for(State s: neighbours){

                total += s.getTotalVillagers();

        }
        double avgVillagers = total / neighbours.size();
        return avgVillagers;
    }
    public double calculateAttackScore(List<State> neighbours,State target){
        double score = 100;
        double avgVill = averageVillagers(neighbours);
        double avgEm = averageEmeralds(neighbours);
        score *=  this.getTotalVillagers() / avgVill;
        score *= this.getEmeralds() / avgEm;
        score *= target.getEmeralds() / avgEm;
        score *= avgVill / target.getTotalVillagers();

        double losowaLiczba = czynnikLosowy.nextDouble();
        losowaLiczba = 0.5 + (losowaLiczba * 0.8);
        score *= losowaLiczba;
        // Sojusz
        score *= personality.attackMultiplier;
        return score;
    }
    public State findBestTarget(List<State> neighbours){
        State bestState = null;
        double bestScore = Integer.MIN_VALUE;
        for (State s: neighbours){
            double score = this.calculateAttackScore(neighbours,s);
            if( score>bestScore){
                bestScore = score;
                bestState = s;
            }
        }
        return bestState;
    }


}
