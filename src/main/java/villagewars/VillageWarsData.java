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
    private final Map<TerritoryKey,TerritoryEntry> savedTerritories = new HashMap<>();
    private final Set<TerritoryKey> checkedCandidates = new HashSet<>();




    private VillageWarsData(){}

    private VillageWarsData(List<OwnerEntry> ownersSaved, List<StateEntry> statesSaved, List<VillageEntry> villageSaved, List<TerritoryEntry> territoriesSaved, List<TerritoryKey> checkedCandidates1) {
        for (OwnerEntry entry : ownersSaved) {
            savedOwners.put(entry.village(),entry.stateId());

        }
        for(StateEntry entry : statesSaved){
            savedStates.put(entry.id(),entry);
        }
        for(VillageEntry entry : villageSaved){
            savedVillages.put(entry.village(),entry);
        }
        for(TerritoryEntry entry: territoriesSaved){
                savedTerritories.put(entry.territoryKey(),entry);


        }
        checkedCandidates.addAll(checkedCandidates1);

    }


    private static final Codec<VillageWarsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            OwnerEntry.CODEC.listOf().fieldOf("owners").forGetter(data -> data.savedOwners.entrySet().stream().map(entry -> new OwnerEntry(entry.getKey(),entry.getValue())).toList()),
            StateEntry.CODEC.listOf().fieldOf("states").forGetter(data -> new ArrayList<>(data.savedStates.values())),
                    VillageEntry.CODEC.listOf().fieldOf("villages").forGetter(data -> new ArrayList<>(data.savedVillages.values())),
                    TerritoryEntry.CODEC.listOf().fieldOf("territories").forGetter(data -> new ArrayList<>(data.savedTerritories.values())),
                    TerritoryKey.CODEC.listOf().fieldOf("Checked territories").forGetter(data -> new ArrayList<>(data.checkedCandidates))

            )

            .apply(instance,VillageWarsData::new));

    public static final PersistentStateType<VillageWarsData> TYPE = new PersistentStateType<>("villagewars_data",VillageWarsData::new,CODEC,null);
    public UUID getOwner(VillageKey village){
        return savedOwners.get(village);
    }
    public void putOwner(VillageKey village, UUID stateId){
        savedOwners.put(village,stateId);
        markDirty();
    }
    public boolean isCandidateChecked(TerritoryKey territoryKey){
        return checkedCandidates.contains(territoryKey);
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
        savedVillages.put(villageKey,new VillageEntry(villageKey,village.getName(),village.getPosition(),village.getResidentId()));
        markDirty();
    }
    public void putTerritory (TerritoryKey territoryKey,TerritoryAssignment territoryAssignment ){
        savedTerritories.put(territoryKey,new TerritoryEntry(territoryAssignment.villageKey(),territoryKey,territoryAssignment.status()));
        markDirty();
    }
    public void removeTerritory(TerritoryKey key) {
        if (savedTerritories.remove(key) != null) {
            markDirty();
        }
    }
    public void putCheckedCandidates(TerritoryKey territoryKey){
        checkedCandidates.add(territoryKey);
    }
    public void removeCheckedCandidate(TerritoryKey key) {
        if (checkedCandidates.remove(key)) {
            markDirty();
        }
    }
    public TerritoryEntry getTerritory(TerritoryKey key){
        return savedTerritories.get(key);
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
    public Collection<TerritoryEntry> getTerritoryEntries(){
        return List.copyOf(savedTerritories.values());
    }
    public Set<TerritoryKey> getCheckedCandidates(){
        return checkedCandidates;
    }


   




}
