package io.github.vampirestudios.raa_dimension.utils;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class Testing {

    public static double changeGravity(Level world) {
        if (world.dimension().location().getNamespace().equals(RAADimensionAddon.MOD_ID)) {
            return Dimensions.DIMENSIONS.get(world.dimension().location()).getGravity() * 0.08d;
        }
        return 0.08d;
    }

    public static void changeGravityDamage(MobEffectInstance statusEffectInstance, Level world, float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> info) {
        float f = statusEffectInstance == null ? 0.0F : (float)(statusEffectInstance.getAmplifier() + 1);
        if (world.dimension().location().getNamespace().equals(RAADimensionAddon.MOD_ID)) {
            info.setReturnValue((int) (Mth.ceil((fallDistance - 3.0F - f) * damageMultiplier) * Dimensions.DIMENSIONS.get(world.dimension().location()).getGravity()));
        }
    }

}
