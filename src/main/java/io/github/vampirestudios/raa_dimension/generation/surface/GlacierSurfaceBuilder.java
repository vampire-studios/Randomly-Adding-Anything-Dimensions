package io.github.vampirestudios.raa_dimension.generation.surface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import java.util.Random;
import java.util.function.DoubleFunction;

//Code kindly taken from Terrestria. Thank you, coderbot, Prospector, and Valoeghese!
public class GlacierSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {
    private static SimplexNoise noiseGenerator;
    protected final BlockState WATER = Blocks.WATER.defaultBlockState();
    protected final BlockState SAND = Blocks.SAND.defaultBlockState();
    protected final BlockState pICE = Blocks.PACKED_ICE.defaultBlockState();
    protected final BlockState bICE = Blocks.BLUE_ICE.defaultBlockState();
    private long currentSeed = 0L;
    private DoubleFunction<TernarySurfaceConfig> configProvider;

    public GlacierSurfaceBuilder(DoubleFunction<TernarySurfaceConfig> config) {
        super(TernarySurfaceConfig.CODEC);
        configProvider = config;
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig surfaceConfig) {
        TernarySurfaceConfig config = configProvider.apply(noise);

        if (noiseGenerator == null || seed != currentSeed) {
            noiseGenerator = new SimplexNoise(new ChunkRandom(seed));
            currentSeed = seed;
        }

        double sample = noiseGenerator.getValue((double) x / 260D, (double) z / 260D);
        int glacierDifference = (int) ((sample > 0.1D && sample < 0.5D) ? (1171875 * Math.pow(sample - 0.26, 4)) - 3 : 0);

        int localX = x & 15;
        int localZ = z & 15;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(localX, chunk.getMaxBuildHeight() - 1, localZ);

        if (glacierDifference != 0) {
            pos.setY(height);
            chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);

            pos.setY(height - 1);
            chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);

            height -= 2;
        }

        for (int y = height; y >= 0; --y) {
            pos.setY(y);
            BlockState toSet = Blocks.STONE.defaultBlockState();

            if (y - height > glacierDifference) {
                toSet = (y - height == -1) ? bICE : pICE;
            } else {
                if (y < chunk.getMaxBuildHeight()-1) {
                    BlockState upState = chunk.getBlockState(pos.above());
                    if (upState == Blocks.AIR.defaultBlockState()) {
                        toSet = config.getTopMaterial();
                    } else if (upState == WATER) {
                        toSet = config.getUnderwaterMaterial();
                    } else {
                        if (y < chunk.getMaxBuildHeight()-3) {
                            if (chunk.getBlockState(pos.above(3)) == Blocks.AIR.defaultBlockState() || chunk.getBlockState(pos.above(2)) == Blocks.AIR.defaultBlockState()) {
                                toSet = config.getUnderMaterial();
                            }
                        }
                    }
                }
            }

            chunk.setBlockState(pos, toSet, false);
        }
    }
}