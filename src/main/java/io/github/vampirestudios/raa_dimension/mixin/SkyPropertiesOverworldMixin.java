package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkyProperties.Overworld.class)
public class SkyPropertiesOverworldMixin extends SkyProperties implements AstralBodyModifier {

    public SkyPropertiesOverworldMixin(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean shouldRenderSky, boolean darkened) {
        super(cloudsHeight, alternateSkyColor, skyType, shouldRenderSky, darkened);
    }

    @Override
    public Vec3d adjustSkyColor(Vec3d color, float sunHeight) {
        return color.multiply(sunHeight * 0.94F + 0.06F, sunHeight * 0.94F + 0.06F, sunHeight * 0.91F + 0.09F);
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }

    @Override
    public boolean shouldRenderSky() {
        return true;
    }

    @Override
    public boolean hasCustomSky() {
        return true;
    }

    @Override
    public boolean isEndSky() {
        return true;
    }

}
