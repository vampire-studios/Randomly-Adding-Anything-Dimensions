package io.github.vampirestudios.raa_dimension.utils;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class Testing {

    public static double changeGravity(World world) {
        System.out.println(world.getRegistryKey().getValue());
        if (world.getRegistryKey().getValue().getNamespace().equals(RAADimensionAddon.MOD_ID)) {
            return Dimensions.DIMENSIONS.get(world.getRegistryKey().getValue()).getGravity() * 0.08d;
        }
        return 0.08d;
    }

    public static void changeGravityDamage(StatusEffectInstance statusEffectInstance, World world, float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> info) {
        float f = statusEffectInstance == null ? 0.0F : (float)(statusEffectInstance.getAmplifier() + 1);
        System.out.println(world.getRegistryKey().getValue());
        if (world.getRegistryKey().getValue().getNamespace().equals(RAADimensionAddon.MOD_ID)) {
            info.setReturnValue((int) (MathHelper.ceil((fallDistance - 3.0F - f) * damageMultiplier) * Dimensions.DIMENSIONS.get(world.getRegistryKey().getValue()).getGravity()));
        }
    }

}
