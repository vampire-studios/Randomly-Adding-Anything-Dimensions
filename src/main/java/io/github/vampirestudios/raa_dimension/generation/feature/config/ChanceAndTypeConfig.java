package io.github.vampirestudios.raa_dimension.generation.feature.config;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.world.gen.decorator.DecoratorConfig;

import java.util.Map;


public class ChanceAndTypeConfig implements DecoratorConfig {
	public final float chanceModifier;
	public final Type type;
	public static final Codec<ChanceAndTypeConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.FLOAT.fieldOf("chance").forGetter((chanceAndTypeConfig) -> {
			return chanceAndTypeConfig.chanceModifier;
		}), ChanceAndTypeConfig.Type.field_24772.fieldOf("heightmap").forGetter((chanceAndTypeConfig) -> {
			return chanceAndTypeConfig.type;
		})).apply(instance, ChanceAndTypeConfig::new);
	});

	public ChanceAndTypeConfig(float chance, Type typeIn) {
		this.chanceModifier = chance;
		this.type = typeIn;
	}

	public enum Type implements StringIdentifiable {
		SUNSHRINE("SUNRISE"), STONEHENGE("STONEHENGE"), HANGING_RUINS("HANGING_RUINS");

		public static final Codec<ChanceAndTypeConfig.Type> field_24772 = StringIdentifiable.createCodec(ChanceAndTypeConfig.Type::values, ChanceAndTypeConfig.Type::byName);
		private final String name;
		private static final Map<String, ChanceAndTypeConfig.Type> BY_NAME = Util.make(Maps.newHashMap(), (hashMap) -> {
			for (Type type : values()) {
				hashMap.put(type.name, type);
			}
		});

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static ChanceAndTypeConfig.Type byName(String name) {
			return BY_NAME.get(name);
		}

		@Override
		public String asString() {
			return name;
		}
	}

}