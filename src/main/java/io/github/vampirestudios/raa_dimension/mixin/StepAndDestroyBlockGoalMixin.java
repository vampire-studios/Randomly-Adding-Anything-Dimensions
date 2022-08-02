package io.github.vampirestudios.raa_dimension.mixin;

import io.github.vampirestudios.raa_dimension.GravityEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RemoveBlockGoal.class)
public class StepAndDestroyBlockGoalMixin {
	@Shadow
	@Final
	private Mob removerMob;

	@ModifyConstant(method = "tick()V", constant = @Constant(doubleValue = 0.08D))
	double getGravity(double original) {
		return ((GravityEntity) removerMob).raa_getGravity();
	}
}