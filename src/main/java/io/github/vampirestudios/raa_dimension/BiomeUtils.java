package io.github.vampirestudios.raa_dimension;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BiomeUtils {

    public static void addFeatureToBiome(Biome biome, GenerationStep.Feature feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().features;
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);
    }

    private static void ConvertImmutableFeatures(Biome biome) {
        biome.getGenerationSettings().features = biome.getGenerationSettings().features.stream().map(Lists::newArrayList).collect(Collectors.toList());
    }

    public static ConfiguredSurfaceBuilder<?> newConfiguredSurfaceBuilder(String id, ConfiguredSurfaceBuilder<?> configuredSurfaceBuilder) {
        Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, id), configuredSurfaceBuilder);
        return configuredSurfaceBuilder;
    }

    public static void addStructureFeatureToBiome(Biome biome, GenerationStep.Feature feature, ConfiguredStructureFeature<?, ?> configuredFeature) {
        convertImmutableStructureFeatures(biome);
        List<Supplier<ConfiguredStructureFeature<?, ?>>> biomeFeatures = biome.getGenerationSettings().structureFeatures;
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(null);
        }
        biomeFeatures.add(() -> configuredFeature);
    }
    private static void convertImmutableStructureFeatures(Biome biome) {
        if (biome.getGenerationSettings().structureFeatures instanceof ImmutableList) {
            biome.getGenerationSettings().structureFeatures = new ArrayList<>(biome.getGenerationSettings().structureFeatures);
        }
    }

    public static int calcSkyColor(float f) {
        float g = f / 3.0F;
        g = MathHelper.clamp(g, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - g * 0.05F, 0.5F + g * 0.1F, 1.0F);
    }

    public static void registerEndBiome(Biome biome, String id) {
        if (biome != null) {
            Registry.register(BuiltinRegistries.BIOME, new Identifier(RAADimensionAddon.MOD_ID, id), biome);
        }
    }

}
