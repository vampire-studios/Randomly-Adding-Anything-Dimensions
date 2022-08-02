/*
package io.github.vampirestudios.raa_dimension.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_dimension.DynamicRegistryCallback;
import io.github.vampirestudios.raa_dimension.DynamicRegistryCallbackManager;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(RegistryAccess.class)
public abstract class MixinDynamicRegistryManager {
    @Shadow @Final private static Map<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> INFOS;

    @Shadow public abstract <E> Registry<E> get(ResourceKey<? extends Registry<? extends E>> key);

    @Inject(method = "create", at = @At("RETURN"))
    private static void create(CallbackInfoReturnable<RegistryAccess.Impl> cir) {
        for (ResourceKey<? extends Registry<?>> registryKey : INFOS.keySet()) {
            acceptEvents((ResourceKey) registryKey, cir.getReturnValue(), key -> true);
        }
    }
    
    @Unique
    private static <E> void acceptEvents(ResourceKey<Registry<E>> registryKey, RegistryAccess manager, Predicate<ResourceKey<E>> predicate) {
        manager.registry(registryKey).ifPresent(registry -> {
            Event<DynamicRegistryCallback<E>> event = DynamicRegistryCallbackManager.eventNullable(registryKey);
            if (event != null) {
                DynamicRegistryCallback<E> invoker = event.invoker();
                for (Map.Entry<ResourceKey<E>, E> entry : registry.getEntries()) {
                    if (predicate.test(entry.getKey()))
                        invoker.accept(manager, entry.getKey(), entry.getValue());
                }
            }
        });
    }
    
    @Unique Map<ResourceLocation, Set<ResourceLocation>> storedLoaded;

    @Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/util/dynamic/RegistryOps;)V", at = @At("HEAD"))
    private static void beforeLoad(RegistryAccess manager, RegistryOps<?> registryOps, CallbackInfo ci) {
        Map<ResourceLocation, Set<ResourceLocation>> map = Maps.newHashMap();
        ((MixinDynamicRegistryManager) (Object) manager).storedLoaded = map;
        for (ResourceKey<? extends Registry<?>> registryKey : INFOS.keySet()) {
            store((ResourceKey) registryKey, manager, map);
        }
    }
    
    @Unique
    private static <E> void store(ResourceKey<Registry<E>> registryKey, RegistryAccess manager, Map<ResourceLocation, Set<ResourceLocation>> map) {
        manager.registry(registryKey).ifPresent(registry -> {
            map.put(registryKey.location(), Sets.newHashSet(registry.keySet()));
        });
    }
    
    @Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/util/dynamic/RegistryOps;)V", at = @At("RETURN"))
    private static void afterLoad(RegistryAccess dynamicRegistryManager, RegistryOps<?> registryOps, CallbackInfo ci) {
        Map<ResourceLocation, Set<ResourceLocation>> storedLoaded = ((MixinDynamicRegistryManager) (Object) dynamicRegistryManager).storedLoaded;
        for (ResourceKey<? extends Registry<?>> registryKey : INFOS.keySet()) {
            afterLoad((ResourceKey) registryKey, dynamicRegistryManager, storedLoaded);
        }
        
        ((MixinDynamicRegistryManager) (Object) dynamicRegistryManager).storedLoaded = null;
    }
    
    @Unique
    private static <E> void afterLoad(ResourceKey<Registry<E>> registryKey, RegistryAccess manager, Map<ResourceLocation, Set<ResourceLocation>> map) {
        Set<ResourceLocation> identifiers = map.getOrDefault(registryKey.location(), Collections.emptySet());
        acceptEvents(registryKey, manager, valueKey -> !identifiers.contains(valueKey.location()));
    }
}*/
