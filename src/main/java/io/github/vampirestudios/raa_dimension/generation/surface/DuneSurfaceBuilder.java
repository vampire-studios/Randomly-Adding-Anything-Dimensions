package io.github.vampirestudios.raa_dimension.generation.surface;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.utils.WorleyNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import java.util.Random;

//Code kindly taken from Terrestria. Thank you, coderbot, Prospector, and Valoeghese!
public class DuneSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {

    private static final WorleyNoise NOISE = new WorleyNoise(3445);

    public DuneSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int vHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig config) {
        vHeight = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(x & 15, z & 15);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, vHeight - 8, z);

        double blend = Mth.clamp((vHeight - seaLevel) * 0.125, 0, 1);

        double height = (NOISE.sample(x * 0.01, z * 0.015) * 30) * blend;

        height = Math.abs(height);

        for (int i = 0; i < 8; i++) {
            chunk.setBlockState(pos, defaultBlock, false);
            pos.relative(Direction.UP);
        }

        // Cap the height based on noise

        height = Math.min(height, (NOISE.sample(x * 0.03 + 5, z * 0.05 + 5, 0) * 30 + 6));

        for (int h = 0; h < height; h++) {
            chunk.setBlockState(pos, defaultBlock, false);
            pos.relative(Direction.UP);
        }

        for (int i = 0; i < 3 + (noise / 2); i++) {
            chunk.setBlockState(pos, Blocks.SANDSTONE.defaultBlockState(), false);
            pos.relative(Direction.UP);
        }
        chunk.setBlockState(pos, config.getTopMaterial(), false);
    }
}