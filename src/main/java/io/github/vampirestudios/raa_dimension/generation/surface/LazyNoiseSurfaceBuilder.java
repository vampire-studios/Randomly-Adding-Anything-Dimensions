package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import java.util.Random;

public class LazyNoiseSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {
    public LazyNoiseSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        BlockPos.MutableBlockPos delPos = new BlockPos.MutableBlockPos(x, height, z);
        for (int i = 0; i < height; i++) {
            chunk.setBlockState(delPos, Blocks.AIR.defaultBlockState(), false);
            delPos.relative(Direction.DOWN);
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 1, z);
        for (int i = 0; i < 80 + (Math.abs(noise) * 8); i++) {
            chunk.setBlockState(pos, defaultBlock, false);
            pos.relative(Direction.UP);
        }
        for (int i = 0; i < 3; i++) {
            chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
            pos.relative(Direction.UP);
        }
        chunk.setBlockState(pos, surfaceConfig.getTopMaterial(), false);
    }
}
