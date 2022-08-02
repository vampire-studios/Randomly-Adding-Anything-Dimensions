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

public class StratifiedSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {
    public StratifiedSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        if (noise > 1.5) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, height, z);
            BlockPos.MutableBlockPos posTilHeight = new BlockPos.MutableBlockPos(x, 0, z);
            for (int i = 0; i <= height; i++) {
                chunk.setBlockState(posTilHeight, defaultBlock, false);
                posTilHeight.relative(Direction.UP);
            }
            for (int i = 0; i < (noise > 2.5 ? 20 : 12); i++) {
                chunk.setBlockState(pos, defaultBlock, false);
                pos.relative(Direction.UP);
            }
            int dirtHeight = (int) noise * 2;
            for (int i = 0; i < dirtHeight; i++) {
                chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
                pos.relative(Direction.UP);
            }
            chunk.setBlockState(pos, surfaceConfig.getTopMaterial(), false);
        } else {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, surfaceConfig);
        }
    }
}
