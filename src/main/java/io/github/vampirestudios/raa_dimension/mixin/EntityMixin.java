package io.github.vampirestudios.raa_dimension.mixin;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_dimension.DimensionLayerRegistry;
import io.github.vampirestudios.raa_dimension.GravityEntity;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
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
    private PortalInfo raa_nextTeleportTarget = null;

    @Shadow
    public Level level;

    @ModifyVariable(at = @At("HEAD"), method = "causeFallDamage", index = 1, argsOnly = true)
    float raa_getDamageMultiplier(float damageMultiplier) {
        return (float) (damageMultiplier * raa_getGravity() * raa_getGravity());
    }

    @Override
    public double raa_getGravity() {
        Level world = ((Entity) (Object) this).level;
        return raa_getGravity(world);
    }

    @Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
    private void raa_getTeleportTarget(ServerLevel destination, CallbackInfoReturnable<PortalInfo> cir) {
        if (destination.dimension().location().getNamespace().equals("raa_dimensions")) {
            cir.setReturnValue(raa_nextTeleportTarget);
            raa_nextTeleportTarget = null;
        }
    }

    @Inject(at = @At("HEAD"), method = "handleNetherPortal()V")
    void raa_tickNetherPortal(CallbackInfo callbackInformation) {
        Entity entity = (Entity) (Object) this;

        if ((int) entity.position().y() != raa_lastY && !entity.level.isClientSide && entity.getVehicle() == null) {
            raa_lastY = (int) entity.position().y();

            int bottomPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.BOTTOM, entity.level.dimension());
            int topPortal = DimensionLayerRegistry.INSTANCE.getLevel(DimensionLayerRegistry.Type.TOP, entity.level.dimension());

            if (raa_lastY <= bottomPortal && bottomPortal != Integer.MIN_VALUE) {
                ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.BOTTOM, entity.level.dimension()).location());

                raa_teleport(entity, worldKey, DimensionLayerRegistry.Type.BOTTOM);
            } else if (raa_lastY >= topPortal && topPortal != Integer.MIN_VALUE) {
                ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, DimensionLayerRegistry.INSTANCE.getDimension(DimensionLayerRegistry.Type.TOP, entity.level.dimension()).location());

                raa_teleport(entity, worldKey, DimensionLayerRegistry.Type.TOP);
            }
        }
    }

    void raa_teleport(Entity entity, ResourceKey<Level> destinationKey, DimensionLayerRegistry.Type type) {
        ServerLevel serverWorld = entity.level.getServer().getLevel(destinationKey);

        List<SynchedEntityData.DataItem<?>> entries = Lists.newArrayList();
        for (SynchedEntityData.DataItem<?> entry : entity.getEntityData().getAll()) {
            entries.add(entry.copy());
        }

        raa_nextTeleportTarget = DimensionLayerRegistry.INSTANCE.getPlacer(type, entity.level.dimension()).placeEntity(entity);
        Entity newEntity = entity.changeDimension(serverWorld);

        for (SynchedEntityData.DataItem entry : entries) {
            newEntity.getEntityData().set(entry.getAccessor(), entry.getValue());
        }
    }

}