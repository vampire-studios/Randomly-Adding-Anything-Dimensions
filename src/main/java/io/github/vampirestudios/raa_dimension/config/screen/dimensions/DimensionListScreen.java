package io.github.vampirestudios.raa_dimension.config.screen.dimensions;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.vampirestudios.raa_dimension.config.screen.ConfigScreen;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DimensionListScreen extends Screen {

    private static ResourceLocation background;
    Screen parent;
    String tooltip = null;
    private RAADimensionListWidget dimensionList;
    private RAADimensionDescriptionListWidget descriptionList;

    public DimensionListScreen(Screen parent) {
        super(Component.translatable("config.title.raa.dimension"));
        this.parent = parent;
        background = new ResourceLocation("textures/block/dirt.png");
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256 && this.shouldCloseOnEsc()) {
            minecraft.setScreen(parent);
            return true;
        }
        return super.keyPressed(int_1, int_2, int_3);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new Button(4, 4, 50, 20, Component.translatable("gui.back"), var1 -> minecraft.setScreen(parent)));
        addRenderableWidget(dimensionList = new RAADimensionListWidget(minecraft, width / 2 - 10, height,
                28 + 5, height - 5, background
        ));
        addRenderableWidget(descriptionList = new RAADimensionDescriptionListWidget(minecraft, width / 2 - 10, height,
                28 + 5, height - 5, background
        ));
        dimensionList.setLeftPos(5);
        descriptionList.setLeftPos(width / 2 + 5);
        List<DimensionData> materials = new ArrayList<>();
        for (DimensionData material : Dimensions.DIMENSIONS) materials.add(material);
        materials.sort(Comparator.comparing(material -> WordUtils.capitalizeFully(material.getName()), String::compareToIgnoreCase));
        for (DimensionData material : materials) {
            dimensionList.addItem(new RAADimensionListWidget.DimensionEntry(material) {
                @Override
                public void onClick() {
                    descriptionList.addMaterial(DimensionListScreen.this, material);
                }

                @Override
                public boolean isSelected(DimensionData material) {
                    return descriptionList.data == material;
                }
            });
        }
        if (!materials.isEmpty()) dimensionList.addItem(new RAADimensionListWidget.EmptyEntry(10));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float delta) {
        tooltip = null;
        renderBackground(matrixStack, 0);
        dimensionList.render(matrixStack, mouseX, mouseY, delta);
        descriptionList.render(matrixStack, mouseX, mouseY, delta);
        ConfigScreen.overlayBackground(0, 0, width, 28, 64, 64, 64, 255, 255);
        ConfigScreen.overlayBackground(0, height - 5, width, height, 64, 64, 64, 255, 255);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value,
                GlStateManager.SourceFactor.ZERO.value, GlStateManager.DestFactor.ONE.value
        );
//        RenderSystem.disableAlphaTest();
//        RenderSystem.shadeModel(7425);
        RenderSystem.disableTexture();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(0, 28 + 4, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 0).endVertex();
        buffer.vertex(this.width, 28 + 4, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 0).endVertex();
        buffer.vertex(this.width, 28, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
        buffer.vertex(0, 28, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
        tessellator.end();
        RenderSystem.enableTexture();
//        RenderSystem.shadeModel(7424);
//        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        drawCenteredString(matrixStack, font, title, width / 2, 10, 16777215);
        super.render(matrixStack, mouseX, mouseY, delta);
        if (tooltip != null) {
            List<Component> text = new ArrayList<>();
            for (String s : tooltip.split("\n")) {
                text.add(Component.literal(s));
            }
            renderComponentTooltip(matrixStack, text, mouseX, mouseY);
        }
    }

}