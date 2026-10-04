package villagewars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.*;

public class VillageWarsData extends PersistentState {
    private final Map<UUID,StateEntry> savedStates = new HashMap<>();
    private final Map<VillageKey,UUID> savedOwners = new HashMap<>();
    private final Map<VillageKey, VillageEntry> savedVillages = new HashMap<>();




    private VillageWarsData(){}

    private VillageWarsData(List<OwnerEntry> ownersSaved, List<StateEntry> statesSaved, List<VillageEntry> villageSaved) {
        for (OwnerEntry entry : ownersSaved) {
            savedOwners.put(entry.village(),entry.stateId());

        }
        for(StateEntry entry : statesSaved){
            savedStates.put(entry.id(),entry);
        }
        for(VillageEntry entry : villageSaved){
            savedVillages.put(entry.village(),entry);
        }
    }


    private static final Codec<VillageWarsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            OwnerEntry.CODEC.listOf().fieldOf("owners").forGetter(data -> data.savedOwners.entrySet().stream().map(entry -> new OwnerEntry(entry.getKey(),entry.getValue())).toList()),
            StateEntry.CODEC.listOf().fieldOf("states").forGetter(data -> new ArrayList<>(data.savedStates.values())),
                    VillageEntry.CODEC.listOf().fieldOf("villages").forGetter(data -> new ArrayList<>(data.savedVillages.values())))
            .apply(instance,VillageWarsData::new));

    public static final PersistentStateType<VillageWarsData> TYPE = new PersistentStateType<>("villagewars_data",VillageWarsData::new,CODEC,null);
    public UUID getOwner(VillageKey village){
        return savedOwners.get(village);
    }
    public void putOwner(VillageKey village, UUID stateId){
        savedOwners.put(village,stateId);
        markDirty();
    }
    public StateEntry getState(UUID stateId){
        return savedStates.get(stateId);
    }
    public void putState(UUID stateId, State state){
        Optional<UUID> playerId = state instanceof PlayerState playerState ? Optional.of(playerState.getOwner()) : Optional.empty();
        Optional<Personality> personality = state instanceof AIState aiState ? Optional.of(aiState.getPersonality()) : Optional.empty();
        savedStates.put(stateId,new StateEntry(stateId,state.getName(),state.getEmeralds(),state.getWarList(),state.getStateType(),playerId,personality));
        markDirty();
    }
    public VillageEntry getVillage(VillageKey village){
        return savedVillages.get(village);
    }
    public void putVillage(VillageKey villageKey, Village village){
        savedVillages.put(villageKey,new VillageEntry(villageKey,village.getName(),village.getPosition()));
        markDirty();
    }
    public Collection<StateEntry> getStateEntries(){
        return List.copyOf(savedStates.values());
    }
    public Map<VillageKey,UUID> getOwnerEntries(){
        return Map.copyOf( savedOwners);
    }
    public Collection<VillageEntry> getVillageEntries(){
        return List.copyOf(savedVillages.values());
    }


   




}
