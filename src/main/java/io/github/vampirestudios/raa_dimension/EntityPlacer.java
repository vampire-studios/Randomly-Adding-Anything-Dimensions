package io.github.vampirestudios.raa_dimension;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;

public interface EntityPlacer {
	PortalInfo placeEntity(Entity entity);
}