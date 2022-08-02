package io.github.vampirestudios.raa_dimension.generation.surface.random;

import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import java.util.List;
import java.util.Random;

public class RandomSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {
    private final List<SurfaceElement> elements;

    public RandomSurfaceBuilder(List<SurfaceElement> elements) {
        super(TernarySurfaceConfig.CODEC);
        this.elements = elements;
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        for (SurfaceElement element : elements) {
            element.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, surfaceConfig);
        }
    }
}
