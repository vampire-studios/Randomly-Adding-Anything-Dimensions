package io.github.vampirestudios.raa_dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.TeleportTarget;

public interface EntityPlacer {
	TeleportTarget placeEntity(Entity entity);
}