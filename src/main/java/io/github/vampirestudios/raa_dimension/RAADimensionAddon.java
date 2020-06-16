package io.github.vampirestudios.raa_dimension;

import io.github.vampirestudios.raa_core.api.RAAAddon;

public class RAADimensionAddon implements RAAAddon {
	@Override
	public String[] shouldLoadAfter() {
		return new String[0];
	}

	@Override
	public String getId() {
		return "raa_dimension";
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
	}
}
