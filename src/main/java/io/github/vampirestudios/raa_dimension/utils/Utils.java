package io.github.vampirestudios.raa_dimension.utils;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Utils {
    //dimension bit flags
    public static final int CORRUPTED =  0b1; //nether corruption, same as the old corruption feature
    public static final int DEAD =       0b10; //No plants or passive animals at all, very harsh
    public static final int ABANDONED =  0b100; //only ruins of old civilizations, no living "smart" creatures (like villagers)
    public static final int LUSH =       0b1000; //A lush overgrowth of plants
    public static final int CIVILIZED =  0b10000; //Villages/towns of "smart" creatures who will trade with you
    public static final int MOLTEN =     0b100000; //Instead of water oceans, there are lava oceans.
    public static final int DRY =        0b1000000; //No oceans exist at all.
    public static final int TECTONIC =   0b10000000; //Creates lots of caves and ravines. Usually not visible on the surface.
    public static final int FROZEN =     0b100000000; //Makes the dimension frozen (snow instead of rain)
    public static final int LUCID =      0b1000000000; //Makes the dimension be lucid, have faster time and more crazy skybox
    public static final int SPACE_LIKE = 0b10000000000; //Changes the gravity of the dimension more

    public static final int POST_APOCALYPTIC = CORRUPTED | DEAD | ABANDONED | DRY | TECTONIC; //A combination of corrupted, dead, abandoned, dry, and tectonic

    //maps
    private static List<String> surfaceBuilders = new ArrayList<>();

    static {
        surfaceBuilders.add("raa_dimensions:patchy_desert");
        surfaceBuilders.add("raa_dimensions:dark_patchy_badlands");
        surfaceBuilders.add("raa_dimensions:patchy_badlands");
        surfaceBuilders.add("raa_dimensions:classic_cliffs");
        surfaceBuilders.add("raa_dimensions:stratified_cliffs");
        surfaceBuilders.add("raa_dimensions:sandy_dunes");
        surfaceBuilders.add("raa_dimensions:dunes");
        surfaceBuilders.add("minecraft:default");
    }

    public static String toTitleCase(String lowerCase) {
        return "" + Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);
    }

    public static String nameToId(String name, Map<String, String> specialCharMap) {
        // strip name of special chars
        for (Map.Entry<String, String> specialChar : specialCharMap.entrySet()) {
            name = name.replace(specialChar.getKey(), specialChar.getValue());
        }
        return name.toLowerCase(Locale.ENGLISH);
    }

    public static ResourceLocation addSuffixToPath(ResourceLocation identifier, String suffix) {
        return new ResourceLocation(identifier.getNamespace(), identifier.getPath() + suffix);
    }

    public static ResourceLocation addPrefixToPath(ResourceLocation identifier, String prefix) {
        return new ResourceLocation(identifier.getNamespace(), prefix + identifier.getPath());
    }

    public static ResourceLocation addPrefixAndSuffixToPath(ResourceLocation identifier, String prefix, String suffix) {
        return new ResourceLocation(identifier.getNamespace(), prefix + identifier.getPath() + suffix);
    }

    /*public static TernarySurfaceConfig randomSurfaceBuilderConfig() {
        if (Rands.chance(6)) return SurfaceBuilder.GRASS_CONFIG;

        Map<String, TernarySurfaceConfig> surfaceBuilders = new HashMap<>();
        surfaceBuilders.put("minecraft:gravel_config", SurfaceBuilder.GRAVEL_CONFIG);
        surfaceBuilders.put("minecraft:grass_config", SurfaceBuilder.GRASS_CONFIG);
        surfaceBuilders.put("minecraft:stone_config", SurfaceBuilder.STONE_CONFIG);
        surfaceBuilders.put("minecraft:coarse_dirt_config", SurfaceBuilder.COARSE_DIRT_CONFIG);
        surfaceBuilders.put("minecraft:sand_config", SurfaceBuilder.SAND_CONFIG);
        surfaceBuilders.put("minecraft:grass_sand_underwater_config", SurfaceBuilder.GRASS_SAND_UNDERWATER_CONFIG);
        surfaceBuilders.put("minecraft:sand_sand_underwater_config", SurfaceBuilder.SAND_SAND_UNDERWATER_CONFIG);
        surfaceBuilders.put("minecraft:badlands_config", SurfaceBuilder.BADLANDS_CONFIG);
        surfaceBuilders.put("minecraft:mycelium_config", SurfaceBuilder.MYCELIUM_CONFIG);
        surfaceBuilders.put("minecraft:nether_config", SurfaceBuilder.NETHER_CONFIG);
        surfaceBuilders.put("minecraft:soul_sand_config", SurfaceBuilder.SOUL_SAND_CONFIG);
        surfaceBuilders.put("minecraft:end_config", SurfaceBuilder.END_CONFIG);
        surfaceBuilders.put("minecraft:crimson_nylium_config", SurfaceBuilder.CRIMSON_NYLIUM_CONFIG);
        surfaceBuilders.put("minecraft:warped_nylium_config", SurfaceBuilder.WARPED_NYLIUM_CONFIG);
        return Rands.map(surfaceBuilders).getValue();
    }

    public static TernarySurfaceConfig fromIdentifierToConfig(ResourceLocation name) {
        if (name.equals(new ResourceLocation("gravel_config"))) return SurfaceBuilder.GRAVEL_CONFIG;
        if (name.equals(new ResourceLocation("grass_config"))) return SurfaceBuilder.GRASS_CONFIG;
        if (name.equals(new ResourceLocation("stone_config"))) return SurfaceBuilder.STONE_CONFIG;
        if (name.equals(new ResourceLocation("coarse_dirt_config"))) return SurfaceBuilder.COARSE_DIRT_CONFIG;
        if (name.equals(new ResourceLocation("sand_config"))) return SurfaceBuilder.SAND_CONFIG;
        if (name.equals(new ResourceLocation("grass_sand_underwater_config"))) return SurfaceBuilder.GRASS_SAND_UNDERWATER_CONFIG;
        if (name.equals(new ResourceLocation("sand_sand_underwater_config"))) return SurfaceBuilder.SAND_SAND_UNDERWATER_CONFIG;
        if (name.equals(new ResourceLocation("badlands_config"))) return SurfaceBuilder.BADLANDS_CONFIG;
        if (name.equals(new ResourceLocation("mycelium_config"))) return SurfaceBuilder.MYCELIUM_CONFIG;
        if (name.equals(new ResourceLocation("nether_config"))) return SurfaceBuilder.NETHER_CONFIG;
        if (name.equals(new ResourceLocation("soul_sand_config"))) return SurfaceBuilder.SOUL_SAND_CONFIG;
        if (name.equals(new ResourceLocation("end_config"))) return SurfaceBuilder.END_CONFIG;

        return SurfaceBuilder.GRASS_CONFIG;
    }

    public static ResourceLocation fromConfigToIdentifier(TernarySurfaceConfig config) {
        if (config.equals(SurfaceBuilder.GRAVEL_CONFIG)) return new ResourceLocation("gravel_config");

        if (config.equals(SurfaceBuilder.GRASS_CONFIG)) return new ResourceLocation("grass_config");
        if (config.equals(SurfaceBuilder.STONE_CONFIG)) return new ResourceLocation("stone_config");
        if (config.equals(SurfaceBuilder.COARSE_DIRT_CONFIG)) return new ResourceLocation("coarse_dirt_config");
        if (config.equals(SurfaceBuilder.SAND_CONFIG)) return new ResourceLocation("sand_config");
        if (config.equals(SurfaceBuilder.GRASS_SAND_UNDERWATER_CONFIG)) return new ResourceLocation("grass_sand_underwater_config");
        if (config.equals(SurfaceBuilder.SAND_SAND_UNDERWATER_CONFIG)) return new ResourceLocation("sand_sand_underwater_config");
        if (config.equals(SurfaceBuilder.BADLANDS_CONFIG)) return new ResourceLocation("badlands_config");
        if (config.equals(SurfaceBuilder.MYCELIUM_CONFIG)) return new ResourceLocation("mycelium_config");
        if (config.equals(SurfaceBuilder.NETHER_CONFIG)) return new ResourceLocation("nether_config");
        if (config.equals(SurfaceBuilder.SOUL_SAND_CONFIG)) return new ResourceLocation("soul_sand_config");
        if (config.equals(SurfaceBuilder.END_CONFIG)) return new ResourceLocation("end_config");
        if (config.equals(SurfaceBuilder.CRIMSON_NYLIUM_CONFIG)) return new ResourceLocation("crimson_nylium_config");
        if (config.equals(SurfaceBuilder.WARPED_NYLIUM_CONFIG)) return new ResourceLocation("warped_nylium_config");

        return new ResourceLocation("grass_config");
    }

    public static SurfaceSystem<?> newRandomSurfaceBuilder() {
        //random surface builder
        if (Rands.chance(2)) {
            return SurfaceBuilderGenerator.RANDOM_SURFACE_BUILDER.getRandom(Rands.getRandom());
        }

        //choose the default type of surface builder
        SurfaceSystem<?> sb = Registry.SURFACE_BUILDER.get(new ResourceLocation(Rands.list(surfaceBuilders)));
        if (sb == null) { //dunno why it's null sometimes, but if it is, default to the default
            sb = SurfaceBuilder.DEFAULT; //TODO: fix this instead of a hack
        }
        return sb;
    }*/

    public static boolean checkBitFlag(int toCheck, int flag) {
        return (toCheck & flag) == flag;
    }

    public static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static String generateCivsName() throws IOException {
        String civilizationName;
        Random rand = new Random();
        ResourceLocation surnames = new ResourceLocation(RAADimensionAddon.MOD_ID, "names/civilizations.txt");
        InputStream stream = Minecraft.getInstance().getResourceManager().open(surnames);
        Scanner scanner = new Scanner(Objects.requireNonNull(stream));
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
            builder.append(",");
        }
        String[] strings = builder.toString().split(",");
        civilizationName = strings[rand.nextInt(strings.length)];
        stream.close();
        scanner.close();
        return civilizationName;
    }

    public static void createSpawnsFile(String name, ServerLevelAccessor world, BlockPos pos) {
        /*try {
            String path;
            World world2 = world.getWorld();
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
                path = "saves/" + ((ServerWorld) world2).getSaveHandler().getWorldDir().getName() + "/DIM_raa_" + world.getDimension().getType().getSuffix().substring(4) + "/data/" + name + "_spawns.txt";
            else
                path = world.getLevelProperties().getLevelName() + "/DIM_raa_" + world.getDimension().getType().getSuffix().substring(4) + "/data/" + name + "_spawns.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.append(pos.getX() + "," + pos.getY() + "," + pos.getZ() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
