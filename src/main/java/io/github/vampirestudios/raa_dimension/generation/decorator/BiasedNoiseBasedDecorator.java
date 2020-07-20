package io.github.vampirestudios.raa_dimension.generation.decorator;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BiasedNoiseBasedDecorator extends Decorator<BiasedNoiseBasedDecoratorConfig> {
    public static final OctaveSimplexNoiseSampler NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(79L), IntStream.of(2, 0));

    public BiasedNoiseBasedDecorator(Codec<BiasedNoiseBasedDecoratorConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, BiasedNoiseBasedDecoratorConfig config, BlockPos pos) {
        double noise = NOISE.sample((double) pos.getX() / config.noiseFactor, (double) pos.getZ() / config.noiseFactor, false);
        int int_1 = (int) Math.ceil((noise + config.noiseOffset) * (double) config.noiseToCountRatio);
        return IntStream.range(0, int_1).mapToObj((int_1x) -> {
            int int_2 = random.nextInt(16);
            int int_3 = random.nextInt(16);
            int int_4 = context.getTopY(config.heightmap, pos.getX() + int_2, pos.getZ() + int_3);
            return new BlockPos(pos.getX() + int_2, int_4, pos.getZ() + int_3);
        });
    }
}
