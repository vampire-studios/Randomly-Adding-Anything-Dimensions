package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import io.github.vampirestudios.raa_dimension.init.SurfaceBuilders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import java.util.Random;
import java.util.stream.IntStream;

public class PatchyDarkBadlandsSurfaceElement extends SurfaceElement {
    public static final PerlinSimplexNoise MESA_NOISE = new PerlinSimplexNoise(new ChunkRandom(79L), IntStream.of(6, 0));

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks) {
        double mesaNoise = MESA_NOISE.getValue(x * 0.049765625D, z * 0.049765625D, false);
        if (mesaNoise > 0.0D) {
            SurfaceBuilders.DARK_BADLANDS.initSeed(seed);
            SurfaceBuilders.DARK_BADLANDS.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownNumber, seed, SurfaceBuilder.BADLANDS_CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownNumber, seed, surfaceBlocks);
        }
    }

    @Override
    public void serialize(JsonObject obj) {}

    @Override
    public void deserialize(JsonObject obj) {}

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("raa_dimensions", "patchy_dark_badlands");
    }

    @Override
    public int getPriority() {
        return 100;
    }

}
