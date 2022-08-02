package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class NetherrackFeature extends Feature<NoneFeatureConfiguration> {
    public NetherrackFeature(Codec<NoneFeatureConfiguration> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        if (world.getBlockState(pos.offset(0, -1, 0)).isAir() || !world.getBlockState(pos.offset(0, -1, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;
        this.setBlock(world, pos.offset(0, -1, 0), Blocks.NETHERRACK.defaultBlockState());
        if (Rands.chance(3)) {
            this.setBlock(world, pos, Blocks.FIRE.defaultBlockState());
        }
        return true;
    }
}
