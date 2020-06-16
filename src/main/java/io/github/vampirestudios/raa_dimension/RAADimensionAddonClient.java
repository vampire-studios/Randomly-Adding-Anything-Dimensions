package io.github.vampirestudios.raa_dimension;

import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;

public class RAADimensionAddonClient implements RAAAddonClient {
    @Override
    public void onClientInitialize() {

    }

    @Override
    public String getId() {
        return "raa_dimension";
    }

    @Override
    public String[] shouldLoadAfter() {
        return new String[0];
    }
}
