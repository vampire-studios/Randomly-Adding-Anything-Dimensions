package io.github.vampirestudios.raa_dimension.generation.feature.tree.foliage;

import com.mojang.datafixers.Dynamic;
import io.github.vampirestudios.raa.registries.FoliagePlacers;
import io.github.vampirestudios.raa.utils.Rands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;

import java.util.Random;
import java.util.Set;

public class RandomSpruceFoliagePlacer extends FoliagePlacer {
    public RandomSpruceFoliagePlacer(int radius, int randomRadius) {
        super(radius, randomRadius, FoliagePlacers.RANDOM_SPRUCE);
    }

    public <T> RandomSpruceFoliagePlacer(Dynamic<T> dynamic_1) {
        this(dynamic_1.get("radius").asInt(0), dynamic_1.get("radius_random").asInt(0));
    }

    @Override
    public void generate(ModifiableTestableWorld modifiableTestableWorld, Random random, BranchedTreeFeatureConfig branchedTreeFeatureConfig, int i, int j, int k, BlockPos blockPos, Set<BlockPos> set) {
        //random spruce
        for (int int_4 = i; int_4 >= j; --int_4) {
            this.generate(modifiableTestableWorld, random, branchedTreeFeatureConfig, i, blockPos, int_4, Rands.randIntRange(0, 5), set);
        }
    }

    @Override
    public int getRadius(Random random, int i, int j, BranchedTreeFeatureConfig branchedTreeFeatureConfig) {
        return this.radius + random.nextInt(this.randomRadius + 1);
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int i, int j, int k, int l, int m) {
        return Math.abs(j) == m && Math.abs(l) == m && m > 0;
    }

    @Override
    public int getRadiusForPlacement(int i, int j, int k, int l) {
        return l <= 1 ? 0 : 2;
    }

    public static RandomSpruceFoliagePlacer method_26653(Random random) {
        return new RandomSpruceFoliagePlacer(random.nextInt(10) + 1, random.nextInt(5));
    }
}
