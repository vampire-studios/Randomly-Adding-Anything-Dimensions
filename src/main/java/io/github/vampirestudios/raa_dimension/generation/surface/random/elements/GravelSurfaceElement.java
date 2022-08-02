package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;

public class GravelSurfaceElement extends SurfaceElement {
    private double chance;

    public GravelSurfaceElement() {
        chance = Rands.randFloatRange(-0.25f, 2);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks) {
        if (noise > chance) {
            resetTop(chunk, x, z, height, defaultBlock, defaultFluid);
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownNumber, seed, SurfaceBuilder.GRAVEL_CONFIG);
        }
    }

    @Override
    public void serialize(JsonObject obj) {
        obj.addProperty("chance", chance);
    }

    @Override
    public void deserialize(JsonObject obj) {
        chance = obj.get("chance").getAsDouble();
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("raa_dimensions", "gravel");
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
