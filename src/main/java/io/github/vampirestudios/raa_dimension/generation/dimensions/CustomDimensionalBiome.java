package io.github.vampirestudios.raa_dimension.generation.dimensions;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionBiomeData;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionTreeData;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class CustomDimensionalBiome extends Biome {

    static final SpawnSettings.Builder SPAWN_SETTINGS = new SpawnSettings.Builder();
    static final GenerationSettings.Builder GENERATION_SETTINGS = (new GenerationSettings.Builder())
            .carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
            .feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.SPRING_LAVA)
            .structureFeature(ConfiguredStructureFeatures.FORTRESS);

    private static DimensionData dimensionData;
    private static DimensionBiomeData biomeData;

    public CustomDimensionalBiome(DimensionData dimensionDataIn, DimensionBiomeData biomeDataIn) {
        super(new Weather(Utils.checkBitFlag(dimensionDataIn.getFlags(), Utils.FROZEN) ? Precipitation.SNOW : Rands.chance(10) ? Precipitation.RAIN : Precipitation.NONE,
                biomeDataIn.getTemperature(), TemperatureModifier.NONE, biomeDataIn.getDownfall()),
                Category.PLAINS,
                biomeDataIn.getDepth(),
                biomeDataIn.getScale(),
                new BiomeEffects.Builder()
                        .fogColor(dimensionDataIn.getDimensionColorPalette().getFogColor())
                        .waterColor(biomeDataIn.getWaterColor())
                        .waterFogColor(biomeDataIn.getWaterColor())
                        .skyColor(dimensionDataIn.getDimensionColorPalette().getSkyColor())
                        .build(), GENERATION_SETTINGS.surfaceBuilder(new ConfiguredSurfaceBuilder<>((SurfaceBuilder<TernarySurfaceConfig>) Registry.SURFACE_BUILDER.get(biomeDataIn.getSurfaceBuilder()), biomeDataIn.getSurfaceConfig())).build(), SPAWN_SETTINGS.build());
        dimensionData = dimensionDataIn;
        biomeData = biomeDataIn;

        /*if (Registry.SURFACE_BUILDER.get(biomeData.getSurfaceBuilder()) == SurfaceBuilders.HYPER_FLAT) {
            DefaultBiomeFeatures.addSeagrassOnStone(this);
            DefaultBiomeFeatures.addKelp(this);
        }*/

        /*if (Rands.chance(4) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.DRY) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.MOLTEN)) {
            DefaultBiomeFeatures.addPlainsTallGrass(GENERATION_SETTINGS);
        }*/

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
                    this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                            getMegaTree(treeData.getTreeType())
                                    .configure(
                                            config
                                    ).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, treeData.getChance(), 1))));
                    ConfiguredFeatures.DARK_OAK
                } else {
                    BranchedTreeFeatureConfig config1 = getTreeConfig(treeData);
                    this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
                            getNormalTree(treeData.getTreeType())
                                    .configure(
                                            config1
                                    ).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, treeData.getChance(), 1))));
                }
            }
        }

        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) || Utils.checkBitFlag(dimensionData.getFlags(), Utils.DRY)) {
            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Features.LARGE_SKELETON_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, biomeData.getLargeSkeletonTreeChance(), 1))));
            this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Feature.FOSSIL.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_PASSTHROUGH.configure(new ChanceDecoratorConfig(64))));
        }

        if (!Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && !Utils.checkBitFlag(dimensionData.getFlags(), Utils.CORRUPTED)) {
            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.SUNFLOWER_CONFIG)
                    .createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 50 : 20))));
            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.ROSE_BUSH_CONFIG)
                    .createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 20 : 5))));
            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.LILAC_CONFIG)
                    .createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 20 : 5))));

            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.SUGAR_CANE_CONFIG)
                    .createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 20 : 5))));

            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.PUMPKIN_PATCH_CONFIG)
                    .createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUSH) ? 50 : 20))));
        }

        if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.CORRUPTED)) {
            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Features.CRATER_FEATURE.configure(new CorruptedFeatureConfig(true)).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, Rands.randFloatRange(0, 1F), 1))));
            this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Features.CORRUPTED_NETHRRACK.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(2))));
        } else {
            if (biomeData.spawnsCratersInNonCorrupted()) {
                this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.CRATER_FEATURE.configure(new CorruptedFeatureConfig(false)).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, Rands.randFloatRange(0, 1F), 1))));
            }
        }*/

        float campfireChance = biomeDataIn.getCampfireChance();
        float outpostChance = biomeDataIn.getOutpostChance();
        float towerChance = biomeDataIn.getTowerChance();
        float fossilChance = 0;
        float shrineChance = 0.002F;

        if (Utils.checkBitFlag(dimensionDataIn.getFlags(), Utils.ABANDONED)) {
            outpostChance = Rands.randFloatRange(0.002F, 0.003F);
            towerChance = Rands.randFloatRange(0.002F, 0.00225F);
        }
        if (Utils.checkBitFlag(dimensionDataIn.getFlags(), Utils.DEAD)) {
            campfireChance = 0;
            fossilChance = Rands.randFloatRange(0.007F, 0.0075F);
        }
        if (Utils.checkBitFlag(dimensionDataIn.getFlags(), Utils.CIVILIZED)) {
            campfireChance = Rands.randFloatRange(0.005F, 0.007F);
            outpostChance = Rands.randFloatRange(0.002F, 0.008F);
            towerChance = Rands.randFloatRange(0.002F, 0.003F);
        }

        /*if (dimensionData.getDimensionChunkGenerator().equals(DimensionChunkGenerators.CAVES)) {
            this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.BASALT_PILLAR.configure(FeatureConfig.DEFAULT)
                    .createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 256))));
        }*/

        // TODO fix this
        /*this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.OUTPOST.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, outpostChance, 1))));
        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.CAMPFIRE.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, campfireChance, 1))));
        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.TOWER.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, towerChance, 1))));
        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.FOSSIL.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, fossilChance, 1))));
        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.SHRINE.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, shrineChance, 1))));

        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.STONE_HENGE.configure(new DefaultFeatureConfig()).createDecoratedFeature(
                        Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(
                                new CountExtraChanceDecoratorConfig(0, 0.001F, 1)
                        )
                )
        );
        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.COLUMN_RAMP.configure(new ColumnBlocksConfig(Blocks.STONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(),
                        Blocks.NETHERRACK.getDefaultState())).createDecoratedFeature(
                        Decorator.COUNT_RANGE.configure(
                                new RangeDecoratorConfig(2, 70, 0, 220)
                        )
                )
        );
        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.COLUMN_VERTICAL.configure(new ColumnBlocksConfig(Blocks.STONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(),
                        Blocks.NETHERRACK.getDefaultState())).createDecoratedFeature(
                        Decorator.COUNT_RANGE.configure(
                                new RangeDecoratorConfig(2, 70, 0, 220)
                        )
                )
        );*/
        /*this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.HANGING_RUINS.configure(FeatureConfig.DEFAULT).createDecoratedFeature(
                        RAAPlacements.LEDGE_UNDERSIDE_MINI_FEATURE.configure(
                                new ChanceAndTypeConfig(0.1F, ChanceAndTypeConfig.Type.HANGING_RUINS)
                        )
                )
        );*/
        /*this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,
                Features.HANGING_RUINS.configure(new DefaultFeatureConfig()).createDecoratedFeature(
                        Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(
                                new CountExtraChanceDecoratorConfig(0, 0.001F, 1)
                        )
                )
        );*/

//        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.BEE_NEST.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, 1.0f, 1))));
//        this.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, Features.CAVE_CAMPFIRE.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, 1.0f, 1))));
//        this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.MUSHROOM_RUIN.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, 1.0f, 1))));
//        this.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, Features.UNDERGROUND_BEE_HIVE.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, 1.0f, 1))));



        /*if ((Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED)) || (Utils.checkBitFlag(dimensionData.getFlags(), Utils.DEAD) && Utils.checkBitFlag(dimensionData.getFlags(), Utils.ABANDONED))) {
            StoneCircleFeature STONE_CIRCLE = Features.register(String.format("%s_stone_circle", dimensionData.getName().toLowerCase()), new StoneCircleFeature(dimensionData));
            this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, STONE_CIRCLE.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(120))));

            this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Features.SPIDER_LAIR.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(230))));

            TombFeature tomb = Features.register(String.format("%s_tomb", dimensionData.getName().toLowerCase()), new TombFeature(dimensionData));
            this.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, tomb.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorators.RANDOM_EXTRA_HEIGHTMAP_DECORATOR.configure(new CountExtraChanceDecoratorConfig(0, 0.015f, 1))));
        }*/
    }

    static {
//        DefaultBiomeFeatures.addDefaultLakes(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addDungeons(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addMineables(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addDefaultOres(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addDefaultDisks(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addDefaultMushrooms(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addDefaultVegetation(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addSprings(GENERATION_SETTINGS);
        DefaultBiomeFeatures.addFrozenTopLayer(GENERATION_SETTINGS);

        /*if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.ABANDONED) || Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED))
            GENERATION_SETTINGS.structureFeature(Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(dimensionData.getId().getNamespace(), dimensionData.getId().getPath() + "_mineshaft"), StructureFeature.MINESHAFT.configure(new MineshaftFeatureConfig((Utils.checkBitFlag(dimensionData.getFlags(), Utils.CIVILIZED)) ? 0.016F : 0.004F,
                    MineshaftFeature.Type.NORMAL))));*/

//        Features.addDefaultCarvers(dimensionData, biomeData);

        /*if (biomeData.hasMushrooms()) {
            GENERATION_SETTINGS.feature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                    ImmutableList.of(
                            ConfiguredFeatures.BROWN_MUSHROOM_GIANT.withChance(1)),
                    ConfiguredFeatures.OAK
            )).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, Rands.randFloatRange(0.01F, 1F), 1))));
            GENERATION_SETTINGS.feature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                    ImmutableList.of(
                            ConfiguredFeatures.RED_MUSHROOM_GIANT.withChance(1)),
                    ConfiguredFeatures.OAK
            )).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, Rands.randFloatRange(0.01F, 1F), 1))));
        }
        if (biomeData.hasMossyRocks())*/
            DefaultBiomeFeatures.addMossyRocks(GENERATION_SETTINGS);

//        if (dimensionData.getMobs().containsKey("sheep"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.SHEEP, dimensionData.getMobs().get("sheep")[0], dimensionData.getMobs().get("sheep")[1], dimensionData.getMobs().get("sheep")[2]));
//        if (dimensionData.getMobs().containsKey("pig"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.PIG, dimensionData.getMobs().get("pig")[0], dimensionData.getMobs().get("pig")[1], dimensionData.getMobs().get("pig")[2]));
//        if (dimensionData.getMobs().containsKey("chicken"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.CHICKEN, dimensionData.getMobs().get("chicken")[0], dimensionData.getMobs().get("chicken")[1], dimensionData.getMobs().get("chicken")[2]));
//        if (dimensionData.getMobs().containsKey("cow"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.COW, dimensionData.getMobs().get("cow")[0], dimensionData.getMobs().get("cow")[1], dimensionData.getMobs().get("cow")[2]));
//        if (dimensionData.getMobs().containsKey("horse"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.HORSE, dimensionData.getMobs().get("horse")[0], dimensionData.getMobs().get("horse")[1], dimensionData.getMobs().get("horse")[2]));
//        if (dimensionData.getMobs().containsKey("donkey"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.DONKEY, dimensionData.getMobs().get("donkey")[0], dimensionData.getMobs().get("donkey")[1], dimensionData.getMobs().get("donkey")[2]));
//
//        if (dimensionData.getMobs().containsKey("bat"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.AMBIENT, new SpawnEntry(EntityType.BAT, dimensionData.getMobs().get("bat")[0], dimensionData.getMobs().get("bat")[1], dimensionData.getMobs().get("bat")[2]));
//
//        if (dimensionData.getMobs().containsKey("spider"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.SPIDER, dimensionData.getMobs().get("spider")[0], dimensionData.getMobs().get("spider")[1], dimensionData.getMobs().get("spider")[2]));
//        if (dimensionData.getMobs().containsKey("zombie"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.ZOMBIE, dimensionData.getMobs().get("zombie")[0], dimensionData.getMobs().get("zombie")[1], dimensionData.getMobs().get("zombie")[2]));
//        if (dimensionData.getMobs().containsKey("zombie_villager"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.ZOMBIE_VILLAGER, dimensionData.getMobs().get("zombie_villager")[0], dimensionData.getMobs().get("zombie_villager")[1], dimensionData.getMobs().get("zombie_villager")[2]));
//        if (dimensionData.getMobs().containsKey("skeleton"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.SKELETON, dimensionData.getMobs().get("skeleton")[0], dimensionData.getMobs().get("skeleton")[1], dimensionData.getMobs().get("skeleton")[2]));
//        if (dimensionData.getMobs().containsKey("creeper"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.CREEPER, dimensionData.getMobs().get("creeper")[0], dimensionData.getMobs().get("creeper")[1], dimensionData.getMobs().get("creeper")[2]));
//        if (dimensionData.getMobs().containsKey("slime"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.SLIME, dimensionData.getMobs().get("slime")[0], dimensionData.getMobs().get("slime")[1], dimensionData.getMobs().get("slime")[2]));
//        if (dimensionData.getMobs().containsKey("enderman"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.ENDERMAN, dimensionData.getMobs().get("enderman")[0], dimensionData.getMobs().get("enderman")[1], dimensionData.getMobs().get("enderman")[2]));
//        if (dimensionData.getMobs().containsKey("witch"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.WITCH, dimensionData.getMobs().get("witch")[0], dimensionData.getMobs().get("witch")[1], dimensionData.getMobs().get("witch")[2]));
//
//        if (dimensionData.getMobs().containsKey("blaze"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.WITCH, dimensionData.getMobs().get("blaze")[0], dimensionData.getMobs().get("blaze")[1], dimensionData.getMobs().get("blaze")[2]));
//        if (dimensionData.getMobs().containsKey("piglin"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.WITCH, dimensionData.getMobs().get("piglin")[0], dimensionData.getMobs().get("piglin")[1], dimensionData.getMobs().get("piglin")[2]));
//        if (dimensionData.getMobs().containsKey("zombified_piglin"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.WITCH, dimensionData.getMobs().get("zombified_piglin")[0], dimensionData.getMobs().get("zombified_piglin")[1], dimensionData.getMobs().get("zombified_piglin")[2]));
//        if (dimensionData.getMobs().containsKey("ghast"))
//            SPAWN_SETTINGS.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.WITCH, dimensionData.getMobs().get("ghast")[0], dimensionData.getMobs().get("ghast")[1], dimensionData.getMobs().get("ghast")[2]));
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
                return new AcaciaFoliagePlacer(UniformIntDistribution.of(treeData.getFoliageRange()), UniformIntDistribution.of(0));
            case SPRUCE:
                return new SpruceFoliagePlacer(UniformIntDistribution.of(treeData.getFoliageRange()), UniformIntDistribution.of(0), UniformIntDistribution.of(treeData.getFoliageHeight()));
            case PINE:
                return new PineFoliagePlacer(UniformIntDistribution.of(treeData.getFoliageRange()), UniformIntDistribution.of(0), UniformIntDistribution.of(treeData.getFoliageHeight()));
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
                return new BlobFoliagePlacer(UniformIntDistribution.of(treeData.getFoliageRange()), UniformIntDistribution.of(0), treeData.getFoliageHeight());
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getSkyColor() {
        return dimensionData.getDimensionColorPalette().getSkyColor();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getGrassColorAt(double x, double z) {
        return dimensionData.getDimensionColorPalette().getGrassColor();
    }

    @Override
    public int getFoliageColor() {
        return dimensionData.getDimensionColorPalette().getFoliageColor();
    }

}