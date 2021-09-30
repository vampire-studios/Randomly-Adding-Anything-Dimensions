package io.github.vampirestudios.raa_dimension.utils;

import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;

public class CustomTeleporter {

    public static void TPToDim(Identifier dimension, Identifier returnDimension, World world, Entity entity) {
        RegistryKey<World> destKey = world.getRegistryKey() == RAADimensionAddon.dims.get(dimension) ? RAADimensionAddon.dims.get(returnDimension) : RAADimensionAddon.dims.get(dimension);
        ServerWorld destination = ((ServerWorld) world).getServer().getWorld(destKey);
        if (destination == null) return;

        TeleportTarget target = customTPTarget(destination, entity);
        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).teleport(destination, target.position.x, target.position.y, target.position.z, target.yaw, target.pitch);
        } else {
            //copied from entity.moveToWorld(destination);
            entity.detach();
            Entity newEntity = entity.getType().create(destination);
            newEntity.copyFrom(entity);
            newEntity.refreshPositionAndAngles(target.position.x, target.position.y, target.position.z, target.yaw, newEntity.getPitch());
            newEntity.setVelocity(target.velocity);
            destination.onDimensionChanged(newEntity);
            entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        }
    }

    public static TeleportTarget customTPTarget(ServerWorld destination, Entity entity) {
        WorldBorder worldBorder = destination.getWorldBorder();
        double d = Math.max(-2.9999872E7D, worldBorder.getBoundWest() + 16.0D);
        double e = Math.max(-2.9999872E7D, worldBorder.getBoundNorth() + 16.0D);
        double f = Math.min(2.9999872E7D, worldBorder.getBoundEast() - 16.0D);
        double g = Math.min(2.9999872E7D, worldBorder.getBoundSouth() - 16.0D);
        double h = DimensionType.getCoordinateScaleFactor(entity.world.getDimension(), destination.getDimension());
        int x = (int) MathHelper.clamp(entity.getX() * h, d, f);
        int z = (int) MathHelper.clamp(entity.getZ() * h, e, g);
        int y = destination.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos blockPos3 = new BlockPos(x, y, z);
        return idkWhereToPutYou(entity, blockPos3);
    }

    protected static TeleportTarget idkWhereToPutYou(Entity entity, BlockPos pos) {
        return new TeleportTarget(new Vec3d(pos.getX() + .5, pos.getY(), pos.getZ() + .5), entity.getVelocity(), entity.getYaw(), entity.getPitch());
    }
}