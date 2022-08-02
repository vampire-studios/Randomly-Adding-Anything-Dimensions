package io.github.vampirestudios.raa_dimension.utils;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

public class CustomTeleporter {

    public static void TPToDim(ResourceLocation dimension, ResourceLocation returnDimension, Level world, Entity entity) {
        ResourceKey<Level> destKey = world.dimension() == RAADimensionAddon.dims.get(dimension) ? RAADimensionAddon.dims.get(returnDimension) : RAADimensionAddon.dims.get(dimension);
        ServerLevel destination = ((ServerLevel) world).getServer().getLevel(destKey);
        if (destination == null) return;

        PortalInfo target = customTPTarget(destination, entity);
        FabricDimensions.teleport(entity, destination, target);
        /*if (entity instanceof ServerPlayer) {
            ((ServerPlayer) entity).teleportTo(destination, target.pos.x, target.pos.y, target.pos.z, target.yRot, target.xRot);
        } else {
            //copied from entity.moveToWorld(destination);
            entity.unRide();
            Entity newEntity = entity.getType().create(destination);
            newEntity.restoreFrom(entity);
            newEntity.moveTo(target.pos.x, target.pos.y, target.pos.z, target.yRot, target.xRot);
            newEntity.setDeltaMovement(target.speed);
            destination.addDuringTeleport(newEntity);
            entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        }*/
    }

    public static PortalInfo customTPTarget(ServerLevel destination, Entity entity) {
        WorldBorder worldBorder = destination.getWorldBorder();
        double d = Math.max(-2.9999872E7D, worldBorder.getMinX() + 16.0D);
        double e = Math.max(-2.9999872E7D, worldBorder.getMinZ() + 16.0D);
        double f = Math.min(2.9999872E7D, worldBorder.getMaxX() - 16.0D);
        double g = Math.min(2.9999872E7D, worldBorder.getMaxZ() - 16.0D);
        double h = DimensionType.getTeleportationScale(entity.level.dimensionType(), destination.dimensionType());
        int x = (int) Mth.clamp(entity.getX() * h, d, f);
        int z = (int) Mth.clamp(entity.getZ() * h, e, g);
        int y = destination.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos blockPos3 = new BlockPos(x, y, z);
        return idkWhereToPutYou(entity, blockPos3);
    }

    protected static PortalInfo idkWhereToPutYou(Entity entity, BlockPos pos) {
        return new PortalInfo(new Vec3(pos.getX() + .5, pos.getY(), pos.getZ() + .5), entity.getDeltaMovement(), entity.yRotO, entity.xRotO);
    }
}