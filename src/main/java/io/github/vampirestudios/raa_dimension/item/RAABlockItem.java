package io.github.vampirestudios.raa_dimension.item;

import com.ibm.icu.text.MessageFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.text.WordUtils;

public class RAABlockItem extends BlockItem {
    private String name;
    private BlockType blockType;

    public RAABlockItem(String name, Block block_1, Properties item$Settings_1, BlockType blockType) {
        super(block_1, item$Settings_1);
        this.name = name;
        this.blockType = blockType;
    }

    @Override
    public Component getDescription() {
        return super.getDescription();
    }

    @Override
    public Component getName(ItemStack itemStack_1) {
        MessageFormat format = new MessageFormat(Component.translatable("text.raa_dimensions.block." + getBlockType().name().toLowerCase()).getString());
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return Component.literal(format.format(data));
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public enum BlockType {
        ORE("_ore"),
        BLOCK("_block");

        private String suffix;

        BlockType(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }
    }
}
