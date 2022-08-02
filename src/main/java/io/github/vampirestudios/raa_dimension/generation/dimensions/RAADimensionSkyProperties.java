package io.github.vampirestudios.raa_dimension.generation.dimensions;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.Color;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class RAADimensionSkyProperties extends DimensionSpecialEffects {

	private final DimensionData dimensionData;

	public RAADimensionSkyProperties(DimensionData dimensionData) {
		super(dimensionData.getCloudHeight(), false, DimensionSpecialEffects.SkyType.NORMAL, dimensionData.getCustomSkyInformation().hasSky(), false);
		this.dimensionData = dimensionData;
	}

	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
		if (Utils.checkBitFlag(dimensionData.getFlags(), Utils.LUCID)) {
			return color.scale(0.15000000596046448D);
		}
		int fogColor2 = dimensionData.getDimensionColorPalette().getFogColor();
		int[] rgbColor = Color.intToRgb(fogColor2);
		return new Vec3(rgbColor[0] / 255.0, rgbColor[1] / 255.0, rgbColor[2] / 255.0);
	}

	@Override
	public boolean isFoggyAt(int camX, int camY) {
		return false;
	}

	/*@Override
	@Environment(EnvType.CLIENT)
	public float getSunSize() {
		return dimensionData.getCustomSkyInformation().getSunSize();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public float getMoonSize() {
		return dimensionData.getCustomSkyInformation().getMoonSize();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Vector4i getSunTint() {
		int fogColor2 = dimensionData.getCustomSkyInformation().getSunTint();
		int[] rgbColor = Color.intToRgb(fogColor2);
		return new Vector4i(rgbColor[0], rgbColor[1], rgbColor[2], 255);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Vector4i getMoonTint() {
		int fogColor2 = dimensionData.getCustomSkyInformation().getMoonTint();
		int[] rgbColor = Color.intToRgb(fogColor2);
		return new Vector4i(rgbColor[0], rgbColor[1], rgbColor[2], 255);
	}

	@Override
	public ResourceLocation getSunTexture() {
		return dimensionData.getTexturesInformation().getSunTexture();
	}

	@Override
	public ResourceLocation getMoonTexture() {
		return dimensionData.getTexturesInformation().getMoonTexture();
	}

	@Override
	public boolean hasCustomAstralBody() {
		return true;
	}*/
}