package io.github.vampirestudios.raa_dimension.mixin;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_dimension.EntityPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public float yaw;

    @Shadow
    public float pitch;

    @Shadow
    public World world;

    private TeleportTarget raa_nextTeleportTarget = null;

    @Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
    private void getTeleportTargetGC(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
        if (destination.getRegistryKey().getValue().getNamespace().equals("raa_dimensions")) { //TODO lander/parachute stuff
            raa_teleport((Entity) (Object) this, destination);
        }
    }

    void raa_teleport(Entity entity, ServerWorld destination) {
        List<DataTracker.Entry<?>> entries = Lists.newArrayList();
        for (DataTracker.Entry<?> entry : Objects.requireNonNull(entity.getDataTracker().getAllEntries())) {
            entries.add(entry.copy());
        }
        BlockPos pos = destination.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, destination.getSpawnPos());

        EntityPlacer placer = entity1 -> new TeleportTarget(new Vec3d((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D), this.getVelocity(),
                this.yaw, this.pitch);
        raa_nextTeleportTarget = placer.placeEntity(entity);
        Entity newEntity = entity.moveToWorld(destination);

        for (DataTracker.Entry entry : entries) {
            newEntity.getDataTracker().set(entry.getData(), entry.get());
        }
    }

    @Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
    protected void astromine_getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
        if (raa_nextTeleportTarget != null) {
            cir.setReturnValue(raa_nextTeleportTarget);
            raa_nextTeleportTarget = null;
        }
    }
}