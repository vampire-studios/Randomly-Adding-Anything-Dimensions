package io.github.vampirestudios.raa_dimension.blocks;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class DimensionalBlock extends Block {

    public DimensionalBlock(DimensionData dimensionData) {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).strength(/*Rands.randFloatRange(0.25f, 4)*/dimensionData.getStoneHardness(), /*Rands.randFloatRange(4, 20)*/dimensionData.getStoneResistance()));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(state.getBlock().asItem()));
        return list;
    }
}
