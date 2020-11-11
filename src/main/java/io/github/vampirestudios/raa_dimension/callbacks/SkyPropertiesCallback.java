package io.github.vampirestudios.raa_dimension.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public interface SkyPropertiesCallback {
	Event<SkyPropertiesCallback> EVENT = EventFactory.createArrayBacked(SkyPropertiesCallback.class, (listeners) -> (properties) -> {
		for (SkyPropertiesCallback listener : listeners) {
			listener.handle(properties);
		}
	});

	void handle(Object2ObjectMap<Identifier, SkyProperties> properties);
}