package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.raa_dimension.GravityEntity;
import io.github.vampirestudios.raa_dimension.init.RAAAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Thanks to JoeZwet and GalactiCraft Rewoken
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements GravityEntity {

    @Inject(at = @At("RETURN"), method = "createLivingAttributes")
    private static void createLivingAttributesInject(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(RAAAttributes.GRAVITY_MULTIPLIER);
    }

    @Shadow
    public abstract double getAttributeValue(Attribute attribute);

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08D, ordinal = 0))
    private double modifyGravity(double original) {
        return raa_getGravity();
    }

    @Override
    public double raa_getGravityMultiplier() {
        return getAttributeValue(RAAAttributes.GRAVITY_MULTIPLIER);
    }

}