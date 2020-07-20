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

//Code kindly taken from Terraform. Thank you, coderbot, Prospector, and Valoeghese!
public class ClassicCliffsSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {

    public ClassicCliffsSurfaceBuilder(Codec<TernarySurfaceConfig> ternarySurfaceConfigCodec) {
        super(ternarySurfaceConfigCodec);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState stone, BlockState water, int seaLevel, long seed, TernarySurfaceConfig config) {
        x &= 15;
        z &= 15;
        height -= 1;
        if (noise > 0.5D && height > seaLevel + 1 && height < seaLevel + 12) {
            if (height > seaLevel + 5) {
                height = seaLevel + 5;
            }
            BlockPos.Mutable pos = new BlockPos.Mutable(x, height, z);
            int basaltLayers = 3;
            if (noise > 1.0D) {
                basaltLayers += 1;
            }
            if (noise > 1.5D) {
                basaltLayers += 1;
            }
            for (int i = 0; i < basaltLayers; i++) {
                chunk.setBlockState(pos, Blocks.DIRT.getDefaultState(), false);
                pos.offset(Direction.UP);
            }
            for (int i = 0; i < 3; i++) {
                chunk.setBlockState(pos, config.getUnderMaterial(), false);
                pos.offset(Direction.UP);
            }
            chunk.setBlockState(pos, config.getTopMaterial(), false);
        } else {
            SurfaceBuilder.DEFAULT.generate(rand, chunk, biome, x, z, height, noise, stone, water, seaLevel, seed, config);
        }
    }

}