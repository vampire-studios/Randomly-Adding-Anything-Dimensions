package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class RedDesertSurfaceElement extends SurfaceElement {
    private static final TernarySurfaceConfig CONFIG = new TernarySurfaceConfig(Blocks.RED_SAND.getDefaultState(), Blocks.RED_SANDSTONE.getDefaultState(), Blocks.GRAVEL.getDefaultState());
    private double chance;

    public RedDesertSurfaceElement() {
        chance = Rands.randFloatRange(-0.25f, 2);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig surfaceBlocks) {
        if (noise > chance) {
            resetTop(chunk, x, z, height, defaultBlock, defaultFluid);
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, seed, CONFIG);
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
    public Identifier getType() {
        return new Identifier("raa", "red_desert");
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
