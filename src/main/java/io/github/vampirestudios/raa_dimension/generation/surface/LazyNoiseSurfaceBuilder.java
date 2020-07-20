package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class LazyNoiseSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public LazyNoiseSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState stone, BlockState water, int var11, long seed, TernarySurfaceConfig config) {
        BlockPos.Mutable delPos = new BlockPos.Mutable(x, height, z);
        for (int i = 0; i < height; i++) {
            chunk.setBlockState(delPos, Blocks.AIR.getDefaultState(), false);
            delPos.offset(Direction.DOWN);
        }

        BlockPos.Mutable pos = new BlockPos.Mutable(x, 1, z);
        for (int i = 0; i < 80 + (Math.abs(noise) * 8); i++) {
            chunk.setBlockState(pos, stone, false);
            pos.offset(Direction.UP);
        }
        for (int i = 0; i < 3; i++) {
            chunk.setBlockState(pos, Blocks.DIRT.getDefaultState(), false);
            pos.offset(Direction.UP);
        }
        chunk.setBlockState(pos, config.getTopMaterial(), false);
    }
}
