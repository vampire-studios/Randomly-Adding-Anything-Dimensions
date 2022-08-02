/*
package io.github.vampirestudios.raa_dimension;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Collection;
import java.util.function.Predicate;

public class ExpandedBiomeSelectors {

	*/
/**
	 * @see #excludeByTag(Collection)
	 *//*

	@SafeVarargs
	public static Predicate<BiomeSelectionContext> excludeByTag(ResourceKey<Biome>... keys) {
		return excludeByTag(ImmutableSet.copyOf(keys));
	}

	*/
/**
	 * Returns a selector that will reject any biome whos keys is in the given collection of keys.
	 *
	 * <p>This is useful for allowing a list of biomes to be defined in the config file, where
	 * a certain feature should not spawn.
	 *//*

	public static Predicate<BiomeSelectionContext> excludeByTag(Collection<ResourceKey<Biome>> keys) {
		return context -> !keys.contains(context.getBiomeKey());
	}

	*/
/**
	 * @see #includeByTag(Collection)
	 *//*

	@SafeVarargs
	public static Predicate<BiomeSelectionContext> includeByTag(TagKey<Biome>... keys) {
		return includeByTag(ImmutableSet.copyOf(keys));
	}

	*/
/**
	 * Returns a selector that will accept only biomes whos keys are in the given collection of keys.
	 *
	 * <p>This is useful for allowing a list of biomes to be defined in the config file, where
	 * a certain feature should spawn exclusively.
	 *//*

	public static Predicate<BiomeSelectionContext> includeByTag(Collection<TagKey<Biome>> keys) {
		return context -> keys.forEach(context::hasTag);
	}
}
*/
