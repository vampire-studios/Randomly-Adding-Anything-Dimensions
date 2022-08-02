package io.github.vampirestudios.raa_dimension.mixin;

import com.mojang.datafixers.DataFixer;
import io.github.vampirestudios.raa_dimension.utils.DynamicRegistryManagerBlame;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

/* @author - TelepathicGrunt
 *
 * A mixin to print unregistered worldgen stuff
 *
 * LGPLv3
 */
@Mixin(value=MinecraftServer.class, priority = 10000)
public class MinecraftServerMixin {

	/**
	 * The main hook for the parser to work from. This will check every biomes in the
	 * DynamicRegistry to see if it has exploded due to unregistered stuff added to it.
	 */
	@Inject(method = "<init>",
			at = @At(value = "RETURN"), require = 1)
	private void init(Thread serverThread, LevelStorageSource.LevelStorageAccess session, PackRepository dataPackManager, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, ChunkProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci)
	{
		DynamicRegistryManagerBlame.printUnregisteredWorldgenConfiguredStuff(worldStem.registryAccess());
	}
}