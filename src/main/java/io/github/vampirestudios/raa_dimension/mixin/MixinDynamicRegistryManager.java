package io.github.vampirestudios.raa_dimension.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_dimension.DynamicRegistryCallback;
import io.github.vampirestudios.raa_dimension.DynamicRegistryCallbackManager;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
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

@Mixin(DynamicRegistryManager.class)
public abstract class MixinDynamicRegistryManager {
    @Shadow @Final private static Map<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> INFOS;

    @Shadow public abstract <E> Registry<E> get(RegistryKey<? extends Registry<? extends E>> key);

    @Inject(method = "create", at = @At("RETURN"))
    private static void create(CallbackInfoReturnable<DynamicRegistryManager.Impl> cir) {
        for (RegistryKey<? extends Registry<?>> registryKey : INFOS.keySet()) {
            acceptEvents((RegistryKey) registryKey, cir.getReturnValue(), key -> true);
        }
    }
    
    @Unique
    private static <E> void acceptEvents(RegistryKey<Registry<E>> registryKey, DynamicRegistryManager manager, Predicate<RegistryKey<E>> predicate) {
        manager.getOptional(registryKey).ifPresent(registry -> {
            Event<DynamicRegistryCallback<E>> event = DynamicRegistryCallbackManager.eventNullable(registryKey);
            if (event != null) {
                DynamicRegistryCallback<E> invoker = event.invoker();
                for (Map.Entry<RegistryKey<E>, E> entry : registry.getEntries()) {
                    if (predicate.test(entry.getKey()))
                        invoker.accept(manager, entry.getKey(), entry.getValue());
                }
            }
        });
    }
    
    @Unique Map<Identifier, Set<Identifier>> storedLoaded;

    @Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/util/dynamic/RegistryOps;)V", at = @At("HEAD"))
    private static void beforeLoad(DynamicRegistryManager manager, RegistryOps<?> registryOps, CallbackInfo ci) {
        Map<Identifier, Set<Identifier>> map = Maps.newHashMap();
        ((MixinDynamicRegistryManager) (Object) manager).storedLoaded = map;
        for (RegistryKey<? extends Registry<?>> registryKey : INFOS.keySet()) {
            store((RegistryKey) registryKey, manager, map);
        }
    }
    
    @Unique
    private static <E> void store(RegistryKey<Registry<E>> registryKey, DynamicRegistryManager manager, Map<Identifier, Set<Identifier>> map) {
        manager.getOptional(registryKey).ifPresent(registry -> {
            map.put(registryKey.getValue(), Sets.newHashSet(registry.getIds()));
        });
    }
    
    @Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/util/dynamic/RegistryOps;)V", at = @At("RETURN"))
    private static void afterLoad(DynamicRegistryManager dynamicRegistryManager, RegistryOps<?> registryOps, CallbackInfo ci) {
        Map<Identifier, Set<Identifier>> storedLoaded = ((MixinDynamicRegistryManager) (Object) dynamicRegistryManager).storedLoaded;
        for (RegistryKey<? extends Registry<?>> registryKey : INFOS.keySet()) {
            afterLoad((RegistryKey) registryKey, dynamicRegistryManager, storedLoaded);
        }
        
        ((MixinDynamicRegistryManager) (Object) dynamicRegistryManager).storedLoaded = null;
    }
    
    @Unique
    private static <E> void afterLoad(RegistryKey<Registry<E>> registryKey, DynamicRegistryManager manager, Map<Identifier, Set<Identifier>> map) {
        Set<Identifier> identifiers = map.getOrDefault(registryKey.getValue(), Collections.emptySet());
        acceptEvents(registryKey, manager, valueKey -> !identifiers.contains(valueKey.getValue()));
    }
}