package io.github.vampirestudios.raa_dimension.config.screen.dimensions;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionColorPalette;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RAADimensionDescriptionListWidget extends DynamicElementListWidget<RAADimensionDescriptionListWidget.Entry> {

    DimensionData data;

    public RAADimensionDescriptionListWidget(Minecraft client, int width, int height, int top, int bottom, ResourceLocation backgroundLocation) {
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
    public int addItem(io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry item) {
        return super.addItem(item);
    }

    public void clearItemsPublic() {
        clearItems();
    }

    public void addMaterial(DimensionListScreen og, DimensionData dimensionData) {
        this.data = dimensionData;
        clearItems();
        addItem(new TitleMaterialOverrideEntry(og, dimensionData, Component.literal(WordUtils.capitalizeFully(dimensionData.getName())).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
        DimensionColorPalette colorPalette = dimensionData.getDimensionColorPalette();
//        addItem(new TitleEntry(Component.literal(WordUtils.capitalizeFully(dimensionData.getName())).withStyle(Formatting.UNDERLINE, Formatting.BOLD)));
        addItem(new TextEntry(Component.translatable("config.text.raa.identifier", dimensionData.getId().toString())));

        addItem(new TextEntry(Component.translatable("config.text.raa.hasSky", Component.translatable("config.text.raa.boolean.value." + dimensionData.getCustomSkyInformation().hasSky()))));
        addItem(new TextEntry(Component.translatable("config.text.raa.hasSkyLight", Component.translatable("config.text.raa.boolean.value." + dimensionData.getCustomSkyInformation().hasSkyLight()))));
        addItem(new TextEntry(Component.translatable("config.text.raa.canSleep", Component.translatable("config.text.raa.boolean.value." + dimensionData.canSleep()))));
        addItem(new TextEntry(Component.translatable("config.text.raa.waterVaporize", Component.translatable("config.text.raa.boolean.value." + dimensionData.doesWaterVaporize()))));
        addItem(new TextEntry(Component.translatable("config.text.raa.renderFog", Component.translatable("config.text.raa.boolean.value." + dimensionData.hasThickFog()))));

        //determine formatting colors
        //the numbers will have to change when more dangerous dimensions are added
        ChatFormatting difficultyFormatting = ChatFormatting.GREEN;
        if (dimensionData.getDifficulty() > 2) difficultyFormatting = ChatFormatting.YELLOW;
        if (dimensionData.getDifficulty() > 6) difficultyFormatting = ChatFormatting.RED;
        if (dimensionData.getDifficulty() > 10) difficultyFormatting = ChatFormatting.DARK_RED;
        addItem(new TextEntry(Component.translatable("config.text.raa.difficulty", Component.literal(dimensionData.getDifficulty() + "").withStyle(difficultyFormatting))));

        addItem(new TitleEntry(Component.translatable("config.title.raa.advancedInformation").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
//        addItem(new TextEntry(Component.translatable("config.text.raa.chunkGenerator", WordUtils.capitalizeFully(dimensionData.getDimensionChunkGenerator().toString().replace("_", " ").toLowerCase()))));

        if (dimensionData.getFlags() != 0) {
            addItem(new TitleEntry(Component.translatable("config.title.raa.flags").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
            int flags = dimensionData.getFlags();
            if (Utils.checkBitFlag(flags, Utils.LUSH))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.lush").withStyle(ChatFormatting.GREEN), "config.tooltip.raa.lush", og));
            if (Utils.checkBitFlag(flags, Utils.CIVILIZED))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.civilized").withStyle(ChatFormatting.DARK_GREEN), "config.tooltip.raa.civilized", og));
            if (Utils.checkBitFlag(flags, Utils.ABANDONED))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.abandoned").withStyle(ChatFormatting.GRAY), "config.tooltip.raa.abandoned", og));
            if (Utils.checkBitFlag(flags, Utils.DEAD))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.dead").withStyle(ChatFormatting.DARK_GRAY), "config.tooltip.raa.dead", og));
            if (Utils.checkBitFlag(flags, Utils.DRY))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.dry").withStyle(ChatFormatting.YELLOW), "config.tooltip.raa.dry", og));
            if (Utils.checkBitFlag(flags, Utils.TECTONIC))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.tectonic").withStyle(ChatFormatting.DARK_GRAY), "config.tooltip.raa.tectonic", og));
            if (Utils.checkBitFlag(flags, Utils.MOLTEN))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.molten").withStyle(ChatFormatting.YELLOW), "config.tooltip.raa.molten", og));
            if (Utils.checkBitFlag(flags, Utils.CORRUPTED))
                addItem(new TextEntryWithTooltip(Component.translatable("config.text.raa.flags.corrupted").withStyle(ChatFormatting.DARK_RED), "config.tooltip.raa.corrupted", og));
        }

        if (dimensionData.getCivilizationInfluences().size() > 0) {
            addItem(new TitleEntry(Component.translatable("config.title.raa.civs").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
            for (Map.Entry<String, Double> pair : dimensionData.getCivilizationInfluences().entrySet()) {
                if (pair.getValue() != 1.0) {
                    addItem(new TextEntry(Component.translatable("config.text.raa.civs.var", pair.getKey(), new DecimalFormat("##.00").format(pair.getValue() * 100))));
                } else {
                    addItem(new TextEntry(Component.translatable("config.text.raa.civs.var.home", pair.getKey(), new DecimalFormat("##.00").format(pair.getValue() * 100))));
                }
            }
        }

        addItem(new TitleEntry(Component.translatable("config.title.raa.colors").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));

        if (dimensionData.getCustomSkyInformation().hasSky()) {
            addItem(new ColorEntry("config.text.raa.skyColor", colorPalette.getSkyColor()));
        }
        addItem(new ColorEntry("config.text.raa.grassColor", colorPalette.getGrassColor()));
        addItem(new ColorEntry("config.text.raa.fogColor", colorPalette.getFogColor()));
        addItem(new ColorEntry("config.text.raa.foliageColor", colorPalette.getFoliageColor()));
        addItem(new ColorEntry("config.text.raa.stoneColor", colorPalette.getStoneColor()));
        addItem(new ColorEntry("config.text.raa.waterColor", dimensionData.getBiomeData().get(0).getWaterColor()));
    }

    public static class ColorEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry {
        private final String s;
        private final int color;

        public ColorEntry(String s, int color) {
            this.s = s;
            this.color = color;
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            int i = Minecraft.getInstance().font.drawShadow(matrixStack, ChatFormatting.GRAY + I18n.get(s) + ChatFormatting.WHITE + I18n.get("#" + Integer.toHexString(color).replace("ff", "")), x, y, 16777215);
            fillGradient(matrixStack, i + 1, y + 1, i + 1 + entryHeight, y + 1 + entryHeight, color, color);
        }

        @Override
        public int getItemHeight() {
            return 11;
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

    public static class TitleMaterialOverrideEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry {
        protected String s;
        private final Button overrideButton;

        public TitleMaterialOverrideEntry(DimensionListScreen og, DimensionData material, Component text) {
            this.s = text.getString();
            String btnText = I18n.get("config.button.raa.edit");
            overrideButton = new Button(0, 0, Minecraft.getInstance().font.width(btnText) + 10, 20, Component.literal(btnText), widget -> {
                openClothConfigForMaterial(og, material);
            });
        }

        @SuppressWarnings("deprecation")
        private static void openClothConfigForMaterial(DimensionListScreen og, DimensionData material) {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(og)
                    .setTitle(Component.translatable("config.title.raa.config_specific", WordUtils.capitalizeFully(material.getName())));
            ConfigCategory category = builder.getOrCreateCategory(Component.literal("null")); // The name is not required if we only have 1 category in Cloth Config 1.8+
            ConfigEntryBuilder eb = builder.entryBuilder();
            category.addEntry(
                    eb.startStrField(Component.translatable("config.field.raa.identifier"), material.getId().getPath())
                            .setDefaultValue(material.getId().getPath())
                            .setSaveConsumer(material::setId)
                            .setErrorSupplier(str -> {
                                if (str.toLowerCase().equals(str))
                                    return Optional.empty();
                                return Optional.of(Component.translatable("config.error.raa.identifier.no.caps"));
                            })
                            .build()
            );

            //TODO: refactor this with array support
            /*category.addEntry(
                    eb.startStrField("config.field.raa.name", material.getName())
                            .setDefaultValue(material.getName())
                            .setSaveConsumer(material::setName)
                            .build()
            );*/
//            SubCategoryBuilder biomeData = eb.startSubCategory("config.title.raa.biomeData").setExpended(false);
//            biomeData.add(
//                    eb.startStrField("config.field.raa.biomeData.id", material.getBiomeData().getId().getPath())
//                            .setDefaultValue(material.getBiomeData().getId().getPath())
//                            .setSaveConsumer(str -> material.getBiomeData().setId(new Identifier(RandomlyAddingAnything.MOD_ID, str)))
//                            .build()
//            );
//            biomeData.add(
//                    eb.startStrField("config.field.raa.biomeData.name", material.getBiomeData().getName())
//                            .setDefaultValue(material.getBiomeData().getName())
//                            .setSaveConsumer(material.getBiomeData()::setName)
//                            .build()
//            );
//            biomeData.add(
//                    eb.startIntField("config.field.raa.biomeData.surfaceBuilderVariantChance",
//                            material.getBiomeData().getSurfaceBuilderVariantChance())
//                            .setDefaultValue(material.getBiomeData().getSurfaceBuilderVariantChance())
//                            .setSaveConsumer(material.getBiomeData()::setSurfaceBuilderVariantChance)
//                            .build()
//            );
//            biomeData.add(
//                    eb.startFloatField("config.field.raa.biomeData.depth", material.getBiomeData().getDepth())
//                            .setDefaultValue(material.getBiomeData().getDepth())
//                            .setSaveConsumer(material.getBiomeData()::setDepth)
//                            .build()
//            );
//            biomeData.add(
//                    eb.startFloatField("config.field.raa.biomeData.scale", material.getBiomeData().getScale())
//                            .setDefaultValue(material.getBiomeData().getScale())
//                            .setSaveConsumer(material.getBiomeData()::setScale)
//                            .build()
//            );
//            biomeData.add(
//                    eb.startFloatField("config.field.raa.biomeData.temperature", material.getBiomeData().getTemperature())
//                            .setDefaultValue(material.getBiomeData().getTemperature())
//                            .setSaveConsumer(material.getBiomeData()::setTemperature)
//                            .build()
//            );
//            biomeData.add(
//                    eb.startFloatField("config.field.raa.biomeData.downfall", material.getBiomeData().getDownfall())
//                            .setDefaultValue(material.getBiomeData().getDownfall())
//                            .setSaveConsumer(material.getBiomeData()::setDownfall)
//                            .build()
//            );
//            category.addEntry(biomeData.build());

            category.addEntry(
                    eb.startBooleanToggle(Component.translatable("config.field.raa.hasSky"), material.getCustomSkyInformation().hasSky())
                            .setDefaultValue(material.getCustomSkyInformation().hasSky())
                            .setSaveConsumer(material.getCustomSkyInformation()::setHasSky)
                            .build()
            );
            category.addEntry(
                    eb.startBooleanToggle(Component.translatable("config.field.raa.hasSkyLight"), material.getCustomSkyInformation().hasSkyLight())
                            .setDefaultValue(material.getCustomSkyInformation().hasSkyLight())
                            .setSaveConsumer(material.getCustomSkyInformation()::setHasSkyLight)
                            .build()
            );
            // TODO Fix this
//            if (material.hasSky()) {
//                category.addEntry(
//                        eb.startStrField("config.field.raa.skyColor", Integer.toHexString(material.getDimensionColorPalette().getSkyColor()).replaceFirst("ff", ""))
//                                .setDefaultValue(Integer.toHexString(material.getDimensionColorPalette().getSkyColor()).replaceFirst("ff", ""))
//                                .setSaveConsumer(str -> material.getDimensionColorPalette().setSkyColor(Integer.decode("0x" + str)))
//                                .setErrorSupplier(str -> {
//                                    try {
//                                        Integer.decode("0x" + str);
//                                        return Optional.empty();
//                                    } catch (Exception e) {
//                                        return Optional.of(I18n.translate("config.error.raa.invalid.color"));
//                                    }
//                                })
//                                .build()
//                );
//            }
            category.addEntry(
                    eb.startBooleanToggle(Component.translatable("config.field.raa.canSleep"), material.canSleep())
                            .setDefaultValue(material.canSleep())
                            .setSaveConsumer(material::setCanSleep)
                            .build()
            );
            category.addEntry(
                    eb.startBooleanToggle(Component.translatable("config.field.raa.doesWaterVaporize"), material.doesWaterVaporize())
                            .setDefaultValue(material.doesWaterVaporize())
                            .setSaveConsumer(material::setWaterVaporize)
                            .build()
            );
            category.addEntry(
                    eb.startBooleanToggle(Component.translatable("config.field.raa.shouldRenderFog"), material.hasThickFog())
                            .setDefaultValue(material.hasThickFog())
                            .setSaveConsumer(material::setRenderFog)
                            .build()
            );
            builder.setSavingRunnable(RAADimensionAddon.DIMENSIONS_CONFIG::overrideFile);
            Minecraft.getInstance().setScreen(builder.build());
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            matrixStack.pushPose();
            matrixStack.scale(1.4F, 1.4F, 1.4F);
            Minecraft.getInstance().font.drawShadow(matrixStack, s, x / 1.4f, (y + 5) / 1.4f, 16777215);
            matrixStack.popPose();
            overrideButton.x = x + entryWidth - overrideButton.getWidth();
            overrideButton.y = y;
            overrideButton.render(matrixStack, mouseX, mouseY, delta);
        }

        @Override
        public int getItemHeight() {
            return 21;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(overrideButton);
        }
    }

    public static class TextEntryWithTooltip extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry {
        protected String s;
        protected String tooltip;
        protected DimensionListScreen screen;

        public TextEntryWithTooltip(Component text, String tooltip, DimensionListScreen screen) {
            this.s = text.getString();
            this.tooltip = I18n.exists(tooltip) ? I18n.get(tooltip) : null;
            this.screen = screen;
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            Minecraft.getInstance().font.drawShadow(matrixStack, s, x, y, 16777215);
            if (tooltip != null && mouseX >= x && mouseY >= y && mouseX <= x + Minecraft.getInstance().font.width(s) && mouseY <= y + getItemHeight())
                screen.tooltip = tooltip;
        }

        @Override
        public int getItemHeight() {
            return 11;
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

    public static class TextEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry {
        protected String s;

        public TextEntry(Component text) {
            this.s = text.getString();
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            Minecraft.getInstance().font.drawShadow(matrixStack, s, x, y, 16777215);
        }

        @Override
        public int getItemHeight() {
            return 11;
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

    public static class TitleEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry {
        protected String s;

        public TitleEntry(String s) {
            this.s = s;
        }

        public TitleEntry(Component text) {
            this.s = text.getString();
        }

        @Override
        public void render(PoseStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            Minecraft.getInstance().font.drawShadow(matrixStack, s, x, y + 10, 16777215);
        }

        @Override
        public int getItemHeight() {
            return 21;
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

    public static class EmptyEntry extends io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry {
        private final int height;

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

    public static abstract class Entry extends DynamicElementListWidget.ElementEntry<io.github.vampirestudios.raa_dimension.config.screen.dimensions.RAADimensionDescriptionListWidget.Entry> {

    }

}