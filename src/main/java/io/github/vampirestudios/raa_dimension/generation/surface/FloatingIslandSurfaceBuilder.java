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

public class FloatingIslandSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public FloatingIslandSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceBlocks) {
        if (noise > 1) {
            BlockPos.Mutable pos = new BlockPos.Mutable(x, 50 + height + (noise), z);
            for (int i = 0; i < 2 + (noise / 4); i++) {
                chunk.setBlockState(pos, defaultBlock, false);
                pos.offset(Direction.UP);
            }
            for (int i = 0; i < 3 + (noise / 2); i++) {
                chunk.setBlockState(pos, Blocks.DIRT.getDefaultState(), false);
                pos.offset(Direction.UP);
            }
            chunk.setBlockState(pos, surfaceBlocks.getTopMaterial(), false);
        }
        SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, surfaceBlocks);
    }
}
