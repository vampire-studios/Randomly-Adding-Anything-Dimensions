package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.utils.FeatureUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

//Code kindly taken from The Hallow, thanks to everyone who is working on it!
public class SpiderLairFeature extends Feature<DefaultFeatureConfig> {

    public SpiderLairFeature(Codec<DefaultFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        if (world.getBlockState(pos.add(0, -1, 0)).isAir() || !world.getBlockState(pos.add(0, -1, 0)).isOpaque() || world.getBlockState(pos.add(0, -1, 0)).equals(Blocks.BEDROCK.getDefaultState()))
            return true;
        if (world.getBlockState(pos.down(1)).getBlock() == Blocks.GRASS_BLOCK) {
            FeatureUtils.setSpawner(world, pos, EntityType.SPIDER);

            for (int i = 0; i < 64; ++i) {
                BlockPos pos_2 = pos.add(random.nextInt(6) - random.nextInt(6), random.nextInt(3) - random.nextInt(3), random.nextInt(6) - random.nextInt(6));
                if (world.isAir(pos_2) || world.getBlockState(pos_2).getBlock() == Blocks.GRASS_BLOCK) {
                    world.setBlockState(pos_2, Blocks.COBWEB.getDefaultState(), 2);
                }
            }

            BlockPos chestPos = pos.add(0, -1, 0);
            world.setBlockState(chestPos, StructurePiece.orientateChest(world, chestPos, Blocks.CHEST.getDefaultState()), 2);
            LootableContainerBlockEntity.setLootTable(world, random, chestPos, new Identifier(RAADimensionAddon.MOD_ID, "chest/spider_lair"));

//            Utils.createSpawnsFile("spider_lair", world, pos);
            return true;
        } else {
            return false;
        }
    }
}