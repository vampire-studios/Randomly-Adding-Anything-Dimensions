package io.github.vampirestudios.raa_dimension;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import io.github.vampirestudios.raa_dimension.utils.ModelUtils;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RAADimensionAddonClient implements RAAAddonClient {
    @Override
    public void onClientInitialize() {
        Artifice.registerAssetsNew(new Identifier(RAADimensionAddon.MOD_ID, "pack"), clientResourcePackBuilder -> {
            Dimensions.DIMENSIONS.forEach(dimensionData -> {
                Identifier identifier = dimensionData.getId();

                Identifier stoneId = Utils.addSuffixToPath(identifier, "_stone");
                clientResourcePackBuilder.addBlockState(stoneId, blockStateBuilder -> blockStateBuilder.variant("", variant ->
                        variant.model(new Identifier(stoneId.getNamespace(), "block/" + stoneId.getPath())))
                );
                clientResourcePackBuilder.addBlockModel(stoneId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("block/leaves"));
                    modelBuilder.texture("all", dimensionData.getTexturesInformation().getStoneTexture());
                });
                clientResourcePackBuilder.addItemModel(stoneId,
                        modelBuilder -> modelBuilder.parent(new Identifier(stoneId.getNamespace(), "block/" + stoneId.getPath())));

                Identifier stoneStairsId = Utils.addSuffixToPath(identifier, "_stone_stairs");
                ModelUtils.stairs(clientResourcePackBuilder, stoneStairsId, dimensionData.getTexturesInformation().getStoneTexture());

                Identifier stoneSlabId = Utils.addSuffixToPath(identifier, "_stone_slab");
                ModelUtils.slab(clientResourcePackBuilder, stoneSlabId, stoneId, dimensionData.getTexturesInformation().getStoneTexture());

                Identifier stoneWallId = Utils.addSuffixToPath(identifier, "_stone_wall");
                ModelUtils.wall(clientResourcePackBuilder, stoneWallId, dimensionData.getTexturesInformation().getStoneTexture());

                Identifier stoneBricksId = Utils.addSuffixToPath(identifier, "_stone_bricks");
                clientResourcePackBuilder.addBlockState(stoneBricksId, blockStateBuilder -> blockStateBuilder.variant("", variant ->
                        variant.model(new Identifier(stoneBricksId.getNamespace(), "block/" + stoneBricksId.getPath())))
                );
                clientResourcePackBuilder.addBlockModel(stoneBricksId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("block/leaves"));
                    modelBuilder.texture("all", dimensionData.getTexturesInformation().getStoneBricksTexture());
                });
                clientResourcePackBuilder.addItemModel(stoneBricksId,
                        modelBuilder -> modelBuilder.parent(new Identifier(stoneBricksId.getNamespace(), "block/" + stoneBricksId.getPath())));


                Identifier stoneBricksStairsId = Utils.addSuffixToPath(identifier, "_stone_brick_stairs");
                ModelUtils.stairs(clientResourcePackBuilder, stoneBricksStairsId, dimensionData.getTexturesInformation().getStoneBricksTexture());

                Identifier stoneBricksSlabId = Utils.addSuffixToPath(identifier, "_stone_brick_slab");
                ModelUtils.slab(clientResourcePackBuilder, stoneBricksSlabId, stoneBricksId, dimensionData.getTexturesInformation().getStoneBricksTexture());

                Identifier stoneBricksWallId = Utils.addSuffixToPath(identifier, "_stone_brick_wall");
                ModelUtils.wall(clientResourcePackBuilder, stoneBricksWallId, dimensionData.getTexturesInformation().getStoneBricksTexture());


                Identifier cobblestoneId = Utils.addSuffixToPath(identifier, "_cobblestone");
                clientResourcePackBuilder.addBlockState(cobblestoneId, blockStateBuilder -> blockStateBuilder.variant("", variant ->
                        variant.model(new Identifier(cobblestoneId.getNamespace(), "block/" + cobblestoneId.getPath())))
                );
                clientResourcePackBuilder.addBlockModel(cobblestoneId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("block/leaves"));
                    modelBuilder.texture("all", dimensionData.getTexturesInformation().getCobblestoneTexture());
                });
                clientResourcePackBuilder.addItemModel(cobblestoneId,
                        modelBuilder -> modelBuilder.parent(new Identifier(cobblestoneId.getNamespace(), "block/" + cobblestoneId.getPath())));


                Identifier cobblestoneStairsId = Utils.addSuffixToPath(identifier, "_cobblestone_stairs");
                ModelUtils.stairs(clientResourcePackBuilder, cobblestoneStairsId, dimensionData.getTexturesInformation().getCobblestoneTexture());

                Identifier cobblestoneSlabId = Utils.addSuffixToPath(identifier, "_cobblestone_slab");
                ModelUtils.slab(clientResourcePackBuilder, cobblestoneSlabId, cobblestoneId, dimensionData.getTexturesInformation().getCobblestoneTexture());

                Identifier cobblestoneWallId = Utils.addSuffixToPath(identifier, "_cobblestone_wall");
                ModelUtils.wall(clientResourcePackBuilder, cobblestoneWallId, dimensionData.getTexturesInformation().getCobblestoneTexture());


                Identifier chiseledId = Utils.addPrefixAndSuffixToPath(identifier, "chiseled_", "_stone_bricks");
                clientResourcePackBuilder.addBlockState(chiseledId, blockStateBuilder -> blockStateBuilder.variant("", variant ->
                        variant.model(new Identifier(chiseledId.getNamespace(), "block/" + chiseledId.getPath())))
                );
                clientResourcePackBuilder.addBlockModel(chiseledId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("block/leaves"));
                    modelBuilder.texture("all", dimensionData.getTexturesInformation().getChiseledTexture());
                });
                clientResourcePackBuilder.addItemModel(chiseledId,
                        modelBuilder -> modelBuilder.parent(new Identifier(chiseledId.getNamespace(), "block/" + chiseledId.getPath())));


                Identifier crackedChiseledId = Utils.addPrefixAndSuffixToPath(identifier, "cracked_", "_chiseled_stone_bricks");
                clientResourcePackBuilder.addBlockState(crackedChiseledId, blockStateBuilder -> blockStateBuilder.variant("", variant ->
                        variant.model(new Identifier(chiseledId.getNamespace(), "block/" + crackedChiseledId.getPath())))
                );
                clientResourcePackBuilder.addBlockModel(crackedChiseledId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("block/leaves"));
                    modelBuilder.texture("all", dimensionData.getTexturesInformation().getChiseledTexture());
                });
                clientResourcePackBuilder.addItemModel(chiseledId,
                        modelBuilder -> modelBuilder.parent(new Identifier(chiseledId.getNamespace(), "block/" + chiseledId.getPath())));


                Identifier polishedId = Utils.addPrefixToPath(identifier, "polished_");
                clientResourcePackBuilder.addBlockState(polishedId, blockStateBuilder -> blockStateBuilder.variant("", variant ->
                        variant.model(new Identifier(polishedId.getNamespace(), "block/" + polishedId.getPath())))
                );
                clientResourcePackBuilder.addBlockModel(polishedId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("block/leaves"));
                    modelBuilder.texture("all", dimensionData.getTexturesInformation().getPolishedTexture());
                });
                clientResourcePackBuilder.addItemModel(polishedId,
                        modelBuilder -> modelBuilder.parent(new Identifier(polishedId.getNamespace(), "block/" + polishedId.getPath())));


                Identifier polishedStairsId = Utils.addPrefixAndSuffixToPath(identifier, "polished_", "_stairs");
                ModelUtils.stairs(clientResourcePackBuilder, polishedStairsId, dimensionData.getTexturesInformation().getPolishedTexture());

                Identifier polishedSlabId = Utils.addPrefixAndSuffixToPath(identifier, "polished_", "_slab");
                ModelUtils.slab(clientResourcePackBuilder, polishedSlabId, polishedId, dimensionData.getTexturesInformation().getPolishedTexture());

                Identifier polishedWallId = Utils.addPrefixAndSuffixToPath(identifier, "polished_", "_wall");
                ModelUtils.wall(clientResourcePackBuilder, polishedWallId, dimensionData.getTexturesInformation().getPolishedTexture());

                Identifier portalId = Utils.addSuffixToPath(identifier, "_portal");
                clientResourcePackBuilder.addBlockState(portalId, blockStateBuilder -> {
                    /*blockStateBuilder.variant("activated=true", variant ->
                            variant.model(new Identifier(stoneId.getNamespace(), "block/" + portalId.getPath() + "_activated")));
                    blockStateBuilder.variant("activated=false", variant ->
                            variant.model(new Identifier(stoneId.getNamespace(), "block/" + portalId.getPath())));*/
                    blockStateBuilder.variant("", variant -> variant.model(Utils.addPrefixToPath(portalId, "block/")));
                });
                clientResourcePackBuilder.addBlockModel(portalId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa:block/portal"));
                    modelBuilder.texture("0", dimensionData.getTexturesInformation().getStoneTexture());
                    modelBuilder.texture("2", new Identifier("raa:block/metal_top_activated"));
                    modelBuilder.texture("3", new Identifier("raa:block/metal_side"));
                    modelBuilder.texture("4", new Identifier("raa:block/portal_top"));
                    modelBuilder.texture("particle", dimensionData.getTexturesInformation().getStoneTexture());
                });
                /*clientResourcePackBuilder.addBlockModel(Utils.addSuffixToPath(portalId, "_activated"), modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa:block/portal_activated"));
                    modelBuilder.texture("0", dimensionData.getTexturesInformation().getStoneTexture());
                    modelBuilder.texture("2", new Identifier("raa:block/metal_top_activated"));
                    modelBuilder.texture("3", new Identifier("raa:block/metal_side"));
                    modelBuilder.texture("4", new Identifier("raa:block/portal_top"));
                    modelBuilder.texture("5", new Identifier("raa:block/metal_side_activated_overlay"));
                    modelBuilder.texture("particle", dimensionData.getTexturesInformation().getStoneTexture());
                });*/
                clientResourcePackBuilder.addItemModel(portalId,
                        modelBuilder -> modelBuilder.parent(new Identifier(portalId.getNamespace(), "block/" + portalId.getPath())));

                ColorProviderRegistryImpl.ITEM.register((stack, layer) ->  {
                    if (layer == 0) return dimensionData.getCustomSkyInformation().hasSky() ? dimensionData.getDimensionColorPalette().getSkyColor() :
                            dimensionData.getDimensionColorPalette().getFogColor();
                    if (layer == 1) return dimensionData.getDimensionColorPalette().getStoneColor();
                    else return -1;
                }, Registry.ITEM.get(portalId));
                ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) ->  {
                    if (layer == 0) return dimensionData.getCustomSkyInformation().hasSky() ? dimensionData.getDimensionColorPalette().getSkyColor() :
                            dimensionData.getDimensionColorPalette().getFogColor();
                    if (layer == 1) return dimensionData.getDimensionColorPalette().getStoneColor();
                    else return -1;
                }, Registry.BLOCK.get(portalId));
                BlockRenderLayerMapImpl.INSTANCE.putBlock(Registry.BLOCK.get(portalId), RenderLayer.getCutout());

                //TODO: Finish this
                /*clientResourcePackBuilder.addItemModel(Utils.addSuffixToPath(identifier, "_shield"), modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa", "item/shield"));
                    modelBuilder.texture()
                });*/

                Identifier portalKeyId = Utils.addSuffixToPath(identifier, "_portal_key");
                Item portalKey = Registry.ITEM.get(portalKeyId);
                clientResourcePackBuilder.addItemModel(portalKeyId, modelBuilder -> {
                    modelBuilder.parent(new Identifier("item/generated"));
                    modelBuilder.texture("layer0", new Identifier(RAADimensionAddon.MOD_ID, "item/portal_key"));
                });
                ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
                    if (layer == 0) return dimensionData.getDimensionColorPalette().getSkyColor();
                    else return -1;
                }, portalKey);
            });
        });

        Dimensions.DIMENSIONS.forEach(dimensionData -> {
            Identifier identifier = dimensionData.getId();
            Block stone = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone"));
            Block stoneStairs = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_stairs"));
            Block stoneSlab = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_slab"));
            Block stoneWall = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_wall"));
            Block stoneBricks = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_bricks"));
            Block stoneBrickStairs = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_brick_stairs"));
            Block stoneBrickSlab = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_brick_slab"));
            Block stoneBrickWall = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_stone_brick_wall"));
            Block cobblestone = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_cobblestone"));
            Block cobblestoneStairs = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_cobblestone_stairs"));
            Block cobblestoneSlab = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_cobblestone_slab"));
            Block cobblestoneWall = Registry.BLOCK.get(Utils.addSuffixToPath(identifier, "_cobblestone_wall"));
            Block chiseled = Registry.BLOCK.get(Utils.addPrefixAndSuffixToPath(identifier, "chiseled_", "_stone_bricks"));
            Block polished = Registry.BLOCK.get(new Identifier(identifier.getNamespace(), "polished_" + identifier.getPath()));
            Block polishedStairs = Registry.BLOCK.get(new Identifier(identifier.getNamespace(), "polished_" + identifier.getPath() + "_stairs"));
            Block polishedSlab = Registry.BLOCK.get(new Identifier(identifier.getNamespace(), "polished_" + identifier.getPath() + "_slab"));
            Block polishedWall = Registry.BLOCK.get(new Identifier(identifier.getNamespace(), "polished_" + identifier.getPath() + "_wall"));

            ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
                        if (layer == 0) return dimensionData.getDimensionColorPalette().getStoneColor();
                        else return -1;
                    }, stone, stoneSlab, stoneStairs, stoneWall, stoneBricks, stoneBrickSlab, stoneBrickStairs, stoneBrickWall, cobblestone,
                    cobblestoneSlab, cobblestoneStairs, cobblestoneWall, chiseled, polished, polishedSlab, polishedStairs, polishedWall);
            ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) ->
                            dimensionData.getDimensionColorPalette().getStoneColor(),
                    stone, stoneSlab, stoneStairs, stoneWall, stoneBricks, stoneBrickSlab, stoneBrickStairs, stoneBrickWall, cobblestone,
                    cobblestoneSlab, cobblestoneStairs, cobblestoneWall, chiseled, polished, polishedSlab, polishedStairs, polishedWall);
        });
    }

    @Override
    public String getId() {
        return "raa_dimension";
    }

    @Override
    public String[] shouldLoadAfter() {
        return new String[0];
    }
}
