package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import io.github.vampirestudios.raa_dimension.utils.WorleyNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Random;

public class DunesSurfaceElement extends SurfaceElement {
    private static final WorleyNoise NOISE = new WorleyNoise(3445);

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks) {
        height = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(x & 15, z & 15);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, height - 8, z);

        double blend = Mth.clamp((height - seaLevel) * 0.125, 0, 1);

        double vHeight = (NOISE.sample(x * 0.01, z * 0.015) * 30) * blend;

        vHeight = Math.abs(vHeight);

        for (int i = 0; i < 8; i++) {
            chunk.setBlockState(pos, defaultBlock, false);
            pos.relative(Direction.UP);
        }

        // Cap the height based on noise

        vHeight = Math.min(vHeight, (NOISE.sample(x * 0.03 + 5, z * 0.05 + 5, 0) * 30 + 6));

        for (int h = 0; h < vHeight; h++) {
            chunk.setBlockState(pos, defaultBlock, false);
            pos.relative(Direction.UP);
        }

        /*for (int i = 0; i < 3 + (noise / 2); i++) {
            chunk.setBlockState(pos, Blocks.SANDSTONE.getDefaultState(), false);
            pos.offset(Direction.UP);
        }*/
        chunk.setBlockState(pos, surfaceBlocks.getTopMaterial(), false);
    }

    @Override
    public void serialize(JsonObject obj) {}

    @Override
    public void deserialize(JsonObject obj) {}

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("raa_dimensions", "dunes");
    }

    @Override
    public int getPriority() {
        return 100;
    }

}
