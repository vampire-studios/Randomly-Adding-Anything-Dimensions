package io.github.vampirestudios.raa_dimension.mixin;

import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CaveCarverConfiguration.class)
public interface CaveCarverConfigAccessor {
    @Accessor
    FloatProvider getFloorLevel();
}
