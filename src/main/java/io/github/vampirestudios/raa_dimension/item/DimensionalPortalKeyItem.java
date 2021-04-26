package io.github.vampirestudios.raa_dimension.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

public class DimensionalPortalKeyItem extends Item {

    public DimensionalPortalKeyItem() {
        super(new Item.Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE));
    }

}