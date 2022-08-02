package io.github.vampirestudios.raa_dimension.generation.surface.random.elements;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceElement;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;

public class RandomSpiresSurfaceElement extends SurfaceElement {
    private int spireChance;
    private int spireHeight;

    public RandomSpiresSurfaceElement() {
        spireChance = Rands.randIntRange(32, 256);
        spireHeight = Rands.randIntRange(3, 16);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownNumber, long seed, TernarySurfaceConfig surfaceBlocks) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, height, z);

        //don't place on water
        if (chunk.getBlockState(pos.immutable().below()) == defaultFluid) return;

        //don't place on air/void
        if (chunk.getBlockState(pos.immutable().below()).isAir()) return;

        if (random.nextInt(spireChance) == 0) {
            int spireHeightRandom = random.nextInt(spireHeight);
            for (int i = 0; i < spireHeightRandom; i++) {
                pos.setY(height + i);
                chunk.setBlockState(pos, defaultBlock, false);
            }

            //generate the top layer of the spire
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height + spireHeightRandom + 1, noise, defaultBlock, defaultFluid, seaLevel, unknownNumber, seed, surfaceBlocks);
        }
    }

    @Override
    public void serialize(JsonObject obj) {
        obj.addProperty("spireChance", spireChance);
        obj.addProperty("spireHeight", spireHeight);
    }

    @Override
    public void deserialize(JsonObject obj) {
        spireChance = obj.get("spireChance").getAsInt();
        spireHeight = obj.get("spireHeight").getAsInt();
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("raa_dimensions", "spires");
    }

    @Override
    public int getPriority() {
        return 90;
    }
}
