package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.generation.chunkgenerator.ChaosChunkGenerator;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.*;

public class ChunkGenerators {
    public static final GeneratorType CHAOS = new GeneratorType("raa_surface") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new ChaosChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
        }
    };
    private static final GeneratorType FLAT = new GeneratorType("flat") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new FlatChunkGenerator(FlatChunkGeneratorConfig.getDefaultConfig(biomeRegistry));
        }
    };
    private static final GeneratorType LARGE_BIOMES = new GeneratorType("large_biomes") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new VanillaLayeredBiomeSource(seed, false, true, biomeRegistry), seed, () -> {
                return chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
            });
        }
    };
    public static final GeneratorType AMPLIFIED = new GeneratorType("amplified") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () -> {
                return chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.AMPLIFIED);
            });
        }
    };
    private static final GeneratorType SINGLE_BIOME_CAVES = new GeneratorType("single_biome_caves") {
        public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures, boolean bonusChest) {
            Registry<Biome> registry = registryManager.get(Registry.BIOME_KEY);
            Registry<DimensionType> registry2 = registryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<ChunkGeneratorSettings> registry3 = registryManager.get(Registry.NOISE_SETTINGS_WORLDGEN);
            return new GeneratorOptions(seed, generateStructures, bonusChest, GeneratorOptions.method_29962(DimensionType.createDefaultDimensionOptions(registry2, registry, registry3, seed), () -> {
                return registry2.getOrThrow(DimensionType.OVERWORLD_CAVES_REGISTRY_KEY);
            }, this.getChunkGenerator(registry, registry3, seed)));
        }

        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS)), seed, () -> {
                return chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.CAVES);
            });
        }
    };
    private static final GeneratorType SINGLE_BIOME_FLOATING_ISLANDS = new GeneratorType("single_biome_floating_islands") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS)), seed, () -> {
                return chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.FLOATING_ISLANDS);
            });
        }
    };
    /*public static ChunkGeneratorType<FloatingIslandsChunkGeneratorConfig, FloatingIslandsChunkGenerator> FLOATING_ISLANDS;
    public static ChunkGeneratorType<FloatingIslandsChunkGeneratorConfig, LayeredFloatingIslandsChunkGenerator> LAYERED_FLOATING;
    public static ChunkGeneratorType<FloatingIslandsChunkGeneratorConfig, PreClassicFloatingIslandsChunkGenerator> PRE_CLASSIC_FLOATING;

    public static ChunkGeneratorType<CavesChunkGeneratorConfig, CavesChunkGenerator> CAVES;
    public static ChunkGeneratorType<CavesChunkGeneratorConfig, FlatCavesChunkGenerator> FLAT_CAVES;
    public static ChunkGeneratorType<CavesChunkGeneratorConfig, HighCavesChunkGenerator> HIGH_CAVES;

    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, QuadrupleAmplifiedChunkGenerator> QUADRUPLE_AMPLIFIED;
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, PillarWorldChunkGenerator> PILLAR_WORLD;
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, SmoothOverworldChunkGenerator> SMOOTH;
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, TotallyCustomChunkGenerator> TOTALLY_CUSTOM;
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, LayeredChunkGenerator> LAYERED_OVERWORLD;
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, ChaosChunkGenerator> CHAOS;
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, RollingHillsChunkGenerator> ROLLING_HILLS;

    public static ChunkGeneratorType<NoneGeneratorSettings, RetroChunkGenerator> RETRO;
    public static ChunkGeneratorType<NoneGeneratorSettings, CheckerboardChunkGenerator> CHECKERBOARD;

    //this is only for testing use!
    public static ChunkGeneratorType<OverworldChunkGeneratorConfig, TestChunkGenerator> TEST;

    public static void init() {
        //End-like chunk generators
        FLOATING_ISLANDS = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "floating_islands"), FloatingIslandsChunkGenerator::new, FloatingIslandsChunkGeneratorConfig::new, false);
        LAYERED_FLOATING = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "layered_floating"), LayeredFloatingIslandsChunkGenerator::new, FloatingIslandsChunkGeneratorConfig::new, false);
        PRE_CLASSIC_FLOATING = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "pre_classic_floating"), PreClassicFloatingIslandsChunkGenerator::new, FloatingIslandsChunkGeneratorConfig::new, false);

        //Nether-like chunk generators
        CAVES = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "caves"), CavesChunkGenerator::new, CavesChunkGeneratorConfig::new, false);
        FLAT_CAVES = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "flat_caves"), FlatCavesChunkGenerator::new, CavesChunkGeneratorConfig::new, false);
        HIGH_CAVES = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "high_caves"), HighCavesChunkGenerator::new, CavesChunkGeneratorConfig::new, false);

        //Overworld-like chunk generators
        SURFACE = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "surface"), OverworldChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        QUADRUPLE_AMPLIFIED = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "quadruple_amplified"), QuadrupleAmplifiedChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        PILLAR_WORLD = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "pillar_world"), PillarWorldChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        SMOOTH = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "smooth_overworld"), SmoothOverworldChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        TOTALLY_CUSTOM = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "totally_custom"), TotallyCustomChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        LAYERED_OVERWORLD = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "layered_overworld"), LayeredChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        CHAOS = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "chaos"), ChaosChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
        ROLLING_HILLS = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "rolling_hills"), RollingHillsChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);

        RETRO = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "retro"), RetroChunkGenerator::new, NoneGeneratorSettings::new, false);
        CHECKERBOARD = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "checkerboard"), CheckerboardChunkGenerator::new, NoneGeneratorSettings::new, false);

        TEST = RegistryUtils.registerChunkGenerator(new Identifier(MOD_ID, "test"), TestChunkGenerator::new, OverworldChunkGeneratorConfig::new, false);
    }*/

}