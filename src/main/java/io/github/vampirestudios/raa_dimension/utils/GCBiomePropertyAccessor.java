package io.github.vampirestudios.raa_dimension.utils;

import java.util.Map;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public interface GCBiomePropertyAccessor {
    <T> T getProperty(BiomePropertyType<T> type);

    <T> void setProperty(BiomePropertyType<T> type, T value);

    Map<BiomePropertyType<?>, BiomeProperty<?>> getProperties();
}