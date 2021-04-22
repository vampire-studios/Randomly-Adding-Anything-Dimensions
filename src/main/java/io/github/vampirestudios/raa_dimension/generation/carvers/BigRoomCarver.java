package io.github.vampirestudios.raa_dimension.generation.carvers;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.mixin.CaveCarverConfigAccessor;
import net.minecraft.class_6350;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarverConfig;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class BigRoomCarver extends RAACarver {
    public BigRoomCarver(DimensionData data) {
        super(data);
    }

    @Override
    public boolean carve(CarverContext carverContext, CaveCarverConfig carverConfig, Chunk chunk, Function<BlockPos, Biome> posToBiome, Random random, class_6350 arg, ChunkPos pos, BitSet carvingMask) {
        //positions
        double posX = (pos.x* 16) + random.nextInt(16);
        double posZ = (pos.z* 16) + random.nextInt(16);
        double posY = random.nextInt(carverContext.getMaxY());

        //modifiers
        double xVelocity = random.nextDouble() - 0.5;
        double zVoleocity = random.nextDouble() - 0.5;
        double yOffset = random.nextInt(5) - 2; // [-2, 2]
        if (yOffset == 0) yOffset = 1; // 0 offset caves look bad

        //yaw & pitch
        double yaw = 4 + (random.nextDouble()*4);
        double pitch;
        double cmod = 1;

        int bigIndex = random.nextInt(128) + 64; // some caves won't become massive

        for (int i = 0; i < 128; i++) {
            Carver.SkipPredicate skipPredicate = (context, scaledRelativeX, scaledRelativeY, scaledRelativeZ, y) -> isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ, (int) ((CaveCarverConfigAccessor)carverConfig).getFloorLevel().get(random));
            //calculate per-section modifiers
            double xDirectionMod = (random.nextDouble() - 0.5);
            double zDirectionMod = (random.nextDouble() - 0.5);
            double yOffsetMod = (random.nextDouble() - 0.5);
            double yawOffset = (random.nextDouble() - 0.5);

            posY -= (yOffset + yOffsetMod); //lower the carving region
            posX += xVelocity;
            posZ += zVoleocity;
            //velocity application
            xVelocity += xDirectionMod;
            if (xVelocity > 1.5) xVelocity = 1.5;
            if (xVelocity < -1.5)  xVelocity = -1.5;

            zVoleocity += zDirectionMod;
            if (zVoleocity > 1.5) zVoleocity = 1.5;
            if (zVoleocity < -1.5)  zVoleocity = -1.5;

            //yaw modification
            yaw += yawOffset;
            if (yaw < 1.5) yaw = 1.5; // min cave size
            yaw *= cmod;
//            System.out.println(yaw);
//            if (yaw > 30) {
//                yaw = 30;
//            }
            pitch = yaw / 3;
//            if (cmod > 1) {
//                cmod+= 0.003; //increase cmod every time
//            }

            if (i == bigIndex) { //big room
                cmod = 1.01;
            }
            this.carveRegion(carverContext, carverConfig, chunk, posToBiome, random.nextLong(), arg, posX, posY, posZ, yaw, pitch, carvingMask, skipPredicate);
        }

        return true;
    }

    @Override
    public boolean shouldCarve(CaveCarverConfig config, Random random) {
        return random.nextFloat() <= config.probability;
    }

    private static boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ, double floorY) {
        return true;
    }

}
