package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DimensionBiomeData {
    private ResourceLocation id;
    private String biomeName;
    private BiomeParameters biomeParameters;
    private float depth;
    private float scale;
    private float temperature;
    private float downfall;
    private int waterColor;
    private int grassColor;
    private int foliageColor;
    private List<DimensionTreeData> treeData;
    private float corruptedCratersChance;
    private float nonCorruptedCratersChance;
    private boolean spawnsCratersInNonCorrupted;
    private float largeSkeletonTreeChance;
    private float campfireChance;
    private float outpostChance;
    private float towerChance;
    private boolean hasMushrooms;
    private boolean hasMossyRocks;
    private ResourceLocation surfaceBuilder;
    private ResourceLocation surfaceConfig;
    private List<CarverType> carvers;

    DimensionBiomeData(ResourceLocation id, String biomeName, BiomeParameters biomeParameters, float depth, float scale, float temperature, float downfall, int waterColor,
                       int grassColor, int foliageColor, List<DimensionTreeData> treeData, float corruptedCratersChance, float nonCorruptedCratersChance,
                       boolean spawnsCratersInNonCorrupted, float largeSkeletonTreeChance, float campfireChance, float outpostChance, float towerChance,
                       boolean hasMushrooms, boolean hasMossyRocks, ResourceLocation surfaceBuilder, ResourceLocation surfaceConfig, List<CarverType> carvers) {
        this.id = id;
        this.biomeName = biomeName;
        this.biomeParameters = biomeParameters;
        this.depth = depth;
        this.scale = scale;
        this.temperature = temperature;
        this.downfall = downfall;
        this.waterColor = waterColor;
        this.grassColor = grassColor;
        this.foliageColor = foliageColor;
        this.treeData = treeData;
        this.corruptedCratersChance = corruptedCratersChance;
        this.nonCorruptedCratersChance = nonCorruptedCratersChance;
        this.spawnsCratersInNonCorrupted = spawnsCratersInNonCorrupted;
        this.largeSkeletonTreeChance = largeSkeletonTreeChance;
        this.campfireChance = campfireChance;
        this.outpostChance = outpostChance;
        this.towerChance = towerChance;
        this.hasMushrooms = hasMushrooms;
        this.hasMossyRocks = hasMossyRocks;
        this.surfaceBuilder = surfaceBuilder;
        this.surfaceConfig = surfaceConfig;
        this.carvers = carvers;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public String getName() {
        return biomeName;
    }

    public void setName(String biomeName) {
        this.biomeName = biomeName;
    }

    public BiomeParameters getBiomeParameters() {
        return biomeParameters;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getDownfall() {
        return downfall;
    }

    public void setDownfall(float downfall) {
        this.downfall = downfall;
    }

    public int getWaterColor() {
        return waterColor;
    }

    public void setWaterColor(int waterColor) {
        this.waterColor = waterColor;
    }

    public int getGrassColor() {
        return grassColor;
    }

    public int getFoliageColor() {
        return foliageColor;
    }

    public void setFoliageColor(int foliageColor) {
        this.foliageColor = foliageColor;
    }

    public List<DimensionTreeData> getTreeData() {
        return treeData;
    }

    public float getCorruptedCratersChance() {
        return corruptedCratersChance;
    }

    public float getNonCorruptedCratersChance() {
        return nonCorruptedCratersChance;
    }

    public boolean spawnsCratersInNonCorrupted() {
        return spawnsCratersInNonCorrupted;
    }

    public float getLargeSkeletonTreeChance() {
        return largeSkeletonTreeChance;
    }

    public float getCampfireChance() {
        return campfireChance;
    }

    public float getOutpostChance() {
        return outpostChance;
    }

    public float getTowerChance() {
        return towerChance;
    }

    public boolean hasMushrooms() {
        return hasMushrooms;
    }

    public boolean hasMossyRocks() {
        return hasMossyRocks;
    }

    public List<CarverType> getCarvers() {
        return carvers;
    }

    public static class Builder {
        private ResourceLocation id;
        private String name;
        private BiomeParameters biomeParameters;
        private float depth;
        private float scale;
        private float temperature;
        private float downfall;
        private int waterColor;
        private int grassColor;
        private int foliageColor;
        private List<DimensionTreeData> treeData;
        private float corruptedCratersChance;
        private float nonCorruptedCratersChance;
        private boolean spawnsCratersInNonCorrupted;
        private float largeSkeletonTreeChance;
        private float campfireChance;
        private float outpostChance;
        private float towerChance;
        private boolean hasMushrooms;
        private boolean hasMossyRocks;
        private ResourceLocation surfaceBuilder;
        private ResourceLocation surfaceConfig;
        private List<CarverType> carvers;

        private Builder() {

        }

        public static Builder create(ResourceLocation id, String name) {
            Builder builder = new Builder();
            builder.id = id;
            builder.name = name;
            return builder;
        }

        @Deprecated
        public static Builder create() {
            return new Builder();
        }

        public Builder id(ResourceLocation id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder biomeParameters(BiomeParameters biomeParameters) {
            this.biomeParameters = biomeParameters;
            return this;
        }

        public Builder depth(float depth) {
            this.depth = depth;
            return this;
        }

        public Builder scale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder downfall(float downfall) {
            this.downfall = downfall;
            return this;
        }

        public Builder waterColor(int waterColor) {
            this.waterColor = waterColor;
            return this;
        }

        public Builder grassColor(int grassColor) {
            this.grassColor = grassColor;
            return this;
        }

        public Builder foliageColor(int foliageColor) {
            this.foliageColor = foliageColor;
            return this;
        }

        public Builder treeData(List<DimensionTreeData> treeData) {
            this.treeData = treeData;
            return this;
        }

        public Builder hasMossyRocks(boolean hasMossyRocks) {
            this.hasMossyRocks = hasMossyRocks;
            return this;
        }

        public Builder hasMushrooms(boolean hasMushrooms) {
            this.hasMushrooms = hasMushrooms;
            return this;
        }

        public Builder towerChance(float towerChance) {
            this.towerChance = towerChance;
            return this;
        }

        public Builder outpostChance(float outpostChance) {
            this.outpostChance = outpostChance;
            return this;
        }

        public Builder campfireChance(float campfireChance) {
            this.campfireChance = campfireChance;
            return this;
        }

        public Builder largeSkeletonTreeChance(float largeSkeletonTreeChance) {
            this.largeSkeletonTreeChance = largeSkeletonTreeChance;
            return this;
        }

        public Builder spawnsCratersInNonCorrupted(boolean spawnsCratersInNonCorrupted) {
            this.spawnsCratersInNonCorrupted = spawnsCratersInNonCorrupted;
            return this;
        }

        public Builder nonCorruptedCratersChance(float nonCorruptedCratersChance) {
            this.nonCorruptedCratersChance = nonCorruptedCratersChance;
            return this;
        }

        public Builder corruptedCratersChance(float corruptedCratersChance) {
            this.corruptedCratersChance = corruptedCratersChance;
            return this;
        }

        public Builder surfaceBuilder(ResourceLocation surfaceBuilder) {
            this.surfaceBuilder = surfaceBuilder;
            return this;
        }

        public Builder surfaceConfig(ResourceLocation surfaceConfig) {
            this.surfaceConfig = surfaceConfig;
            return this;
        }

        public Builder carvers(List<CarverType> carvers) {
            this.carvers = carvers;
            return this;
        }

        public DimensionBiomeData build() {
            return new DimensionBiomeData(id, name, biomeParameters, depth, scale, temperature, downfall, waterColor, grassColor, foliageColor, treeData,
                    corruptedCratersChance, nonCorruptedCratersChance, spawnsCratersInNonCorrupted, largeSkeletonTreeChance, campfireChance, outpostChance,
                    towerChance, hasMushrooms, hasMossyRocks, surfaceBuilder, surfaceConfig, carvers);
        }
    }
}