package io.github.vampirestudios.raa_dimension.generation.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

// Thanks to TelepathicGrunt and the UltraAmplified mod for this class
public class ColumnBlocksConfig implements FeatureConfiguration {
	public final BlockState topBlock;
	public final BlockState middleBlock;
	public final BlockState insideBlock;
	public static final Codec<ColumnBlocksConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockState.CODEC.fieldOf("topBlock").forGetter((chanceAndTypeConfig) -> {
			return chanceAndTypeConfig.topBlock;
		}), BlockState.CODEC.fieldOf("middleBlock").forGetter((chanceAndTypeConfig) -> {
			return chanceAndTypeConfig.middleBlock;
		}), BlockState.CODEC.fieldOf("insideBlock").forGetter((chanceAndTypeConfig) -> {
			return chanceAndTypeConfig.insideBlock;
		})).apply(instance, ColumnBlocksConfig::new);
	});

	public ColumnBlocksConfig(BlockState topBlock, BlockState middleBlock, BlockState insideBlock) {
		this.topBlock = topBlock;
		this.middleBlock = middleBlock;
		this.insideBlock = insideBlock;
	}


	/*@Override
	public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
		return new Dynamic<>(
			ops,
			ops.createMap(
				ImmutableMap.of(
					ops.createString("top_block"), ops.createString(Registry.BLOCK.getId(topBlock.getBlock()).toString()),
					ops.createString("middle_block"), ops.createString(Registry.BLOCK.getId(middleBlock.getBlock()).toString()),
					ops.createString("inside_block"), ops.createString(Registry.BLOCK.getId(insideBlock.getBlock()).toString())
				)
			)
		);
	}


	public static <T> ColumnBlocksConfig deserialize(Dynamic<T> ops) {
		BlockState topBlock = ops.get("top_block").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		BlockState middleBlock = ops.get("middle_block").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		BlockState insideBlock = ops.get("inside_block").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		return new ColumnBlocksConfig(topBlock, middleBlock, insideBlock);
	}*/
}