package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.utils.FeatureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

//Code kindly taken from The Hallow, thanks to everyone who is working on it!
public class SpiderLairFeature extends Feature<NoneFeatureConfiguration> {

    public SpiderLairFeature(Codec<NoneFeatureConfiguration> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        if (world.getBlockState(pos.offset(0, -1, 0)).isAir() || !world.getBlockState(pos.offset(0, -1, 0)).canOcclude() || world.getBlockState(pos.offset(0, -1, 0)).equals(Blocks.BEDROCK.defaultBlockState()))
            return true;
        if (world.getBlockState(pos.below(1)).getBlock() == Blocks.GRASS_BLOCK) {
            FeatureUtils.setSpawner(world, pos, EntityType.SPIDER);

            for (int i = 0; i < 64; ++i) {
                BlockPos pos_2 = pos.offset(random.nextInt(6) - random.nextInt(6), random.nextInt(3) - random.nextInt(3), random.nextInt(6) - random.nextInt(6));
                if (world.isEmptyBlock(pos_2) || world.getBlockState(pos_2).getBlock() == Blocks.GRASS_BLOCK) {
                    world.setBlock(pos_2, Blocks.COBWEB.defaultBlockState(), 2);
                }
            }

            BlockPos chestPos = pos.offset(0, -1, 0);
            world.setBlock(chestPos, StructurePiece.reorient(world, chestPos, Blocks.CHEST.defaultBlockState()), 2);
            RandomizableContainerBlockEntity.setLootTable(world, random, chestPos, new ResourceLocation(RAADimensionAddon.MOD_ID, "chest/spider_lair"));

//            Utils.createSpawnsFile("spider_lair", world, pos);
            return true;
        } else {
            return false;
        }
    }
}