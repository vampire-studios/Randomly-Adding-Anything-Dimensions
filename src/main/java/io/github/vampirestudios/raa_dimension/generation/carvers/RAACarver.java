package io.github.vampirestudios.raa_dimension.generation.carvers;

import com.google.common.collect.ImmutableSet;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.class_6350;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

//Carver abstraction not place lava in floating island dimensions
public abstract class RAACarver extends Carver<CaveCarverConfig> {

    public RAACarver(DimensionData data) {
        super(CaveCarverConfig.CAVE_CODEC);
        this.alwaysCarvableBlocks = ImmutableSet.of(Registry.BLOCK.get(new Identifier(RAADimensionAddon.MOD_ID, data.getName().toLowerCase() + "_stone")),
                Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL,
                Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA,
                Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA,
                Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
                Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM,
                Blocks.SNOW, Blocks.PACKED_ICE, Blocks.RED_SAND);
    }

    @Override
    protected boolean carveAtPoint(CarverContext carverContext, CaveCarverConfig config, Chunk chunk, Function<BlockPos, Biome> posToBiome, BitSet carvingMask, Random random, BlockPos.Mutable pos, BlockPos.Mutable downPos, class_6350 arg, MutableBoolean foundSurface) {
        BlockState blockState = chunk.getBlockState(pos);
        BlockState blockState2 = chunk.getBlockState(downPos.set(pos, Direction.UP));
        if (blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.MYCELIUM)) {
            foundSurface.setTrue();
        }

        if (!this.canCarveBlock(blockState, blockState2) && !config.debugConfig.isDebugMode()) {
            return false;
        } else {
            BlockState blockState3 = this.method_36418(carverContext, config, pos, arg);
            if (blockState3 == null) {
                return false;
            } else {
                chunk.setBlockState(pos, blockState3, false);
                if (foundSurface.isTrue()) {
                    downPos.set(pos, Direction.DOWN);
                    if (chunk.getBlockState(downPos).isOf(Blocks.DIRT)) {
                        chunk.setBlockState(downPos, posToBiome.apply(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial(), false);
                    }
                }

                return true;
            }
        }
    }

    private static boolean isDebug(CarverConfig config) {
        return config.debugConfig.isDebugMode();
    }

    private static BlockState method_36417(CarverConfig carverConfig, BlockState blockState) {
        if (blockState.isOf(Blocks.AIR)) {
            return carverConfig.debugConfig.getDebugState();
        } else if (blockState.isOf(Blocks.WATER)) {
            BlockState blockState2 = carverConfig.debugConfig.method_36414();
            return blockState2.contains(Properties.WATERLOGGED) ? blockState2.with(Properties.WATERLOGGED, true) : blockState2;
        } else {
            return blockState.isOf(Blocks.LAVA) ? carverConfig.debugConfig.method_36415() : blockState;
        }
    }

    @Nullable
    private BlockState method_36418(CarverContext carverContext, CaveCarverConfig carverConfig, BlockPos blockPos, class_6350 arg) {
        if (blockPos.getY() <= carverConfig.lavaLevel.getY(carverContext)) {
            return LAVA.getBlockState();
        } else if (!carverConfig.field_33610) {
            return isDebug(carverConfig) ? method_36417(carverConfig, AIR) : AIR;
        } else {
            BlockState blockState = arg.a(field_33614, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.0D);
            if (blockState == Blocks.STONE.getDefaultState()) {
                return isDebug(carverConfig) ? carverConfig.debugConfig.method_36416() : null;
            } else {
                return isDebug(carverConfig) ? method_36417(carverConfig, blockState) : blockState;
            }
        }
    }

}
