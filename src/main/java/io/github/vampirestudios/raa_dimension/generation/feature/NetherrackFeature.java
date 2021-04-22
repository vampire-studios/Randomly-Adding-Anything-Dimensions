package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class NetherrackFeature extends Feature<DefaultFeatureConfig> {
    public NetherrackFeature(Codec<DefaultFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        if (world.getBlockState(pos.add(0, -1, 0)).isAir() || !world.getBlockState(pos.add(0, -1, 0)).isOpaque() || world.getBlockState(pos.add(0, -1, 0)).equals(Blocks.BEDROCK.getDefaultState()))
            return true;
        this.setBlockState(world, pos.add(0, -1, 0), Blocks.NETHERRACK.getDefaultState());
        if (Rands.chance(3)) {
            this.setBlockState(world, pos, Blocks.FIRE.getDefaultState());
        }
        return true;
    }
}
