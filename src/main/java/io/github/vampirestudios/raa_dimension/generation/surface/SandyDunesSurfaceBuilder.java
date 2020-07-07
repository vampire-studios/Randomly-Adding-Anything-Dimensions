package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.utils.WorleyNoise;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

//This code is an edit of the dunes surface builder kindly taken from Terrestria. Thank you, coderbot, Prospector, and Valoeghese!
public class SandyDunesSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {

    private static final WorleyNoise NOISE = new WorleyNoise(3445);

    public SandyDunesSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int vHeight, double noise, BlockState stone, BlockState water, int seaLevel, long seed, TernarySurfaceConfig config) {
        vHeight = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(x & 15, z & 15);
        BlockPos.Mutable pos = new BlockPos.Mutable(x, vHeight - 8, z);

        double blend = MathHelper.clamp((vHeight - seaLevel) * 0.125, 0, 1);

        double height = (NOISE.sample(x * 0.01, z * 0.015) * 30) * blend;

        height = Math.abs(height);

        for (int i = 0; i < 8; i++) {
            chunk.setBlockState(pos, Blocks.CUT_SANDSTONE.getDefaultState(), false);
            pos.offset(Direction.UP);
        }

        // Cap the height based on noise

        height = Math.min(height, (NOISE.sample(x * 0.03 + 5, z * 0.05 + 5) * 30 + 6));

        for (int h = 0; h < height; h++) {
            chunk.setBlockState(pos, Blocks.SANDSTONE.getDefaultState(), false);
            pos.offset(Direction.UP);
        }

        for (int i = 0; i < 3 + (noise / 2); i++) {
            chunk.setBlockState(pos, Blocks.SMOOTH_SANDSTONE.getDefaultState(), false);
            pos.offset(Direction.UP);
        }

        for (int i = 0; i < 3 + (noise / 2); i++) {
            chunk.setBlockState(pos, Blocks.SAND.getDefaultState(), false);
        }
    }
}