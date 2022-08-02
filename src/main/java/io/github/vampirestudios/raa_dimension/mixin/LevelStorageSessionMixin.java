package io.github.vampirestudios.raa_dimension.mixin;

import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public class LevelStorageSessionMixin {

    @Shadow @Final private Path directory;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void createDimensionDatapacks(LevelStorageSource outer, String directoryName, CallbackInfo ci) {
        System.out.println(this.directory);
    }

}
