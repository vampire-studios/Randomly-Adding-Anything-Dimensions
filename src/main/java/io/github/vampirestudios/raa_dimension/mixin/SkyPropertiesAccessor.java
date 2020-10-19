package io.github.vampirestudios.raa_dimension.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkyProperties.class)
public interface SkyPropertiesAccessor {
    @Accessor
    static Object2ObjectMap<RegistryKey<DimensionType>, SkyProperties> getBY_IDENTIFIER() {
        throw new UnsupportedOperationException();
    }
}
