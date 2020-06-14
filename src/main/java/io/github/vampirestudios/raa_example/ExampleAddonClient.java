package io.github.vampirestudios.raa_example;

import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;

public class ExampleAddonClient implements RAAAddonClient {
    @Override
    public void onClientInitialize() {

    }

    @Override
    public String getId() {
        return "raa_example";
    }

    @Override
    public String[] shouldLoadAfter() {
        return new String[0];
    }
}
