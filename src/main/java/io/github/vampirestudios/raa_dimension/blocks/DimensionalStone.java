package io.github.vampirestudios.raa_dimension.blocks;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

public class DimensionalStone extends Block {
    private final DimensionData dimensionData;

    public DimensionalStone(DimensionData dimensionData) {
        super(Properties.copy(Blocks.STONE).strength(dimensionData.getStoneHardness(), dimensionData.getStoneResistance()));
        this.dimensionData = dimensionData;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Registry.BLOCK.get(new ResourceLocation(dimensionData.getId().getNamespace(), dimensionData.getId().getPath() + "_cobblestone")).asItem()));
        return list;
    }
}
