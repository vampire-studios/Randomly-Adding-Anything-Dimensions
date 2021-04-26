package io.github.vampirestudios.raa_dimension.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.vampirestudios.raa_dimension.utils.RegistryOpsBlame;
import io.github.vampirestudios.raa_dimension.utils.Validation;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;

@Mixin(RegistryOps.class)
public class RegistryOpsMixin<E> {

    @Inject(method = "of(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/registry/DynamicRegistryManager;)Lnet/minecraft/util/dynamic/RegistryOps;", at = @At("RETURN"), require = 1)
    private static <T> void onCreate(DynamicOps<T> delegate, ResourceManager resourceManager, DynamicRegistryManager dynMgr, CallbackInfoReturnable<RegistryOps<T>> cri) {
        Validation.validate(dynMgr);
    }

    /**
     * Grabs the current file we are at to pass to next mixin in case file explodes.
     */
    @Inject(method = "loadToRegistry(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void getCurrentFile(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec,
                                CallbackInfoReturnable<DataResult<SimpleRegistry<E>>> cir, Collection<Identifier> collection,
                                DataResult<SimpleRegistry<E>> dataresult, String parentPath, Iterator<Identifier> identifierIterator,
                                Identifier identifier)
    {
        RegistryOpsBlame.getCurrentFile(identifier);
    }

    /**
     * Checks if the loaded datapack file errored and print it's resource location if it did
     */
    @Inject(method = "loadToRegistry(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/serialization/DataResult;flatMap(Ljava/util/function/Function;)Lcom/mojang/serialization/DataResult;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void addBrokenFileDetails(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec,
                                      CallbackInfoReturnable<DataResult<SimpleRegistry<E>>> cir, Collection<Identifier> collection,
                                      DataResult<SimpleRegistry<E>> dataresult)
    {
        RegistryOpsBlame.addBrokenFileDetails(dataresult);
    }

}