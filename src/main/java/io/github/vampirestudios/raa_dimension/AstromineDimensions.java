package io.github.vampirestudios.raa_dimension;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class AstromineDimensions {
	private static final Set<ResourceKey<?>> KEYS = new HashSet<>();

	public static <T> ResourceKey<T> register(ResourceKey<Registry<T>> registry, ResourceLocation identifier) {
		ResourceKey<T> key = ResourceKey.create(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(ResourceKey<?> key) {
		return KEYS.contains(key);
	}

	public static void initialize() {

	}
}