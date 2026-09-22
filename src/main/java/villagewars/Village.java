package villagewars;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.*;

public class Village {
    public static Map<RegistryKey<Biome>,List<String>> nazwyWiosek = new HashMap<>();
    private String name;
    private List<VillagerEntity> villagers = new ArrayList<>();
    private BlockPos pozycja;
    private World world;
    private UUID idGracza;
    static{


        RegistryKey<Biome> desert = BiomeKeys.DESERT;
        RegistryKey<Biome> plains = BiomeKeys.PLAINS;
        RegistryKey<Biome> meadow = BiomeKeys.MEADOW;
        RegistryKey<Biome> taiga = BiomeKeys.TAIGA;
        RegistryKey<Biome> savanna = BiomeKeys.SAVANNA;
        RegistryKey<Biome> snowyPlains = BiomeKeys.SNOWY_PLAINS;

        nazwyWiosek.put(desert, List.of("Sandhollow", "Dune Reach", "Sunscorch", "Goldensand", "Oasis Town", "Driftwell", "Sandkeep", "Mirage Hollow", "Dusty Path", "Searing Vale", "Cactus Hollow", "Sandstorm Keep", "Burnt Hollow", "Scorpion Hollow", "Yellowkeep", "Saharton", "Desert Watch", "Blazing Hollow", "Sunburn Town", "Sandveil"));
        nazwyWiosek.put(plains,List.of("Greenfield", "Sunny Hollow", "Clearwater", "Wheatford", "Brightmeadow", "Quiet Pasture", "Harvest Vale", "Goldenfield", "Springvale", "Daisyford", "Calm Plains", "Golden Hollow", "Buttercup Town", "Sunmeadow", "Poppyfield", "Hayfield", "Windy Vale", "Peaceful Glade", "Sunray Valley", "Barleyton"));
        nazwyWiosek.put(meadow,List.of("Bloomfield", "Mountain Glade", "Butterfly Vale", "Highland Hollow", "Beemeadow", "Alpine Keep", "Colorglade", "Slopehollow", "Sunny Highland", "Peak Meadow", "Heatherfield", "Bloomvale", "Mountain Reach", "Lilachollow", "Highmeadow", "Shepherd's Vale", "Goldhighland", "Meadow Keep", "Fragrant Slope", "Mountain Haven"));
        nazwyWiosek.put(taiga,List.of("Pinehollow", "Sprucewood", "Quiet Taiga", "Wolfden", "Pinevale", "Mistwood", "Bearhollow", "Resinford", "Winterwood", "Coneford", "Frostwood", "Forest Watch", "Firhollow", "Dewglade", "Wolfglade", "Borealton", "Northern Taiga", "Shadewood", "Greenwood Reach", "Forest Keep"));
        nazwyWiosek.put(savanna,List.of("Savannaton", "Acacia Reach", "Goldgrass", "Lionhollow", "Drystep", "Dustvale", "Termite Hollow", "Antelope Reach", "Goldsteppe", "Windsteppe", "Baobab Hollow", "Steppe Keep", "Redearth", "Gazelle Reach", "Tallgrass Town", "Amberveld", "Savanna Watch", "Buffalo Plains", "Sunrise Steppe", "Golden Steppe"));
        nazwyWiosek.put(snowyPlains,List.of("Icekeep", "Snowhollow", "Frostvale", "Whitefield", "Frostford", "Glacierton", "Snowdrift", "Polar Watch", "Snowvale", "Winterkeep", "Crystalhollow", "Everfrost", "Rimehollow", "Snowford", "White Path", "Snowveil", "Frostbite Town", "Flakehollow", "Northkeep", "Tundraton"));



    }
    public Village(BlockPos pozycja, World world){
        this.pozycja = pozycja;
        this.world = world;
        this.villagers = new ArrayList<>();

//        Vec3d pozycjaWek = new Vec3d(pozycja.getX(), pozycja.getY(), pozycja.getZ());
        double radius = 50.0;
        Box box = new Box(pozycja).expand(radius);

        villagers = world.getEntitiesByType(EntityType.VILLAGER,box, EntityPredicates.VALID_ENTITY);

        RegistryEntry<Biome> biomEntry =  this.world.getBiome(this.pozycja);
        RegistryKey <Biome> biomKey = biomEntry.getKey().get();
        List <String> nazwy = nazwyWiosek.get(biomKey);
        if(nazwy != null){
            Random random = new Random();
            int losowaLiczba = random.nextInt(nazwy.size());
            this.name = nazwy.get(losowaLiczba);
        }else{
            this.name = "Nameless Village";
        }

    }
    public String getName(){
        return this.name;
    }
    public List<VillagerEntity> getVillagers(){
        return this.villagers;
    }
    public BlockPos getPosition(){
        return this.pozycja;
    }
    public UUID getOwner(){
        return this.idGracza;
    }
    public void setOwner(UUID idGracza){
        this.idGracza = idGracza;
    }







}