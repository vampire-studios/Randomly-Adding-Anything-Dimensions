package io.github.vampirestudios.raa_dimension.generation.feature.portalHub;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import io.github.vampirestudios.raa_dimension.utils.JsonConverter;
import io.github.vampirestudios.raa_dimension.utils.WorldStructureManipulation;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PortalShrineFeature extends Feature<PortalShrineFeatureConfig> {
    private final JsonConverter converter = new JsonConverter();
    private Map<String, JsonConverter.StructureValues> structures;

    public PortalShrineFeature(Codec<PortalShrineFeatureConfig> codec) {
        super(codec);
    }

    public static void placePiece(ServerLevelAccessor world, BlockPos pos, JsonConverter.StructureValues piece, int decay) {
        int themeNum = Rands.randInt(PortalHubThemes.PORTAL_HUB_THEMES.keySet().size());
        PortalHubTheme theme = PortalHubThemes.PORTAL_HUB_THEMES.byId(themeNum);
        assert theme != null;
        for (int i = 0; i < piece.getBlockPositions().size(); i++) {
            Vec3i currBlockPos = piece.getBlockPositions().get(i);
            String currBlockType = piece.getBlockTypes().get(piece.getBlockStates().get(i));
            Map<String, String> currBlockProp = piece.getBlockProperties().get(piece.getBlockStates().get(i));

            if (decay <= 0 || !Rands.chance(14 - decay)) {
                switch (currBlockType) {
                    case "minecraft:stone_bricks":
                        WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), Registry.BLOCK.getKey(theme.getBlock()).toString(), new HashMap<>(), 0);
                        break;
                    case "minecraft:stone_brick_slab":
                        WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), Registry.BLOCK.getKey(theme.getSlab()).toString(), currBlockProp, 0);
                        break;
                    case "minecraft:stone_brick_stairs":
                        WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), Registry.BLOCK.getKey(theme.getStairs()).toString(), currBlockProp, 0);
                        break;
                    case "minecraft:stone_brick_wall":
                        if (themeNum < 14) {
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), Registry.BLOCK.getKey(theme.getWall()).toString(), new HashMap<>(), 0);
                        } else if (themeNum < 16) {
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), Registry.BLOCK.getKey(theme.getWall()).toString(), new HashMap<>(), 0);
                        } else {
                            currBlockProp.remove("up");
                            WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), Registry.BLOCK.getKey(theme.getWall()).toString(), new HashMap<>(), 0);
                        }
                        break;
                    case "minecraft:orange_wool":
                        List<DimensionData> dimensionDataList = new ArrayList<>();
                        Dimensions.DIMENSIONS.forEach(dimensionDataList::add);
                        WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), "raa_dimensions:" + Rands.list(dimensionDataList).getName().toLowerCase() + "_portal", currBlockProp, 0);
                        break;
                    default:
                        WorldStructureManipulation.placeBlock(world, pos.offset(currBlockPos), currBlockType, currBlockProp, 0);
                        break;
                }
            }
        }
    }


    @Override
    public boolean place(FeaturePlaceContext<PortalShrineFeatureConfig> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        JsonObject jsonObject = null;
        PortalShrineFeatureConfig config = context.config();
        try {
            Resource path = Objects.requireNonNull(world.getServer()).getResourceManager().getResourceOrThrow(new ResourceLocation(RAADimensionAddon.MOD_ID, String.format("structures/portal_shrine/%sp-shrine-%s.json", config.getAmount(), config.getVariant())));
            jsonObject = new Gson().fromJson(new InputStreamReader(path.open()), JsonObject.class);
            JsonObject finalJsonObject = jsonObject;
            structures = new HashMap<>() {{
                put(String.format("structures/portal_shrine/%sp-shrine-%s.json", config.getAmount(), config.getVariant()), converter.loadStructure(finalJsonObject));
            }};
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            System.out.println("Can't get the file");
            return true;
        }

        //Cheeky way of limiting these structures to the overworld
        if (!world.dimensionType().equals(BuiltinDimensionTypes.OVERWORLD)) {
            return true;
        }

        //Check if structure can generate in the area
        Vec3i tempPos = WorldStructureManipulation.circularSpawnCheck(world, pos, structures.get(String.format("structures/portal_shrine/%sp-shrine-%s.json", config.getAmount(), config.getVariant())).getSize(), 0.125f);
        if (tempPos.compareTo(Vec3i.ZERO) == 0) {
            return true;
        }
        pos = new BlockPos(tempPos);

        //Generate portal
        placePiece(world, pos, structures.get(String.format("structures/portal_shrine/%sp-shrine-%s.json", config.getAmount(), config.getVariant())), 0);

        //Record spawn in text file
        /*try {
            String path;
            World world2 = world.getWorld();
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
                path = "saves/" + ((ServerWorld) world2).getSaveHandler().getWorldDir().getName() + "/data/portal_hub_spawns.txt";
            else path = world.getLevelProperties().getLevelName() + "/data/portal_hub_spawns.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.append(String.valueOf(pos.getX())).append(",").append(String.valueOf(pos.getY())).append(",").append(String.valueOf(pos.getZ())).append("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return true;
    }
}