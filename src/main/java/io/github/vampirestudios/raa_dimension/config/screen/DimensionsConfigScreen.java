package io.github.vampirestudios.raa_dimension.config.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.vampirestudios.raa_dimension.config.screen.dimensions.DimensionListScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DimensionsConfigScreen extends Screen {

    private Screen parent;

    public DimensionsConfigScreen(Screen parent) {
        super(Component.translatable("config.title.raaDimensions"));
        this.parent = parent;
    }

    public static void overlayBackground(int x1, int y1, int x2, int y2, int red, int green, int blue, int startAlpha, int endAlpha) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        Minecraft.getInstance().getTextureManager().bindForSetup(GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(x1, y2, 0.0D).uv(x1 / 32.0F, y2 / 32.0F).color(red, green, blue, endAlpha).endVertex();
        buffer.vertex(x2, y2, 0.0D).uv(x2 / 32.0F, y2 / 32.0F).color(red, green, blue, endAlpha).endVertex();
        buffer.vertex(x2, y1, 0.0D).uv(x2 / 32.0F, y1 / 32.0F).color(red, green, blue, startAlpha).endVertex();
        buffer.vertex(x1, y1, 0.0D).uv(x1 / 32.0F, y1 / 32.0F).color(red, green, blue, startAlpha).endVertex();
        tessellator.end();
    }

    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256) {
            assert minecraft != null;
            minecraft.setScreen(parent);
            return true;
        }
        return super.keyPressed(int_1, int_2, int_3);
    }

    @Override
    protected void init() {
        super.init();
        assert minecraft != null;
        addRenderableWidget(new Button(width / 2 - 75, 70, 150, 20, Component.translatable("config.button.raa.dimensionConfiguration"), var1 ->
                minecraft.setScreen(new DimensionListScreen(this))));
//        addButton(new ButtonWidget(width / 2 - 75, 100, 150, 20, I18n.translate("config.button.raa.dimensionMaterialConfiguration"), var1 ->
//                client.openScreen(new DimensionMaterialListScreen(this))));
        addRenderableWidget(new Button(4, 4, 50, 20, Component.translatable("gui.back"), var1 -> minecraft.setScreen(parent)));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        overlayBackground(0, 0, width, height, 32, 32, 32, 255, 255);
        overlayBackground(0, 0, width, 28, 64, 64, 64, 255, 255);
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
        drawCenteredString(matrices, font, title, width / 2, 10, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

}