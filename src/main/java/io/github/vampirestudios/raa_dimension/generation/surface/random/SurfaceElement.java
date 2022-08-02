package io.github.vampirestudios.raa_dimension.generation.surface.random;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;

public abstract class SurfaceElement {
    public abstract void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks);

    public abstract void serialize(JsonObject obj);
    public abstract void deserialize(JsonObject obj);

    public abstract ResourceLocation getType();

    public abstract int getPriority();

    //Utilities

    //sets the top layers to stone so the element can have an effect
    //TODO: fix the weird bug with this method
    protected void resetTop(ChunkAccess chunk, int x, int z, int height, BlockState defaultBlock, BlockState defaultFluid) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, height, z);

        //exit if the bottom block is water/lava
        if (chunk.getBlockState(pos.immutable().below()) == defaultFluid) return;

        while (chunk.getBlockState(pos) != defaultBlock) { //make sure the position is for replacement
            pos.setY(pos.getY() - 1);

            if (chunk.getBlockState(pos) == defaultFluid) return; //exit

            chunk.setBlockState(pos, defaultBlock, false);
        }
    }
}