package io.github.vampirestudios.raa_dimension.config.screen.dimensions;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.Rect2i;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.List;

public class RAADimensionListWidget extends DynamicElementListWidget<RAADimensionListWidget.Entry> {
    public RAADimensionListWidget(MinecraftClient client, int width, int height, int top, int bottom, Identifier backgroundLocation) {
        super(client, width, height, top, bottom, backgroundLocation);
    }

    @Override
    public int getItemWidth() {
        return width - 11;
    }

    @Override
    protected int getScrollbarPosition() {
        return left + width - 6;
    }

    @Override
    public int addItem(Entry item) {
        return super.addItem(item);
    }

    public abstract static class DimensionEntry extends Entry {
        private PackWidget widget;
        private DimensionData material;

        public DimensionEntry(DimensionData material) {
            this.widget = new PackWidget();
            this.material = material;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            widget.bounds = new Rect2i(x, y, entryWidth, getItemHeight());
            widget.render(matrixStack, mouseX, mouseY, delta);
        }

        @Override
        public int getItemHeight() {
            return 12;
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(widget);
        }

        public abstract void onClick();

        public abstract boolean isSelected(DimensionData material);

        public class PackWidget implements Element, Drawable {
            private Rect2i bounds;
            private boolean focused;

            @Override
            public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
//                RenderSystem.disableAlphaTest();
                fill(matrixStack, bounds.getX(), bounds.getY(), bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(), 0x15FFFFFF);
                boolean isHovered = focused || bounds.contains(mouseX, mouseY);
                drawTextWithShadow(matrixStack, MinecraftClient.getInstance().textRenderer, new LiteralText((isHovered ? Formatting.UNDERLINE.toString() : "") + (isSelected(material) ? Formatting.BOLD.toString() : "") + WordUtils.capitalizeFully(material.getName())),
                        bounds.getX() + 5, bounds.getY() + 6, 16777215
                );
            }

            @Override
            public boolean mouseClicked(double double_1, double double_2, int int_1) {
                if (int_1 == 0) {
                    boolean boolean_1 = bounds.contains((int) double_1, (int) double_2);
                    if (boolean_1) {
                        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        onClick();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean keyPressed(int int_1, int int_2, int int_3) {
                if (int_1 != 257 && int_1 != 32 && int_1 != 335) {
                    return false;
                } else {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    onClick();
                    return true;
                }
            }

            @Override
            public boolean changeFocus(boolean boolean_1) {
                this.focused = !this.focused;
                return this.focused;
            }

        }
    }

    public static class EmptyEntry extends Entry {
        private int height;

        public EmptyEntry(int height) {
            this.height = height;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {

        }

        @Override
        public int getItemHeight() {
            return height;
        }

        @Override
        public List<? extends Element> children() {
            return Collections.emptyList();
        }
    }

    public static abstract class Entry extends DynamicElementListWidget.ElementEntry<Entry> {

    }

}