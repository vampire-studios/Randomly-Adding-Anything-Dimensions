package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;

public class GrassSurfaceElement extends SurfaceElement {

    public GrassSurfaceElement() {
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks) {
        SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownNumber, seed, surfaceBlocks);
    }

    @Override
    public void serialize(JsonObject obj) {
    }

    @Override
    public void deserialize(JsonObject obj) {
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("raa_dimensions", "grass");
    }

    @Override
    public int getPriority() {
        return 95;
    }
}
