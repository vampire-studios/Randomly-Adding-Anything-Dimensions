package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;

public class FloatingIslandSurfaceElement extends SurfaceElement {

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks) {
        if (noise > 1) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 50 + height + (noise), z);
            for (int i = 0; i < 2 + (noise / 4); i++) {
                chunk.setBlockState(pos, defaultBlock, false);
                pos.relative(Direction.UP);
            }
            for (int i = 0; i < 3 + (noise / 2); i++) {
                chunk.setBlockState(pos, surfaceBlocks.getUnderMaterial(), false);
                pos.relative(Direction.UP);
            }
            chunk.setBlockState(pos, surfaceBlocks.getTopMaterial(), false);
        }
        SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownNumber, seed, surfaceBlocks);
    }

    @Override
    public void serialize(JsonObject obj) { }

    @Override
    public void deserialize(JsonObject obj) {}

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("raa_dimensions", "floating_island");
    }

    @Override
    public int getPriority() {
        return 100;
    }

}
