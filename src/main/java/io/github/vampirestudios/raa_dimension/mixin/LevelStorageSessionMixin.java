package io.github.vampirestudios.raa_dimension.mixin;

import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(LevelStorage.Session.class)
public class LevelStorageSessionMixin {

    @Shadow @Final private Path directory;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void createDimensionDatapacks(CallbackInfo ci) {
        System.out.println(this.directory);
    }
}
