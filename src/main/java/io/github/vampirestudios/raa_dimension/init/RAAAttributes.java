package io.github.vampirestudios.raa_dimension.init;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class RAAAttributes {
	public static final Attribute GRAVITY_MULTIPLIER = new RangedAttribute("attribute.name.generic.raa_dimensions.gravity_multiplier", 1d, -100d, 100d);

	public static void initialize() {
		Registry.register(Registry.ATTRIBUTE, new ResourceLocation(RAADimensionAddon.MOD_ID, "gravity_multiplier"), GRAVITY_MULTIPLIER);
	}
}