package io.github.vampirestudios.raa_dimension.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class FeatureUtils {

    public static void setSpawner(ServerLevelAccessor world, BlockPos pos, EntityType<?> entity) {
        world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof SpawnerBlockEntity) {
            ((SpawnerBlockEntity) be).getSpawner().setEntityId(entity);
        }
    }

    public static void setLootChest(ServerLevelAccessor world, BlockPos pos, ResourceLocation lootTable, RandomSource rand) {
        world.setBlock(pos, Blocks.CHEST.defaultBlockState(), 2);

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof ChestBlockEntity) {
            ((ChestBlockEntity) entity).setLootTable(lootTable, rand.nextLong());
        }
    }

    public static PlacementModifier countExtra(int count, float extraChance, int extraCount) {
        if (extraChance < 0 || extraChance > 1) {
            throw new IllegalStateException("Chance data cannot be represented as list weight");
        } else {
            int f = (int) (extraChance * 1000);
            SimpleWeightedRandomList<IntProvider> simpleweightedrandomlist = SimpleWeightedRandomList.<IntProvider>builder()
                    .add(ConstantInt.of(count), 1000 - f)
                    .add(ConstantInt.of(count + extraCount), f)
                    .build();
            return CountPlacement.of(new WeightedListInt(simpleweightedrandomlist));
        }
    }
}