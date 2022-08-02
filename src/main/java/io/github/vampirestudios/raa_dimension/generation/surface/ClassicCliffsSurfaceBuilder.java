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

//Code kindly taken from Terraform. Thank you, coderbot, Prospector, and Valoeghese!
public class ClassicCliffsSurfaceBuilder extends SurfaceSystem<TernarySurfaceConfig> {

    public ClassicCliffsSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random random, ChunkAccess chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int unknownValue, long seed, TernarySurfaceConfig config) {
        x &= 15;
        z &= 15;
        height -= 1;
        if (noise > 0.5D && height > seaLevel + 1 && height < seaLevel + 12) {
            if (height > seaLevel + 5) {
                height = seaLevel + 5;
            }
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, height, z);
            int basaltLayers = 3;
            if (noise > 1.0D) {
                basaltLayers += 1;
            }
            if (noise > 1.5D) {
                basaltLayers += 1;
            }
            for (int i = 0; i < basaltLayers; i++) {
                chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
                pos.relative(Direction.UP);
            }
            for (int i = 0; i < 3; i++) {
                chunk.setBlockState(pos, config.getUnderMaterial(), false);
                pos.relative(Direction.UP);
            }
            chunk.setBlockState(pos, config.getTopMaterial(), false);
        } else {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, unknownValue, seed, config);
        }
    }

}