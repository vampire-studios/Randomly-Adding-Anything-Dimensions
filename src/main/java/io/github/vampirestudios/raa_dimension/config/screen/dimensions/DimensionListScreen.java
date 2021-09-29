package io.github.vampirestudios.raa_dimension.config.screen.dimensions;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.vampirestudios.raa_dimension.config.screen.ConfigScreen;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DimensionListScreen extends Screen {

    private static Identifier background;
    Screen parent;
    String tooltip = null;
    private RAADimensionListWidget dimensionList;
    private RAADimensionDescriptionListWidget descriptionList;

    public DimensionListScreen(Screen parent) {
        super(new TranslatableText("config.title.raa.dimension"));
        this.parent = parent;
        background = new Identifier("textures/block/dirt.png");
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256 && this.shouldCloseOnEsc()) {
            client.setScreen(parent);
            return true;
        }
        return super.keyPressed(int_1, int_2, int_3);
    }

    @Override
    protected void init() {
        super.init();
        this.addSelectableChild(new ButtonWidget(4, 4, 50, 20, new TranslatableText("gui.back"), var1 -> client.setScreen(parent)));
        this.addSelectableChild(dimensionList = new RAADimensionListWidget(client, width / 2 - 10, height,
                28 + 5, height - 5, background
        ));
        this.addSelectableChild(descriptionList = new RAADimensionDescriptionListWidget(client, width / 2 - 10, height,
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        tooltip = null;
        renderBackground(matrixStack, 0);
        dimensionList.render(matrixStack, mouseX, mouseY, delta);
        descriptionList.render(matrixStack, mouseX, mouseY, delta);
        ConfigScreen.overlayBackground(0, 0, width, 28, 64, 64, 64, 255, 255);
        ConfigScreen.overlayBackground(0, height - 5, width, height, 64, 64, 64, 255, 255);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value,
                GlStateManager.SrcFactor.ZERO.value, GlStateManager.DstFactor.ONE.value
        );
//        RenderSystem.disableAlphaTest();
//        RenderSystem.shadeModel(7425);
        RenderSystem.disableTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(0, 28 + 4, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
        buffer.vertex(this.width, 28 + 4, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
        buffer.vertex(this.width, 28, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
        buffer.vertex(0, 28, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
        tessellator.draw();
        RenderSystem.enableTexture();
//        RenderSystem.shadeModel(7424);
//        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        drawCenteredText(matrixStack, textRenderer, title, width / 2, 10, 16777215);
        super.render(matrixStack, mouseX, mouseY, delta);
        if (tooltip != null) {
            List<Text> text = new ArrayList<>();
            for (String s : tooltip.split("\n")) {
                text.add(new LiteralText(s));
            }
            renderTooltip(matrixStack, text, mouseX, mouseY);
        }
    }

}