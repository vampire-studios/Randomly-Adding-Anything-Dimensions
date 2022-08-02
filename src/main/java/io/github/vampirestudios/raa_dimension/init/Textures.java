package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.api.enums.TextureTypes;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Textures {

    public static void init() {
        blockTextures();

        addTextureToList(TextureTypes.SUNS, new ResourceLocation(RAADimensionAddon.MOD_ID, "textures/environment/sun.png"));
        addTextureToList(TextureTypes.SUNS, new ResourceLocation(RAADimensionAddon.MOD_ID, "textures/environment/sun_2.png"));
        addTextureToList(TextureTypes.SUNS, new ResourceLocation(RAADimensionAddon.MOD_ID, "textures/environment/sun_3.png"));

        addTextureToList(TextureTypes.MOONS, new ResourceLocation(RAADimensionAddon.MOD_ID, "textures/environment/moon_phases.png"));
        addTextureToList(TextureTypes.MOONS, new ResourceLocation(RAADimensionAddon.MOD_ID, "textures/environment/moon_phases_2.png"));
    }

    private static void blockTextures() {
        for (int i = 1; i < 12; i++) {
            addTextureToList(TextureTypes.STONE_BRICKS_TEXTURES, "block/stone/bricks_" + i);
        }
        for (int i = 1; i < 21; i++) {
            addTextureToList(TextureTypes.CHISELED_STONE_TEXTURES, "block/stone/chiseled_" + i);
        }
        for (int i = 1; i < 15; i++) {
            addTextureToList(TextureTypes.COBBLESTONE_TEXTURES, "block/stone/cobblestone_" + i);
        }
        for (int i = 1; i < 9; i++) {
            addTextureToList(TextureTypes.POLISHED_STONE_TEXTURES, "block/stone/polished_" + i);
        }
        for (int i = 1; i < 19; i++) {
            addTextureToList(TextureTypes.STONE_TEXTURES, "block/stone/stone_" + i);
        }
        for (int i = 1; i < 11; i++) {
            addTextureToList(TextureTypes.TILES_TEXTURES, "block/stone/tiles_" + i);
        }

        addTextureToList(TextureTypes.MOSSY_STONE_BRICKS_TEXTURES, new ResourceLocation("block/mossy_stone_bricks"));
        addTextureToList(TextureTypes.MOSSY_COBBLESTONE_TEXTURES, new ResourceLocation("block/mossy_cobblestone"));
        addTextureToList(TextureTypes.MOSSY_CHISELED_STONE_TEXTURES, new ResourceLocation("block/chiseled_stone_bricks"));

        addTextureToList(TextureTypes.CRACKED_CHISELED_STONE_TEXTURES, new ResourceLocation("block/chiseled_stone_bricks"));
        addTextureToList(TextureTypes.CRACKED_STONE_BRICKS_TEXTURES, new ResourceLocation("block/cracked_stone_bricks"));

        addTextureToList(TextureTypes.ICE_TEXTURES, "block/ice");
    }

    private static void addTextureToList(List<ResourceLocation> textures, String name) {
        textures.add(new ResourceLocation(RAADimensionAddon.MOD_ID, name));
    }

    private static void addTextureToList(List<ResourceLocation> textures, ResourceLocation name) {
        textures.add(name);
    }

}
