package io.github.vampirestudios.raa_dimension.blocks;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.ArrayList;
import java.util.List;

public class DimensionalBlock extends Block {

    public DimensionalBlock(DimensionData dimensionData) {
        super(Block.Settings.copy(Blocks.STONE).strength(/*Rands.randFloatRange(0.25f, 4)*/dimensionData.getStoneHardness(), /*Rands.randFloatRange(4, 20)*/dimensionData.getStoneResistance()));
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(state.getBlock().asItem()));
        return list;
    }
}
