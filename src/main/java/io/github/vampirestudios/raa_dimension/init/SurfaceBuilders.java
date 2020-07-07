package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.surface.*;
import io.github.vampirestudios.raa_dimension.generation.surface.vanilla_variants.DarkBadlandsSurfaceBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class SurfaceBuilders {
    public static DarkBadlandsSurfaceBuilder DARK_BADLANDS;

    public static PatchyDesertSurfaceBuilder PATCHY_DESERT;
    public static PatchyBadlandsSurfaceBuilder PATCHY_BADLANDS;
    public static PatchyDarkBadlandsSurfaceBuilder DARK_PATCHY_BADLANDS;
    public static ClassicCliffsSurfaceBuilder CLASSIC_CLIFFS;
    public static StratifiedSurfaceBuilder STRATIFIED_CLIFFS;
    public static FloatingIslandSurfaceBuilder FLOATING_ISLANDS;
    public static DuneSurfaceBuilder DUNES;
    public static SandyDunesSurfaceBuilder SANDY_DUNES;
    public static LazyNoiseSurfaceBuilder LAZY_NOISE;
    public static HyperflatSurfaceBuilder HYPER_FLAT;

    public static void init() {
        DARK_BADLANDS = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "dark_badlands"),
                new DarkBadlandsSurfaceBuilder(TernarySurfaceConfig.CODEC));

        PATCHY_DESERT = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "patchy_desert"),
                new PatchyDesertSurfaceBuilder(TernarySurfaceConfig.CODEC));
        PATCHY_BADLANDS = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "patchy_badlands"),
                new PatchyBadlandsSurfaceBuilder(TernarySurfaceConfig.CODEC));
        DARK_PATCHY_BADLANDS = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "dark_patchy_badlands"),
                new PatchyDarkBadlandsSurfaceBuilder(TernarySurfaceConfig.CODEC));
        CLASSIC_CLIFFS = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "classic_cliffs"),
                new ClassicCliffsSurfaceBuilder(TernarySurfaceConfig.CODEC));
        STRATIFIED_CLIFFS = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "stratified_cliffs"),
                new StratifiedSurfaceBuilder(TernarySurfaceConfig.CODEC));
        FLOATING_ISLANDS = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "floating_islands"),
                new FloatingIslandSurfaceBuilder(TernarySurfaceConfig.CODEC));
        DUNES = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "dunes"),
                new DuneSurfaceBuilder(TernarySurfaceConfig.CODEC));
        SANDY_DUNES = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "sandy_dunes"),
                new SandyDunesSurfaceBuilder(TernarySurfaceConfig.CODEC));
        LAZY_NOISE = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "lazy_noise"),
                new LazyNoiseSurfaceBuilder(TernarySurfaceConfig.CODEC));
        HYPER_FLAT = Registry.register(Registry.SURFACE_BUILDER, new Identifier(RAADimensionAddon.MOD_ID, "hyper_flat"),
                new HyperflatSurfaceBuilder(TernarySurfaceConfig.CODEC));
    }
}
