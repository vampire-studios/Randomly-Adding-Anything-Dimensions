package io.github.vampirestudios.raa_dimension.generation.feature.portalHub;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PortalShrineFeatureConfig implements FeatureConfiguration {
   public static final Codec<PortalShrineFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
      return instance.group(Codec.INT.fieldOf("amount").forGetter((basaltColumnsFeatureConfig) -> {
         return basaltColumnsFeatureConfig.amount;
      }), Codec.INT.fieldOf("variant").forGetter((basaltColumnsFeatureConfig) -> {
         return basaltColumnsFeatureConfig.variant;
      })).apply(instance, PortalShrineFeatureConfig::new);
   });
   private final int amount;
   private final int variant;

   public PortalShrineFeatureConfig(int amount, int variant) {
      this.amount = amount;
      this.variant = variant;
   }

   public int getAmount() {
      return amount;
   }

   public int getVariant() {
      return variant;
   }
}
