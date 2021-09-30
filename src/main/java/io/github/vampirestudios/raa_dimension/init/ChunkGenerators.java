package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.generation.chunkgenerator.ChaosChunkGenerator;
import io.github.vampirestudios.raa_dimension.generation.chunkgenerator.CheckerboardChunkGenerator;
import io.github.vampirestudios.raa_dimension.generation.chunkgenerator.LayeredChunkGenerator;
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
    public static final GeneratorType CHAOS = new GeneratorType("chaos") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new ChaosChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
        }
    };
    public static final GeneratorType LAYERED = new GeneratorType("layered") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new LayeredChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
        }
    };
    public static final GeneratorType CHECKERBOARD = new GeneratorType("checkerboard") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new CheckerboardChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
        }
    };
    private static final GeneratorType TOTALLY_CUSTOM = new GeneratorType("totally_custom") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new FlatChunkGenerator(FlatChunkGeneratorConfig.getDefaultConfig(biomeRegistry));
        }
    };public static final GeneratorType RETRO = new GeneratorType("retro") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new ChaosChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
        }
    };
    private static final GeneratorType LARGE_BIOMES = new GeneratorType("large_biomes") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new VanillaLayeredBiomeSource(seed, false, true, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
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
            Registry<ChunkGeneratorSettings> registry3 = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            return new GeneratorOptions(seed, generateStructures, bonusChest, GeneratorOptions.getRegistryWithReplacedOverworld(DimensionType.createDefaultDimensionOptions(registry2, registry, registry3, seed), () ->
                    registry2.getOrThrow(DimensionType.OVERWORLD_CAVES_REGISTRY_KEY), this.getChunkGenerator(registry, registry3, seed)));
        }

        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS)), seed, () -> chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.CAVES));
        }
    };
    private static final GeneratorType SINGLE_BIOME_FLOATING_ISLANDS = new GeneratorType("single_biome_floating_islands") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new NoiseChunkGenerator(new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS)), seed, () -> {
                return chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.FLOATING_ISLANDS);
            });
        }
    };
}