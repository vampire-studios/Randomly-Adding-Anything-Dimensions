package io.github.vampirestudios.raa_dimension.mixin;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_dimension.DimensionLayerRegistry;
import io.github.vampirestudios.raa_dimension.GravityEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements GravityEntity {
    private int raa_lastY = 0;
    private TeleportTarget raa_nextTeleportTarget = null;

    @Shadow
    public World world;

    @ModifyVariable(at = @At("HEAD"), method = "handleFallDamage", index = 1)
    float raa_getDamageMultiplier(float damageMultiplier) {
        return (float) (damageMultiplier * raa_getGravity() * raa_getGravity());
    }

    @Override
    public double raa_getGravity() {
        World world = ((Entity) (Object) this).world;
        return raa_getGravity(world);
    }

    @Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
    private void raa_getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {
        if (destination.getRegistryKey().getValue().getNamespace().equals("raa_dimensions")) {
            cir.setReturnValue(raa_nextTeleportTarget);
            raa_nextTeleportTarget = null;
        }
    }

    @Inject(at = @At("HEAD"), method = "tickNetherPortal()V")
    void raa_tickNetherPortal(CallbackInfo callbackInformation) {
        Entity entity = (Entity) (Object) this;

        if ((int) entity.getPos().getY() != raa_lastY && !entity.world.isClient && entity.getVehicle() == null) {
            raa_lastY = (int) entity.getPos().getY();

            int bottomPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.world.getRegistryKey());
            int topPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.world.getRegistryKey());

            if (raa_lastY <= bottomPortal && bottomPortal != Integer.MIN_VALUE) {
                RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.world.getRegistryKey()).getValue());

                raa_teleport(entity, worldKey, DimensionLayerRegistry.Type.BOTTOM);
            } else if (raa_lastY >= topPortal && topPortal != Integer.MIN_VALUE) {
                RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.world.getRegistryKey()).getValue());

                raa_teleport(entity, worldKey, DimensionLayerRegistry.Type.TOP);
            }
        }
    }

    void raa_teleport(Entity entity, RegistryKey<World> destinationKey, DimensionLayerRegistry.Type type) {
        ServerWorld serverWorld = entity.world.getServer().getWorld(destinationKey);

        List<DataTracker.Entry<?>> entries = Lists.newArrayList();
        for (DataTracker.Entry<?> entry : entity.getDataTracker().getAllEntries()) {
            entries.add(entry.copy());
        }

        raa_nextTeleportTarget = DimensionLayerRegistry.INSTANCE.getPlacer(type, entity.world.getRegistryKey()).placeEntity(entity);
        Entity newEntity = entity.moveToWorld(serverWorld);

        for (DataTracker.Entry entry : entries) {
            newEntity.getDataTracker().set(entry.getData(), entry.get());
        }
    }

}