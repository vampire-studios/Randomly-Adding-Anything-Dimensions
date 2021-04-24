package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.raa_dimension.GravityEntity;
import io.github.vampirestudios.raa_dimension.init.RAAAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
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

    @Inject(at = @At("RETURN"), method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;")
    private static void createLivingAttributesInject(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue().add(RAAAttributes.GRAVITY_MULTIPLIER);
    }

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D, ordinal = 0))
    private double modifyGravity(double original) {
        return raa_getGravity();
    }

    @Override
    public double raa_getGravityMultiplier() {
        return getAttributeValue(RAAAttributes.GRAVITY_MULTIPLIER);
    }

}