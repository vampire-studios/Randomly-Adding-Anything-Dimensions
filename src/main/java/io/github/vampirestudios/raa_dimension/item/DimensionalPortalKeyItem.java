package io.github.vampirestudios.raa_dimension.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class DimensionalPortalKeyItem extends Item {

    public DimensionalPortalKeyItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).rarity(Rarity.RARE));
    }

}