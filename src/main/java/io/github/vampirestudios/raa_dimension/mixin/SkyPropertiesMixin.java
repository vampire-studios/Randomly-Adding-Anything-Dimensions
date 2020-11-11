package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.raa_dimension.callbacks.SkyPropertiesCallback;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public class SkyPropertiesMixin {

	@Shadow
	@Final
	private static Object2ObjectMap<Identifier, SkyProperties> BY_IDENTIFIER;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void astromine_init(CallbackInfo info) {
		SkyPropertiesCallback.EVENT.invoker().handle(BY_IDENTIFIER);
	}
}