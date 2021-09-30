package io.github.vampirestudios.raa_dimension.generation.decorator;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomExtraHeightmapDecorator extends Decorator<CountExtraDecoratorConfig> {
    private final Random random = null;

    public RandomExtraHeightmapDecorator(Codec<CountExtraDecoratorConfig> dynamicFunction) {
        super(dynamicFunction);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, CountExtraDecoratorConfig config, BlockPos pos) {
        if (random == null) random = new Random();
        int int_1 = config.count;
        if (random.nextFloat() < config.extraChance) {
            int_1 += config.extraCount;
        }

        Random finalRandom = random;
        return IntStream.range(0, int_1).mapToObj((int_1x) -> {
            int int_2 = finalRandom.nextInt(16) + pos.getX();
            int int_3 = finalRandom.nextInt(16) + pos.getZ();
            int int_4 = context.getTopY(Heightmap.Type.MOTION_BLOCKING, int_2, int_3);
            return new BlockPos(int_2, int_4, int_3);
        });
    }
}