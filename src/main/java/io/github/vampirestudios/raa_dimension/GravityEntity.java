package io.github.vampirestudios.raa_dimension;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * An interface meant to be implemented by mixins
 * to {@link Entity}-ies, which provides a method
 * to query the gravity of its {@link Level}.
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
	default double raa_getGravity(Level world) {
		if (world.dimension().location().getNamespace().equals(RAADimensionAddon.MOD_ID)) {
			DimensionData dimensionData = Dimensions.DIMENSIONS.get(world.dimension().location());
			return dimensionData.getGravity() * raa_getGravityMultiplier();
		} else {
			return 0.08D;
		}
	}
}