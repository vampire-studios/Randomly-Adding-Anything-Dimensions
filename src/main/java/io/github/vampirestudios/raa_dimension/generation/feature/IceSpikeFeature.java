package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class IceSpikeFeature extends Feature<NoneFeatureConfiguration> {

   public IceSpikeFeature(Codec<NoneFeatureConfiguration> configDeserializer) {
      super(configDeserializer);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      BlockPos blockPos = context.origin();
      WorldGenLevel iWorld = context.level();
      RandomSource random = context.random();
      while(iWorld.isEmptyBlock(blockPos) && blockPos.getY() > 2) {
         blockPos = blockPos.below();
      }

      blockPos = blockPos.above(random.nextInt(4));
      int i = random.nextInt(4) + 7;
      int j = i / 4 + random.nextInt(2);
      if (j > 1 && random.nextInt(60) == 0) {
         blockPos = blockPos.above(10 + random.nextInt(30));
      }

      int k;
      int l;
      for(k = 0; k < i; ++k) {
         float f = (1.0F - (float)k / (float)i) * (float)j;
         l = Mth.ceil(f);

         for(int m = -l; m <= l; ++m) {
            float g = (float)Mth.abs(m) - 0.25F;

            for(int n = -l; n <= l; ++n) {
               float h = (float)Mth.abs(n) - 0.25F;
               if ((m == 0 && n == 0 || g * g + h * h <= f * f) && (m != -l && m != l && n != -l && n != l || random.nextFloat() <= 0.75F)) {
                  BlockState blockState = iWorld.getBlockState(blockPos.offset(m, k, n));
                  Block block = blockState.getBlock();
                  if (blockState.isAir() || isDirt(block.defaultBlockState())) {
                     this.setBlock(iWorld, blockPos.offset(m, k, n), Blocks.PACKED_ICE.defaultBlockState());
                  }

                  if (k != 0 && l > 1) {
                     blockState = iWorld.getBlockState(blockPos.offset(m, -k, n));
                     block = blockState.getBlock();
                     if (blockState.isAir() || isDirt(block.defaultBlockState())) {
                        this.setBlock(iWorld, blockPos.offset(m, -k, n), Blocks.PACKED_ICE.defaultBlockState());
                     }
                  }
               }
            }
         }
      }

      k = j - 1;
      if (k < 0) {
         k = 0;
      } else if (k > 1) {
         k = 1;
      }

      for(int p = -k; p <= k; ++p) {
         for(l = -k; l <= k; ++l) {
            BlockPos blockPos2 = blockPos.offset(p, -1, l);
            int r = 50;
            if (Math.abs(p) == 1 && Math.abs(l) == 1) {
               r = random.nextInt(5);
            }

            while(blockPos2.getY() > 50) {
               this.setBlock(iWorld, blockPos2, Blocks.PACKED_ICE.defaultBlockState());
               blockPos2 = blockPos2.below();
               --r;
               if (r <= 0) {
                  blockPos2 = blockPos2.below(random.nextInt(5) + 1);
                  r = random.nextInt(5);
               }
            }
         }
      }

      return true;
   }
}
