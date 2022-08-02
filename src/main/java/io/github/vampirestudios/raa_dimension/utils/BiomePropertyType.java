package io.github.vampirestudios.raa_dimension.utils;

import net.minecraft.network.chat.Component;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class BiomePropertyType<T> {
    private final T defaultValue;
    private final Component name;

    private BiomePropertyType(T defaultValue, Component name) {
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public BiomeProperty<T> create() {
        return new BiomeProperty<>(this);
    }

    public BiomeProperty<T> create(T value) {
        return new BiomeProperty<>(this, value);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public Component getName() {
        return name;
    }

    public static class Builder<T> {
        private T defaultValue = null;
        private Component name = Component.literal("");

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> name(Component name) {
            this.name = name;
            return this;
        }

        public BiomePropertyType<T> build() {
            return new BiomePropertyType<>(defaultValue, name);
        }
    }
}