package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RAAAttributes {
	public static final EntityAttribute GRAVITY_MULTIPLIER = new ClampedEntityAttribute("attribute.name.generic.raa_dimensions.gravity_multiplier", 1d, -100d, 100d);

	public static void initialize() {
		Registry.register(Registry.ATTRIBUTE, new Identifier(RAADimensionAddon.MOD_ID, "gravity_multiplier"), GRAVITY_MULTIPLIER);
	}
}