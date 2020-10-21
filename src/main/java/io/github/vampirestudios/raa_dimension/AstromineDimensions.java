package io.github.vampirestudios.raa_dimension;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashSet;
import java.util.Set;

public class AstromineDimensions {
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();

	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		RegistryKey<T> key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}

	public static void initialize() {

	}
}