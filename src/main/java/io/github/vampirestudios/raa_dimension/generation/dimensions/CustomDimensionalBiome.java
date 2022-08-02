package io.github.vampirestudios.raa_dimension.generation.dimensions;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionBiomeData;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionTreeData;
import io.github.vampirestudios.raa_dimension.init.Features;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;

public class CustomDimensionalBiome {

    public static void addFeatures(DimensionData dimensionData, DimensionBiomeData biomeData, BiomeGenerationSettings.Builder generationSettings, MobSpawnSettings.Builder spawnSettings) {
        /*if (Registry.SURFACE_BUILDER.get(biomeData.getSurfaceBuilder()) == SurfaceBuilders.HYPER_FLAT) {
            DefaultBiomeFeatures.addSeagrassOnStone(this);
            DefaultBiomeFeatures.addKelp(this);
        }*/
//        generationSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
//                .feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.SPRING_LAVA)
//                .structureFeature(ConfiguredStructureFeatures.FORTRESS);
//        generationSettings.surfaceBuilder(RegistryUtils.registerConfiguredSurfaceBuilder(dimensionData.getId().getPath() + "_surface_builder", new ConfiguredSurfaceBuilder<>((SurfaceSystem<TernarySurfaceConfig>) Registry.SURFACE_BUILDER.get(biomeData.getSurfaceBuilder()), biomeData.getSurfaceConfig())));

        if (Rands.chance(4) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.DRY) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.MOLTEN)) {
            BiomeDefaultFeatures.addPlainGrass(generationSettings);
        }

        /*if (!Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.CORRUPTED)) {
            for (DimensionTreeData treeData : biomeData.getTreeData()) {
                if (treeData.getTreeType() == DimensionTreeTypes.MEGA_JUNGLE || treeData.getTreeType() == DimensionTreeTypes.MEGA_SPRUCE || treeData.getTreeType() == DimensionTreeTypes.DARK_OAK) {
                    TreeFeatureConfig config = (new TreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(treeData.getWoodType().woodType.getLog().getDefaultState()),
                            new SimpleBlockStateProvider(treeData.getWoodType().woodType.getLeaves().getDefaultState()),
                            getFoliagePlacer(treeData),
                            new DarkOakTrunkPlacer(6, 2, 1),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                    )).maxWaterDepth(2147483647).heightmap(Heightmap.Type.MOTION_BLOCKING).ignoreVines().build();
                    generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                            getMegaTree(treeData.getTreeType())
                                    .configure(
                                            config
                                    ).decorate(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraDecoratorConfig(0, treeData.getChance(), 1))));
                    ConfiguredFeatures.DARK_OAK
                } else {
                    BranchedTreeFeatureConfig config1 = getTreeConfig(treeData);
                    generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                            getNormalTree(treeData.getTreeType())
                                    .configure(
                                            config1
                                    ).decorate(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraDecoratorConfig(0, treeData.getChance(), 1))));
                }
            }
        }*/

        /*if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) || Utils.checkBitFlag(dimensionData.getFlags(), Utils.DRY)) {
//            generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, Features.LARGE_SKELETON_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG).decorate(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraDecoratorConfig(0, biomeData.getLargeSkeletonTreeChance(), 1))));
//            generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, Feature.FOSSIL.configure(new FossilFeatureConfig(FOSSIL_STRUCTURES, FOSSIL_OVERLAY_STRUCTURES, StructureProcessorLists.FOSSIL_ROT, StructureProcessorLists.FOSSIL_COAL, 4))).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(64))));
        }*/

        //TODO
        /*if (!Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.CORRUPTED)) {
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_sunflower_patch_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_PATCH
                    .configure(new RandomPatchConfiguration.Builder(new SimpleStateProvider(Blocks.SUNFLOWER.defaultBlockState()), new DoublePlantPlacer()).build())
                    .decorate(Decorator.COUNT.configure(new CountConfiguration(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 50 : 20)))));
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_rose_bush_patch_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_PATCH
                    .configure(new RandomPatchConfiguration.Builder(new SimpleStateProvider(Blocks.ROSE_BUSH.defaultBlockState()), new DoublePlantPlacer()).build())
                    .decorate(Decorator.COUNT.configure(new CountConfiguration(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 20 : 5)))));
            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_lilac_patch_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_PATCH
                    .configure(new RandomPatchConfiguration.Builder(new SimpleStateProvider(Blocks.LILAC.defaultBlockState()), new DoublePlantPlacer()).build())
                    .decorate(Decorator.COUNT.configure(new CountConfiguration(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 20 : 5)))));

            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_sugar_cane_patch_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_PATCH
                    .configure(new RandomPatchConfiguration.Builder(new SimpleStateProvider(Blocks.SUGAR_CANE.defaultBlockState()), new ColumnPlacer(BiasedToBottomInt.of(2, 4)))
                            .tries(20).spreadX(4).spreadY(0).spreadZ(4).cannotProject().needsWater().build())
                    .decorate(Decorator.COUNT.configure(new CountConfiguration(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 20 : 5)))));

            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_pumpkin_patch_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_PATCH
                    .configure(new RandomPatchConfiguration.Builder(new SimpleStateProvider(Blocks.PUMPKIN.defaultBlockState()), SimpleBlockPlacer.INSTANCE).build())
                    .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 50 : 20)))));
        }*/

//        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.CORRUPTED)) {
//            Holder<ConfiguredFeature<?, ?>> craterCf = register(String.format("%s_corrupted_crater_cf", dimensionData.getName().toLowerCase()), new ConfiguredFeature<>(Features.CRATER_FEATURE, new CorruptedFeatureConfig(true)));
//            Holder<ConfiguredFeature<?, ?>> netherrackCf = register(String.format("%s_corrupted_netherrack_cf", dimensionData.getName().toLowerCase()), new ConfiguredFeature<>(Features.CORRUPTED_NETHRRACK, new NoneFeatureConfiguration()));
//            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_corrupted_crater_pf", dimensionData.getName().toLowerCase()), new PlacedFeature(craterCf, List.of(PlacementUtils.countExtra(0, biomeData.getCorruptedCratersChance(), 1), BiomeFilter.biome(), InSquarePlacement.spread()))));
//            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_corrupted_netherrack_pf", dimensionData.getName().toLowerCase()), new PlacedFeature(netherrackCf, List.of(CountOnEveryLayerPlacement.of(2), BiomeFilter.biome(), InSquarePlacement.spread()))));
//        } else {
//            if (biomeData.spawnsCratersInNonCorrupted()) {
//                Holder<ConfiguredFeature<?, ?>> craterCf = register(String.format("%s_crater_cf", dimensionData.getName().toLowerCase()), new ConfiguredFeature<>(Features.CRATER_FEATURE, new CorruptedFeatureConfig(false)));
//                generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, register(String.format("%s_crater_pf", dimensionData.getName().toLowerCase()), new PlacedFeature(craterCf, List.of(PlacementUtils.countExtra(0, biomeData.getNonCorruptedCratersChance(), 1), BiomeFilter.biome(), InSquarePlacement.spread()))));
//            }
//        }

        float campfireChance = biomeData.getCampfireChance();
        float outpostChance = biomeData.getOutpostChance();
        float towerChance = biomeData.getTowerChance();
        float fossilChance = 0;
        float shrineChance = 0.002F;

        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.ABANDONED)) {
            outpostChance = Rands.randFloatRange(0.002F, 0.003F);
            towerChance = Rands.randFloatRange(0.002F, 0.00225F);
        }
        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD)) {
            campfireChance = 0;
            fossilChance = Rands.randFloatRange(0.007F, 0.0075F);
        }
        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED)) {
            campfireChance = Rands.randFloatRange(0.005F, 0.007F);
            outpostChance = Rands.randFloatRange(0.002F, 0.008F);
            towerChance = Rands.randFloatRange(0.002F, 0.003F);
        }

        /*if (dimensionData.getDimensionChunkGenerator().equals(DimensionChunkGenerators.CAVES)) {
            generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.BASALT_PILLAR.configure(FeatureConfig.DEFAULT)
                    .decorate(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 256))));
        }*/

        // TODO fix this
//        generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, register(String.format("%s_outpost_cf", dimensionData.getName().toLowerCase()), Features.OUTPOST.configure(new NoneFeatureConfiguration()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, outpostChance, 1)))));
//        generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, register(String.format("%s_campfire_cf", dimensionData.getName().toLowerCase()), Features.CAMPFIRE.configure(new NoneFeatureConfiguration()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, campfireChance, 1)))));

        //TODO
//        TowerFeature TOWER = register(String.format("%s_tower", dimensionData.getName().toLowerCase()), new TowerFeature(dimensionData, DefaultFeatureConfig.CODEC));
//        generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, register(String.format("%s_tower_cf", dimensionData.getName().toLowerCase()), TOWER.configure(new DefaultFeatureConfig()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, towerChance, 1)))));

//        generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, register(String.format("%s_fossil_cf", dimensionData.getName().toLowerCase()), Features.FOSSIL.configure(new NoneFeatureConfiguration()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, fossilChance, 1)))));

        //TODO
//        generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, register(String.format("%s_shrine_cf", dimensionData.getName().toLowerCase()), Features.SHRINE.configure(new DefaultFeatureConfig()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, shrineChance, 1)))));

        /*generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.register(String.format("%s_stone_henge_cf", dimensionData.getName().toLowerCase()), Features.STONE_HENGE.configure(new DefaultFeatureConfig()).decorate(
                        Decorator.COUNT_EXTRA.configure(
                                new CountExtraDecoratorConfig(0, 0.001F, 1)
                        )
                ))
        );*/

        //TODO
        /*generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES,
                register(String.format("%s_something2_cf", dimensionData.getName().toLowerCase()), Features.COLUMN_RAMP.configure(new ColumnBlocksConfig(Blocks.STONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState(),
                        Blocks.NETHERRACK.defaultBlockState())).decorate(
                        Decorator.RANGE.configure(
                                new RangeDecoratorConfig(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(220)))
                        )
                ))
        );
        generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES,
                register(String.format("%s_something_cf", dimensionData.getName().toLowerCase()), Features.COLUMN_VERTICAL.configure(new ColumnBlocksConfig(Blocks.STONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState(),
                        Blocks.NETHERRACK.defaultBlockState())).decorate(
                        Decorator.RANGE.configure(
                                new RangeDecoratorConfig(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(220)))
                        )
                ))
        );*/

        /*generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.HANGING_RUINS.configure(FeatureConfig.DEFAULT).decorate(
                        RAAPlacements.LEDGE_UNDERSIDE_MINI_FEATURE.configure(
                                new ChanceAndTypeConfig(0.1F, ChanceAndTypeConfig.Type.HANGING_RUINS)
                        )
                )
        );*/
        /*generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.HANGING_RUINS.configure(new DefaultFeatureConfig()).decorate(
                        Decorator.COUNT_EXTRA.configure(
                                new CountExtraDecoratorConfig(0, 0.001F, 1)
                        )
                )
        );*/

//        generationSettings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, register(String.format("%s_bee_nest_cf", dimensionData.getName().toLowerCase()), Features.BEE_NEST.configure(new NoneFeatureConfiguration()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 1.0f, 1)))));
//        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, register(String.format("%s_cave_campfire_cf", dimensionData.getName().toLowerCase()), Features.CAVE_CAMPFIRE.configure(new NoneFeatureConfiguration()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 1.0f, 1)))));
        //TODO
//        generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, register(String.format("%s_mushroom_ruin_cf", dimensionData.getName().toLowerCase()), Features.MUSHROOM_RUIN.configure(new DefaultFeatureConfig()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 1.0f, 1)))));
//        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, register(String.format("%s_underground_bee_hive_cf", dimensionData.getName().toLowerCase()), Features.UNDERGROUND_BEE_HIVE.configure(new NoneFeatureConfiguration()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 1.0f, 1)))));



        //TODO
        /*if ((Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED)) || (Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && Utils.checkBitFlag(dimensionData.getFlags(), Utils.ABANDONED))) {
            StoneCircleFeature STONE_CIRCLE = register(String.format("%s_stone_circle", dimensionData.getName().toLowerCase()), new StoneCircleFeature(dimensionData));
            generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, register(String.format("%s_stone_circle_cf", dimensionData.getName().toLowerCase()), STONE_CIRCLE.configure(FeatureConfig.DEFAULT).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(120)))));

            generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, register(String.format("%s_spider_lair_cf", dimensionData.getName().toLowerCase()), Features.SPIDER_LAIR.configure(FeatureConfig.DEFAULT).decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(230)))));

            TombFeature tomb = register(String.format("%s_tomb", dimensionData.getName().toLowerCase()), new TombFeature(dimensionData));
            generationSettings.feature(GenerationStep.Feature.SURFACE_STRUCTURES, register(String.format("%s_tomb_cf", dimensionData.getName().toLowerCase()), tomb.configure(FeatureConfig.DEFAULT).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.015f, 1)))));
        }*/

//        DefaultBiomeFeatures.addDefaultLakes(generationSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generationSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generationSettings);
        BiomeDefaultFeatures.addDefaultOres(generationSettings);
        BiomeDefaultFeatures.addDefaultSoftDisks(generationSettings);
        BiomeDefaultFeatures.addDefaultMushrooms(generationSettings);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generationSettings);
        BiomeDefaultFeatures.addDefaultSprings(generationSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(generationSettings);

        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.ABANDONED) || Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED)) {
            ResourceLocation identifier = new ResourceLocation(dimensionData.getId().getNamespace(), dimensionData.getId().getPath() + "_mineshaft");
            if(BuiltinRegistries.STRUCTURES.containsKey(identifier)) identifier = Utils.addSuffixToPath(identifier, String.valueOf(Rands.getRandom().nextInt()));
//            ConfiguredStructureFeature<?, ?> configuredFeature = Registry.register(BuiltinRegistries.STRUCTURES, identifier, Structures.MINESHAFT.configure(new MineshaftFeatureConfig((Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED)) ? 0.016F : 0.004F,
//                    MineshaftFeature.Type.NORMAL)));
//            generationSettings.structureFeature(configuredFeature);
        }

        Features.addDefaultCarvers(generationSettings, dimensionData, biomeData);

        if (biomeData.hasMushrooms()) {
//            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_giant_brown_mushroom_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfiguration(
//                    ImmutableList.of(
//                            ConfiguredFeatures.BROWN_MUSHROOM_GIANT.withChance(1)),
//                    ConfiguredFeatures.OAK
//            )).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, Rands.randFloatRange(0.01F, 1F), 1)))));
//            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, register(String.format("%s_giant_red_mushroom_cf", dimensionData.getName().toLowerCase()), Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfiguration(
//                    ImmutableList.of(
//                            ConfiguredFeatures.RED_MUSHROOM_GIANT.withChance(1)),
//                    ConfiguredFeatures.OAK
//            )).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, Rands.randFloatRange(0.01F, 1F), 1)))));
        }
        if (biomeData.hasMossyRocks())
            BiomeDefaultFeatures.addMossyStoneBlock(generationSettings);

        if (dimensionData.getMobs().containsKey("sheep"))
            spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.SHEEP, dimensionData.getMobs().get("sheep")[0], dimensionData.getMobs().get("sheep")[1], dimensionData.getMobs().get("sheep")[2]));
        if (dimensionData.getMobs().containsKey("pig"))
            spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.PIG, dimensionData.getMobs().get("pig")[0], dimensionData.getMobs().get("pig")[1], dimensionData.getMobs().get("pig")[2]));
        if (dimensionData.getMobs().containsKey("chicken"))
            spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.CHICKEN, dimensionData.getMobs().get("chicken")[0], dimensionData.getMobs().get("chicken")[1], dimensionData.getMobs().get("chicken")[2]));
        if (dimensionData.getMobs().containsKey("cow"))
            spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.COW, dimensionData.getMobs().get("cow")[0], dimensionData.getMobs().get("cow")[1], dimensionData.getMobs().get("cow")[2]));
        if (dimensionData.getMobs().containsKey("horse"))
            spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.HORSE, dimensionData.getMobs().get("horse")[0], dimensionData.getMobs().get("horse")[1], dimensionData.getMobs().get("horse")[2]));
        if (dimensionData.getMobs().containsKey("donkey"))
            spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.DONKEY, dimensionData.getMobs().get("donkey")[0], dimensionData.getMobs().get("donkey")[1], dimensionData.getMobs().get("donkey")[2]));

        if (dimensionData.getMobs().containsKey("bat"))
            spawnSettings.addSpawn(MobCategory.AMBIENT, new SpawnerData(EntityType.BAT, dimensionData.getMobs().get("bat")[0], dimensionData.getMobs().get("bat")[1], dimensionData.getMobs().get("bat")[2]));

        if (dimensionData.getMobs().containsKey("spider"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.SPIDER, dimensionData.getMobs().get("spider")[0], dimensionData.getMobs().get("spider")[1], dimensionData.getMobs().get("spider")[2]));
        if (dimensionData.getMobs().containsKey("zombie"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.ZOMBIE, dimensionData.getMobs().get("zombie")[0], dimensionData.getMobs().get("zombie")[1], dimensionData.getMobs().get("zombie")[2]));
        if (dimensionData.getMobs().containsKey("zombie_villager"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.ZOMBIE_VILLAGER, dimensionData.getMobs().get("zombie_villager")[0], dimensionData.getMobs().get("zombie_villager")[1], dimensionData.getMobs().get("zombie_villager")[2]));
        if (dimensionData.getMobs().containsKey("skeleton"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.SKELETON, dimensionData.getMobs().get("skeleton")[0], dimensionData.getMobs().get("skeleton")[1], dimensionData.getMobs().get("skeleton")[2]));
        if (dimensionData.getMobs().containsKey("creeper"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.CREEPER, dimensionData.getMobs().get("creeper")[0], dimensionData.getMobs().get("creeper")[1], dimensionData.getMobs().get("creeper")[2]));
        if (dimensionData.getMobs().containsKey("slime"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.SLIME, dimensionData.getMobs().get("slime")[0], dimensionData.getMobs().get("slime")[1], dimensionData.getMobs().get("slime")[2]));
        if (dimensionData.getMobs().containsKey("enderman"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.ENDERMAN, dimensionData.getMobs().get("enderman")[0], dimensionData.getMobs().get("enderman")[1], dimensionData.getMobs().get("enderman")[2]));
        if (dimensionData.getMobs().containsKey("witch"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.WITCH, dimensionData.getMobs().get("witch")[0], dimensionData.getMobs().get("witch")[1], dimensionData.getMobs().get("witch")[2]));

        if (dimensionData.getMobs().containsKey("blaze"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.WITCH, dimensionData.getMobs().get("blaze")[0], dimensionData.getMobs().get("blaze")[1], dimensionData.getMobs().get("blaze")[2]));
        if (dimensionData.getMobs().containsKey("piglin"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.WITCH, dimensionData.getMobs().get("piglin")[0], dimensionData.getMobs().get("piglin")[1], dimensionData.getMobs().get("piglin")[2]));
        if (dimensionData.getMobs().containsKey("zombified_piglin"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.WITCH, dimensionData.getMobs().get("zombified_piglin")[0], dimensionData.getMobs().get("zombified_piglin")[1], dimensionData.getMobs().get("zombified_piglin")[2]));
        if (dimensionData.getMobs().containsKey("ghast"))
            spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.WITCH, dimensionData.getMobs().get("ghast")[0], dimensionData.getMobs().get("ghast")[1], dimensionData.getMobs().get("ghast")[2]));
    }

    /*public static BranchedTreeFeatureConfig getTreeConfig(DimensionTreeData treeData) {
        BranchedTreeFeatureConfig config;
        int height = treeData.getBaseHeight();
        int foliageHeight = treeData.getFoliageHeight();
        WoodType woodType = treeData.getWoodType().woodType;
        BlockState logState = woodType.getLog().getDefaultState();
        BlockState leafState = woodType.getLeaves().getDefaultState();

        ArrayList<TreeDecorator> decoratorsRaw = new ArrayList<>();
        if (treeData.hasLeafVines()) decoratorsRaw.add(new LeaveVineTreeDecorator());
        if (treeData.hasTrunkVines()) decoratorsRaw.add(new TrunkVineTreeDecorator());
        if (treeData.hasCocoaBeans()) decoratorsRaw.add(new CocoaBeansTreeDecorator(Rands.randFloatRange(0.1F, 1F)));
        //if (Rands.chance(3)) decoratorsRaw.add(new BeehiveTreeDecorator(Rands.randFloatRange(0.01F, 1F)));
        if (treeData.hasPodzolUnderneath())
            decoratorsRaw.add(new AlterGroundTreeDecorator(new SimpleBlockStateProvider(Blocks.PODZOL.getDefaultState())));
        ImmutableList<TreeDecorator> decorators = ImmutableList.copyOf(decoratorsRaw);

        switch (Rands.randInt(9)) {
            case 1:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new SpruceFoliagePlacer(Rands.randIntRange(1, 4), 0)))
                        .baseHeight(Rands.randIntRange(1, 6)) //trunk height rand 1
                        .heightRandA(height - 1) //trunk height rand 2
                        .foliageHeight(foliageHeight) //foliage amount
                        .foliageHeightRandom(Rands.randIntRange(1, 6)) //random foliage offset
                        .maxFluidDepth(treeData.getMaxWaterDepth()) //water depth
                        .trunkHeight(Rands.randIntRange(1, 8)) //trunk height
                        .trunkHeightRandom(Rands.randIntRange(1, 4)) //trunk height offset
                        .trunkTopOffsetRandom(Rands.randIntRange(1, 2)) //foliage height
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 2:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new PineFoliagePlacer(Rands.randIntRange(1, 2), 0)))
                        .baseHeight(Rands.randIntRange(1, 4))
                        .heightRandA(height - 1)
                        .trunkTopOffset(Rands.randIntRange(1, 2))
                        .foliageHeight(foliageHeight / 2)
                        .foliageHeightRandom(Rands.randIntRange(1, 4))
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 3:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new AcaciaFoliagePlacer(Rands.randIntRange(1, 4), 0)))
                        .baseHeight(Rands.randIntRange(1, 6))
                        .heightRandA(height - 1)
                        .heightRandB(height + Rands.randIntRange(1, 4))
                        .trunkHeight(Rands.randIntRange(1, 8))
                        .foliageHeight(foliageHeight) //foliage amount
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 4:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new CylinderFoliagePlacer(Rands.randIntRange(1, 3), 0)))
                        .baseHeight(Rands.randIntRange(1, 6))
                        .heightRandA(height - 1)
                        .heightRandB(height + Rands.randIntRange(1, 4))
                        .trunkHeight(Rands.randIntRange(1, 8))
                        .foliageHeight(foliageHeight) //foliage amount
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 5:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new UpsideDownOakFoliagePlacer(Rands.randIntRange(1, 3), 0)))
                        .baseHeight(Rands.randIntRange(1, 6))
                        .heightRandA(height - 1)
                        .heightRandB(height + Rands.randIntRange(1, 4))
                        .trunkHeight(Rands.randIntRange(1, 8))
                        .foliageHeight(foliageHeight) //foliage amount
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 6:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new LongOakFoliagePlacer(Rands.randIntRange(1, 3), 0)))
                        .baseHeight(Rands.randIntRange(1, 6))
                        .heightRandA(height - 1)
                        .heightRandB(height + Rands.randIntRange(1, 4))
                        .trunkHeight(Rands.randIntRange(1, 8))
                        .foliageHeight(foliageHeight) //foliage amount
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 7:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new BoringOakFoliagePlacer(Rands.randIntRange(1, 3), 0)))
                        .baseHeight(Rands.randIntRange(1, 6))
                        .heightRandA(height - 1)
                        .heightRandB(height + Rands.randIntRange(1, 4))
                        .trunkHeight(Rands.randIntRange(1, 8))
                        .foliageHeight(foliageHeight) //foliage amount
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 8:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new RandomSpruceFoliagePlacer(Rands.randIntRange(1, 3), 0)))
                        .baseHeight(Rands.randIntRange(1, 6))
                        .heightRandA(height - 1)
                        .heightRandB(height + Rands.randIntRange(1, 4))
                        .trunkHeight(Rands.randIntRange(1, 8))
                        .foliageHeight(foliageHeight) //foliage amount
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();
                break;
            case 0:
            default:
                config = (new BranchedTreeFeatureConfig.Builder(new SimpleBlockStateProvider(logState), new SimpleBlockStateProvider(leafState), new BlobFoliagePlacer(Rands.randIntRange(1, 3), 0)))
                        .baseHeight(Rands.randIntRange(1, 6)) //
                        .heightRandA(height - 1) //trunk height
                        .foliageHeight(foliageHeight) //foliage amount
                        .foliageHeightRandom(Rands.randIntRange(1, 6)) //random foliage offset
                        .maxFluidDepth(Rands.randIntRange(0, 8)) //water depth
                        .noVines()
                        .treeDecorators(decorators)
                        .build();

        }
        return config;
    }*/

    private static FoliagePlacer getFoliagePlacer(DimensionTreeData treeData) {
        switch (treeData.getFoliagePlacerType()) {
            case ACACIA:
                return new AcaciaFoliagePlacer(ConstantInt.of(treeData.getFoliageRange()), ConstantInt.of(0));
            case SPRUCE:
                return new SpruceFoliagePlacer(ConstantInt.of(treeData.getFoliageRange()), ConstantInt.of(0), ConstantInt.of(treeData.getFoliageHeight()));
            case PINE:
                return new PineFoliagePlacer(ConstantInt.of(treeData.getFoliageRange()), ConstantInt.of(0), ConstantInt.of(treeData.getFoliageHeight()));
            /*case LONG:
                return new LongOakFoliagePlacer(treeData.getFoliageRange(), 0);
            case UPSIDE_DOWN:
                return new UpsideDownOakFoliagePlacer(treeData.getFoliageRange(), 0);
            case BORING:
                return new BoringOakFoliagePlacer(treeData.getFoliageRange(), 0);
            case RANDOM:
                return new RandomSpruceFoliagePlacer(treeData.getFoliageRange(), 0);
            case CYLINDER:
                return new CylinderFoliagePlacer(treeData.getFoliageRange(), 0);*/
            case OAK:
            default:
                return new BlobFoliagePlacer(ConstantInt.of(treeData.getFoliageRange()), ConstantInt.of(0), treeData.getFoliageHeight());
        }
    }

}