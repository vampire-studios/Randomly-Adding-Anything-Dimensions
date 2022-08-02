package io.github.vampirestudios.raa_dimension.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;

public class StairsBaseBlock extends StairBlock {
    public StairsBaseBlock(Block block) {
        super(block.defaultBlockState(), Properties.copy(block));
    }
}