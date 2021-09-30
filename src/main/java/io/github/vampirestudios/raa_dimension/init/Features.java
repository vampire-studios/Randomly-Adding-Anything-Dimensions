package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.carvers.BigRoomCarver;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.CarverType;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionBiomeData;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.generation.feature.*;
import io.github.vampirestudios.raa_dimension.generation.feature.FossilFeature;
import io.github.vampirestudios.raa_dimension.generation.feature.config.ColumnBlocksConfig;
import io.github.vampirestudios.raa_dimension.generation.feature.config.CorruptedFeatureConfig;
import io.github.vampirestudios.raa_dimension.generation.feature.portalHub.PortalHubFeature;
import io.github.vampirestudios.raa_dimension.generation.feature.portalHub.PortalShrineFeature;
import io.github.vampirestudios.raa_dimension.generation.feature.portalHub.PortalShrineFeatureConfig;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.*;

public class Features {
    public static NetherrackFeature CORRUPTED_NETHRRACK;
    public static CraterFeature CRATER_FEATURE;
    public static OutpostFeature OUTPOST;
    public static CampfireFeature CAMPFIRE;
//    public static SmallSkeletalTreeFeature SMALL_SKELETON_TREE;
//    public static LargeSkeletalTreeFeature LARGE_SKELETON_TREE;
    public static SpiderLairFeature SPIDER_LAIR;
//    public static FixedTreeFeature FIXED_TREE;
//    public static BentTreeFeature BENT_TREE;
//    public static DoubleTreeFeature DOUBLE_TREE;
    public static TowerFeature TOWER;
    public static FossilFeature FOSSIL;
    public static PortalHubFeature PORTAL_HUB;
    public static PortalShrineFeature PORTAL_SHRINE;
    public static ShrineFeature SHRINE;

    public static BeeNestFeature BEE_NEST;
    public static CaveCampfireFeature CAVE_CAMPFIRE;
    public static MushRuinFeature MUSHROOM_RUIN;
    public static UndegroundBeeHiveFeature UNDERGROUND_BEE_HIVE;
    public static StonehengeFeature STONE_HENGE;
    public static ColumnRampFeature COLUMN_RAMP;
    public static ColumnVerticalFeature COLUMN_VERTICAL;
    public static HangingRuinsFeature HANGING_RUINS;
//    public static AbovegroundStorageFeature ABOVE_GROUND_STORAGE;
//    public static QuarryFeature QUARRY;

    public static StructureFeature<DefaultFeatureConfig> DUNGEON_FEATURE;
    public static StructureFeature<DefaultFeatureConfig> DUNGEON_STRUCTURE_FEATURE;
    public static StructurePieceType DUNGEON_PIECE;

    public static StructureFeature<DefaultFeatureConfig> LABYRINT_FEATURE;
    public static StructureFeature<DefaultFeatureConfig> LABYRINT_STRUCTURE_FEATURE;
    public static StructurePieceType LABYRINT_PIECE;

    public static void init() {
//        CommandRegistry.INSTANCE.register(false, CommandLocateRAAStructure::register);

        CORRUPTED_NETHRRACK = (NetherrackFeature) register("corrupted_netherrack", new NetherrackFeature(DefaultFeatureConfig.CODEC));
        CRATER_FEATURE = (CraterFeature) register("crater_feature", new CraterFeature(CorruptedFeatureConfig.CODEC));
        OUTPOST = (OutpostFeature) register("outpost", new OutpostFeature(DefaultFeatureConfig.CODEC));
        CAMPFIRE = (CampfireFeature) register("campfire", new CampfireFeature(DefaultFeatureConfig.CODEC));
        FOSSIL = (FossilFeature) register("fossil", new FossilFeature(DefaultFeatureConfig.CODEC));
        SHRINE = (ShrineFeature) register("shrine", new ShrineFeature(DefaultFeatureConfig.CODEC));
//        SMALL_SKELETON_TREE = register("skeleton_tree_small", new SmallSkeletalTreeFeature(TreeFeatureConfig.CODEC));
//        LARGE_SKELETON_TREE = register("skeleton_tree_large", new LargeSkeletalTreeFeature(TreeFeatureConfig.CODEC));
        SPIDER_LAIR = (SpiderLairFeature) register("spider_lair", new SpiderLairFeature(DefaultFeatureConfig.CODEC));
//        FIXED_TREE = register("fixed_tree", new FixedTreeFeature(BranchedTreeFeatureConfig.CODEC))
//        BENT_TREE = register("bent_tree", new BentTreeFeature(BranchedTreeFeatureConfig.CODEC));
//        DOUBLE_TREE = register("double_tree", new DoubleTreeFeature(BranchedTreeFeatureConfig.CODEC));
        PORTAL_HUB = (PortalHubFeature) register("portal_hub", new PortalHubFeature(DefaultFeatureConfig.CODEC));
        PORTAL_SHRINE = (PortalShrineFeature) register("portal_shrine", new PortalShrineFeature(PortalShrineFeatureConfig.CODEC));
//        ABOVE_GROUND_STORAGE = register("aboveground_storage", new AbovegroundStorageFeature(DefaultFeatureConfig.CODEC));
//        QUARRY = register("quarry", new QuarryFeature(DefaultFeatureConfig.CODEC));

        BEE_NEST = (BeeNestFeature) register("bee_nest", new BeeNestFeature(DefaultFeatureConfig.CODEC));
        CAVE_CAMPFIRE = (CaveCampfireFeature) register("cave_campfire", new CaveCampfireFeature(DefaultFeatureConfig.CODEC));
        MUSHROOM_RUIN = (MushRuinFeature) register("mushroom_ruin", new MushRuinFeature(DefaultFeatureConfig.CODEC));
        UNDERGROUND_BEE_HIVE = (UndegroundBeeHiveFeature) register("underground_bee_hive", new UndegroundBeeHiveFeature(DefaultFeatureConfig.CODEC));

        STONE_HENGE = (StonehengeFeature) register("stone_henge", new StonehengeFeature(DefaultFeatureConfig.CODEC));
        COLUMN_RAMP = (ColumnRampFeature) register("columnn_ramp", new ColumnRampFeature(ColumnBlocksConfig.CODEC));
        COLUMN_VERTICAL = (ColumnVerticalFeature) register("columnn_vertical", new ColumnVerticalFeature(ColumnBlocksConfig.CODEC));
        HANGING_RUINS = (HangingRuinsFeature) register("hanging_ruins", new HangingRuinsFeature(DefaultFeatureConfig.CODEC));

        /*DUNGEON_FEATURE = Registry.register(
                Registry.FEATURE,
                new Identifier(RAADimensionAddon.MOD_ID, "dungeon_feature"),
                new RandomDungeonFeature(DefaultFeatureConfig.CODEC)
        );
        DUNGEON_STRUCTURE_FEATURE = Registry.register(
                Registry.STRUCTURE_FEATURE,
                new Identifier(RAADimensionAddon.MOD_ID, "dungeon_structure_feature"),
                new RandomDungeonFeature(DefaultFeatureConfig.CODEC)
        );
        DUNGEON_PIECE = Registry.register(
                Registry.STRUCTURE_PIECE,
                new Identifier(RAADimensionAddon.MOD_ID, "dungeon_piece"),
                RandomDungeonPiece::new
        );
        Feature.STRUCTURES.put("dungeon", DUNGEON_FEATURE);

        LABYRINT_FEATURE = Registry.register(
                Registry.FEATURE,
                new Identifier(RAADimensionAddon.MOD_ID, "labyrint_feature"),
                new LabyrintFeature(DefaultFeatureConfig.CODEC)
        );
        LABYRINT_STRUCTURE_FEATURE = Registry.register(
                Registry.STRUCTURE_FEATURE,
                new Identifier(RAADimensionAddon.MOD_ID, "labyrint_structure_feature"),
                new LabyrintFeature(DefaultFeatureConfig.CODEC)
        );
        LABYRINT_PIECE = Registry.register(
                Registry.STRUCTURE_PIECE,
                new Identifier(RAADimensionAddon.MOD_ID, "labyrint_piece"),
                LabyrintPiece::new
        );
        Feature.STRUCTURES.put("labyrint", LABYRINT_FEATURE);*/
    }

    // we use this cursed code to make a new carver per dimension
    public static void addDefaultCarvers(GenerationSettings.Builder generationSettings, DimensionData dimensionData, DimensionBiomeData biomeData) {
        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.TECTONIC)) {
//            CaveCarver caveCarver = registerCarver("cave_carver_" + dimensionData.getId().getPath(), new CaveCarver(dimensionData));
//            biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(caveCarver, new ProbabilityConfig(1)));

//            RavineCarver ravineCarver = registerCarver("ravine_carver_" + dimensionData.getId().getPath(), new RavineCarver(dimensionData));
//            biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(ravineCarver, new ProbabilityConfig(1)));

//            CaveCavityCarver caveCavityCarver = registerCarver("cave_cavity_carver", new CaveCavityCarver(dimensionData));
//            biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(caveCavityCarver, new ProbabilityConfig(1)));
//
//            StackedBubbleRoomsCarver bubbleRoomsCarver = registerCarver("stacked_bubble_rooms_carver", new StackedBubbleRoomsCarver(dimensionData));
//            biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(bubbleRoomsCarver, new ProbabilityConfig(1)));
        } else {
            //TODO: bring this to a dedicated class
            if (biomeData.getCarvers().contains(CarverType.CAVES)) { //80% chance of normal caves
//                CaveCarver caveCarver = registerCarver("cave_carver_" + dimensionData.getId().getPath(), new CaveCarver(CaveCarverConfig.CAVE_CODEC));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(caveCarver, new ProbabilityConfig(0.14285715F)));
//                generationSettings.carver(GenerationStep.Carver.AIR, caveCarver.configure(new CaveCarverConfig()));
                generationSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.PROTOTYPE_CAVE);
            }

            if (biomeData.getCarvers().contains(CarverType.CREVICES)) { //10% chance of normal caves
//                CaveCarver caveCarver = registerCarver("cave_carver_" + dimensionData.getId().getPath(), new CaveCarver(CaveCarverConfig.CAVE_CODEC));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(caveCarver, new ProbabilityConfig(0.14285715F)));
//                generationSettings.carver(GenerationStep.Carver.AIR, caveCarver.configure(new CaveCarverConfig()));
                generationSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.PROTOTYPE_CREVICE);
            }

            if (biomeData.getCarvers().contains(CarverType.RAVINES)) { //75% chance of normal ravines
//                RavineCarver ravineCarver = registerCarver("ravine_carver_" + dimensionData.getId().getPath(), new RavineCarver(RavineCarverConfig.RAVINE_CODEC));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(ravineCarver, new ProbabilityConfig(0.02F)));
//                generationSettings.carver(GenerationStep.Carver.AIR, ravineCarver.configure(new RavineCarverConfig()));
                generationSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.PROTOTYPE_CANYON);
            }

            if (biomeData.getCarvers().contains(CarverType.CAVE_CAVITY)) { //10% chance of cave cavity
//                CaveCavityCarver caveCavityCarver = registerCarver("cave_cavity_carver_" + dimensionData.getId().getPath(), new CaveCavityCarver(dimensionData));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(caveCavityCarver, new ProbabilityConfig(0.03F)));
            }

            if (biomeData.getCarvers().contains(CarverType.TEARDROPS)) { //33% chance of teardrops
//                TeardropCarver teardropCarver = registerCarver("teardrop_carver_" + dimensionData.getId().getPath(), new TeardropCarver(dimensionData));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(teardropCarver, new ProbabilityConfig(0.06F)));
            }

            if (biomeData.getCarvers().contains(CarverType.VERTICAL)) { //25% chance of vertical caves
//                VerticalCarver verticalCarver = registerCarver("vertical_carver_" + dimensionData.getId().getPath(), new VerticalCarver(dimensionData));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(verticalCarver, new ProbabilityConfig(0.04F)));
            }

           if (biomeData.getCarvers().contains(CarverType.BIG_ROOM)) { //10% chance of big rooms
                BigRoomCarver bigRoomCarver = registerCarver("big_room_carver_" + dimensionData.getId().getPath(), new BigRoomCarver(dimensionData));
//                biome.addCarver(GenerationStep.Carver.AIR, Biome.configureCarver(bigRoomCarver, new ProbabilityConfig(0.04F)));
//               generationSettings.carver(GenerationStep.Carver.AIR, bigRoomCarver.configure(new CaveCarverConfig()));
           }
        }
    }

    public static <T extends Feature<?>> T register(String name, T feature) {
        if (!Registry.FEATURE.containsId(new Identifier(RAADimensionAddon.MOD_ID, name))) {
            return Registry.register(Registry.FEATURE, new Identifier(RAADimensionAddon.MOD_ID, name), feature);
        } else {
            return (T) Registry.FEATURE.get(new Identifier(RAADimensionAddon.MOD_ID, name));
        }
    }

    public static ConfiguredFeature<?, ?> register(String name, ConfiguredFeature<?, ?> feature) {
        if (!BuiltinRegistries.CONFIGURED_FEATURE.containsId(new Identifier(RAADimensionAddon.MOD_ID, name))) {
            return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(RAADimensionAddon.MOD_ID, name), feature);
        } else {
            return BuiltinRegistries.CONFIGURED_FEATURE.get(new Identifier(RAADimensionAddon.MOD_ID, name));
        }
    }

    public static <F extends StructureFeature<?>> F registerStructure(String name, F structureFeature) {
        if (Registry.STRUCTURE_FEATURE.get(new Identifier(RAADimensionAddon.MOD_ID, name)) == null) {
            return Registry.register(Registry.STRUCTURE_FEATURE, new Identifier(RAADimensionAddon.MOD_ID, name), structureFeature);
        } else {
            return structureFeature;
        }
    }

    public static <F extends StructurePieceType> F registerStructurePiece(String name, F structurePieceType) {
        if (Registry.STRUCTURE_PIECE.get(new Identifier(RAADimensionAddon.MOD_ID, name)) == null) {
            return Registry.register(Registry.STRUCTURE_PIECE, new Identifier(RAADimensionAddon.MOD_ID, name), structurePieceType);
        } else {
            return structurePieceType;
        }
    }

    public static <F extends CarverConfig, C extends Carver<F>> C registerCarver(String name, C carver) {
        name = name.toLowerCase();
        if (Registry.CARVER.get(new Identifier(RAADimensionAddon.MOD_ID, name)) == null) {
            return Registry.register(Registry.CARVER, new Identifier(RAADimensionAddon.MOD_ID, name), carver);
        } else {
            return carver;
        }
    }

}
