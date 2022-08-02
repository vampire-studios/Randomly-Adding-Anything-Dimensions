package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.raa_dimension.utils.BiomeProperty;
import io.github.vampirestudios.raa_dimension.utils.BiomePropertyType;
import io.github.vampirestudios.raa_dimension.utils.GCBiomePropertyAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
@Mixin(Biome.class)
public abstract class BiomeMixin implements GCBiomePropertyAccessor {
    @Unique
    private final Map<BiomePropertyType<?>, BiomeProperty<?>> properties = new HashMap<>();

    @Override
    public <T> T getProperty(@NotNull BiomePropertyType<T> type) {
        return (T) properties.getOrDefault(type, type.create()).getValue();
    }

    @Override
    public <T> void setProperty(BiomePropertyType<T> type, T value) {
        this.properties.put(type, type.create(value));
    }

    @Override
    public Map<BiomePropertyType<?>, BiomeProperty<?>> getProperties() {
        return properties;
    }
}