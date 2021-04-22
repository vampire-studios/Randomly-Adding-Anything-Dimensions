package io.github.vampirestudios.raa_dimension.mixin;

import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CaveCarverConfig.class)
public interface CaveCarverConfigAccessor {
    @Accessor
    FloatProvider getFloorLevel();
}
