package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.raa_dimension.fabric.api.DimensionRenderingRegistry;
import net.fabricmc.fabric.impl.client.rendering.WorldRenderContextImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow @Final private MinecraftClient client;
	@Shadow private ClientWorld world;
	@Unique
	private final WorldRenderContextImpl context = new WorldRenderContextImpl();

	@Inject(at = @At("HEAD"), method = "renderWeather", cancellable = true)
	private void renderWeather(LightmapTextureManager manager, float tickDelta, double x, double y, double z, CallbackInfo info) {
		DimensionRenderingRegistry.WeatherRenderer renderer = null;

		if (this.client.world != null) renderer = DimensionRenderingRegistry.INSTANCE.getWeatherRenderer(world.getRegistryKey());

		if (renderer != null) {
			renderer.render(context);
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FDDD)V", cancellable = true)
	private void renderCloud(MatrixStack matrices, Matrix4f matrix4f, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo info) {
		DimensionRenderingRegistry.CloudRenderer renderer = null;

		if (this.client.world != null) renderer = DimensionRenderingRegistry.INSTANCE.getCloudRenderer(world.getRegistryKey());

		if (renderer != null) {
			renderer.render(context);
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "renderSky", cancellable = true)
	private void renderSky(MatrixStack matrices, Matrix4f matrix4f, float tickDelta, Runnable runnable, CallbackInfo info) {
		DimensionRenderingRegistry.SkyRenderer renderer = null;

		if (this.client.world != null) renderer = DimensionRenderingRegistry.INSTANCE.getSkyRenderer(world.getRegistryKey());

		if (renderer != null) {
			renderer.render(context);
			info.cancel();
		}
	}

}
