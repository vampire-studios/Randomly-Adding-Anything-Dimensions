package io.github.vampirestudios.raa_dimension.item;

import com.ibm.icu.text.MessageFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.text.WordUtils;

public class RAABlockItemAlt extends BlockItem {
    private String name, type;

    public RAABlockItemAlt(String name, String type, Block block_1, Properties item$Settings_1) {
        super(block_1, item$Settings_1);
        this.name = name;
        this.type = type;
    }

    @Override
    public Component getDescription() {
        return super.getDescription();
    }

    @Override
    public Component getName(ItemStack itemStack_1) {
        MessageFormat format = new MessageFormat(Component.translatable("text.raa_dimensions.block." + type).getString());
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return Component.literal(format.format(data));
    }

}