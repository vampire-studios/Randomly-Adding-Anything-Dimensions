package io.github.vampirestudios.raa_dimension.api.dimension;

import io.github.vampirestudios.raa.blocks.PortalBlock;
import io.github.vampirestudios.raa.generation.dimensions.CustomDimension;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

public enum PlayerPlacementHandlers {
    SURFACE_WORLD((teleported, destination, portalDir, horizontalOffset, verticalOffset) -> {
        BlockPos blockPos = getSurfacePos(destination, teleported);
        destination.setBlockState(blockPos.down(1), Registry.BLOCK.get(Utils.appendToPath(destination.getDimensionRegistryKey().getValue(), "_portal")).getDefaultState());
        return new BlockPattern.TeleportTarget(new Vec3d(new Vector3f(blockPos.getX(), blockPos.getY(), blockPos.getZ())), Vec3d.ZERO, 0);
    }),
    CAVE_WORLD((teleported, destination, portalDir, horizontalOffset, verticalOffset) -> {
        BlockPos blockPos = getSurfacePos(destination, teleported);
        destination.setBlockState(blockPos.down(1), Registry.BLOCK.get(Utils.appendToPath(destination.getDimensionRegistryKey().getValue(), "_portal")).getDefaultState());
        return new BlockPattern.TeleportTarget(new Vec3d(new Vector3f(blockPos.getX(), blockPos.getY(), blockPos.getZ())), Vec3d.ZERO, 0);
    }),
    OVERWORLD((teleported, destination, portalDir, horizontalOffset, verticalOffset) -> {
        BlockPos blockPos = getSurfacePos(destination, teleported);
        destination.setBlockState(blockPos.down(1), Registry.BLOCK.get(Utils.appendToPath(destination.getDimensionRegistryKey().getValue(), "_portal")).getDefaultState());
        return new BlockPattern.TeleportTarget(new Vec3d(new Vector3f(blockPos.getX(), blockPos.getY(), blockPos.getZ())), Vec3d.ZERO, 0);
    }),
    FLOATING_WORLD((teleported, destination, portalDir, horizontalOffset, verticalOffset) -> {
        BlockPos blockPos = getSurfacePos(destination, teleported);
        if (blockPos.getY() == 255 || blockPos.getY() == 256) {
            blockPos = new BlockPos(blockPos.getX(), teleported.getY(), blockPos.getZ());
            destination.setBlockState(blockPos.down(1), Registry.BLOCK.get(Utils.appendToPath(destination.getDimensionRegistryKey().getValue(), "_portal")).getDefaultState());

            BlockState stone = ((CustomDimension) destination.getDimension()).getStoneBlock().getDefaultState();
            blockPos = blockPos.down();
            destination.setBlockState(blockPos.west(), stone);
            destination.setBlockState(blockPos.west().north(), stone);
            destination.setBlockState(blockPos.north(), stone);
            destination.setBlockState(blockPos.north().east(), stone);
            destination.setBlockState(blockPos.east(), stone);
            destination.setBlockState(blockPos.east().south(), stone);
            destination.setBlockState(blockPos.south(), stone);
            destination.setBlockState(blockPos.south().west(), stone);

            blockPos = blockPos.down();
            destination.setBlockState(blockPos, stone);
            destination.setBlockState(blockPos.west(), stone);
            destination.setBlockState(blockPos.west().north(), stone);
            destination.setBlockState(blockPos.north(), stone);
            destination.setBlockState(blockPos.north().east(), stone);
            destination.setBlockState(blockPos.east(), stone);
            destination.setBlockState(blockPos.east().south(), stone);
            destination.setBlockState(blockPos.south(), stone);
            destination.setBlockState(blockPos.south().west(), stone);

            return new BlockPattern.TeleportTarget(new Vec3d(new Vector3f(blockPos.up(2).getX(), blockPos.up(2).getY(), blockPos.up(2).getZ())), Vec3d.ZERO, 0);
        } else {
            destination.setBlockState(blockPos.down(1), Registry.BLOCK.get(Utils.appendToPath(destination.getDimensionRegistryKey().getValue(), "_portal")).getDefaultState());
            return new BlockPattern.TeleportTarget(new Vec3d(new Vector3f(blockPos.getX(), blockPos.getY(), blockPos.getZ())), Vec3d.ZERO, 0);
        }
    });

    private final EntityPlacer entityPlacer;

    PlayerPlacementHandlers(EntityPlacer entityPlacer) {
        this.entityPlacer = entityPlacer;
    }

    private static BlockPos getSurfacePos(ServerWorld serverWorld, Entity entity) {
        BlockPos portalPos = getPortalPos(serverWorld, entity, 255);
        if (portalPos != null) return portalPos.up();
        for (int i = 255; i > 0; i--) {
            BlockPos pos = new BlockPos(entity.getBlockPos().getX(), i, entity.getBlockPos().getZ());
            if (!(serverWorld.getBlockState(pos.down(1)).getBlock() instanceof AirBlock) && (serverWorld.getBlockState(pos.up()).getBlock() instanceof AirBlock) && (serverWorld.getBlockState(pos).getBlock() instanceof AirBlock)) {
                return pos.up();
            }
        }
        portalPos = new BlockPos(entity.getBlockPos().getX(), 255 + 1, entity.getBlockPos().getZ());
        serverWorld.setBlockState(portalPos.up(), Blocks.AIR.getDefaultState());
        serverWorld.setBlockState(portalPos, Blocks.AIR.getDefaultState());
        return portalPos;
    }

    private static BlockPos getPortalPos(ServerWorld serverWorld, Entity entity, int maxHeight) {
        for (int i = maxHeight; i > 0; i--) {
            BlockPos pos = new BlockPos(entity.getPos().x, i, entity.getPos().z);
            if (serverWorld.getBlockState(pos).getBlock() instanceof PortalBlock) {
                return pos;
            }
        }
        return null;
    }

    public EntityPlacer getEntityPlacer() {
        return entityPlacer;
    }
}
