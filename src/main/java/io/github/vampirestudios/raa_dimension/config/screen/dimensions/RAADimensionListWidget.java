package io.github.vampirestudios.raa_dimension.config.screen.dimensions;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.List;

public class RAADimensionListWidget extends DynamicElementListWidget<RAADimensionListWidget.Entry> {
    public RAADimensionListWidget(Minecraft client, int width, int height, int top, int bottom, ResourceLocation backgroundLocation) {
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
    public int addItem(io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionListWidget.Entry item) {
        return super.addItem(item);
    }

    public abstract static class DimensionEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionListWidget.Entry {
        private PackWidget widget;
        private DimensionData material;

        public DimensionEntry(DimensionData material) {
            this.widget = new PackWidget();
            this.material = material;
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            widget.bounds = new Rect2i(x, y, entryWidth, getItemHeight());
            widget.render(matrixStack, mouseX, mouseY, delta);
        }

        @Override
        public int getItemHeight() {
            return 12;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }

        public abstract void onClick();

        public abstract boolean isSelected(DimensionData material);

        public class PackWidget implements GuiEventListener, Widget {
            private Rect2i bounds;
            private boolean focused;

            @Override
            public void render(PoseStack matrixStack, int mouseX, int mouseY, float delta) {
//                RenderSystem.disableAlphaTest();
                fill(matrixStack, bounds.getX(), bounds.getY(), bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(), 0x15FFFFFF);
                boolean isHovered = focused || bounds.contains(mouseX, mouseY);
                drawString(matrixStack, Minecraft.getInstance().font, Component.literal((isHovered ? ChatFormatting.UNDERLINE.toString() : "") + (isSelected(material) ? ChatFormatting.BOLD.toString() : "") + WordUtils.capitalizeFully(material.getName())),
                        bounds.getX() + 5, bounds.getY() + 6, 16777215
                );
            }

            @Override
            public boolean mouseClicked(double double_1, double double_2, int int_1) {
                if (int_1 == 0) {
                    boolean boolean_1 = bounds.contains((int) double_1, (int) double_2);
                    if (boolean_1) {
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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

    public static class EmptyEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionListWidget.Entry {
        private int height;

        public EmptyEntry(int height) {
            this.height = height;
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {

        }

        @Override
        public int getItemHeight() {
            return height;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }
    }

    public static abstract class Entry extends DynamicElementListWidget.ElementEntry<io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionListWidget.Entry> {

    }

}