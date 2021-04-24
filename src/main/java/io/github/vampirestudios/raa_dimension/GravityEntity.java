package io.github.vampirestudios.raa_dimension;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * An interface meant to be implemented by mixins
 * to {@link Entity}-ies, which provides a method
 * to query the gravity of its {@link World}.
 */
public interface GravityEntity {
	/** Returns this entity's gravity multiplier,
	 * used when an entity requires lower or higher
	 * gravity than others. */
	default double raa_getGravityMultiplier() {
		return 1.0d;
	}

	/** Returns this entity's world's gravity. */
	double raa_getGravity();

	/** Returns the given world's gravity. */
	default double raa_getGravity(World world) {
		if (world.getRegistryKey().getValue().getNamespace().equals(RAADimensionAddon.MOD_ID)) {
			DimensionData dimensionData = Dimensions.DIMENSIONS.get(world.getRegistryKey().getValue());
			return dimensionData.getGravity() * raa_getGravityMultiplier();
		} else {
			return 0.08D;
		}
	}
}