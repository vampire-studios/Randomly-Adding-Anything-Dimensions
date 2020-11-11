package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class DimensionNoiseSettingsData {

    private final String default_block;
    private final String default_fluid;
    private final boolean disable_mob_generation;
    private final int sea_level;
    private final DimensionNoiseConfigData noise;

    public DimensionNoiseSettingsData(String default_block, String default_fluid, boolean disable_mob_generation, int sea_level, DimensionNoiseConfigData noise) {
        this.default_block = default_block;
        this.default_fluid = default_fluid;
        this.disable_mob_generation = disable_mob_generation;
        this.sea_level = sea_level;
        this.noise = noise;
    }

    public String getDefaultBlock() {
        return default_block;
    }

    public String getDefaultFluid() {
        return default_fluid;
    }

    public boolean disableMobGeneration() {
        return disable_mob_generation;
    }

    public int getSeaLevel() {
        return sea_level;
    }

    public DimensionNoiseConfigData getNoise() {
        return noise;
    }

    public static class Builder {

        private String default_block;
        private String default_fluid;
        private boolean disable_mob_generation;
        private int sea_level;
        private DimensionNoiseConfigData noise;

        public static DimensionNoiseSettingsData.Builder create() {
            return new DimensionNoiseSettingsData.Builder();
        }

        public Builder defaultDlock(String default_block) {
            this.default_block = default_block;
            return this;
        }

        public Builder defaultFluid(String default_fluid) {
            this.default_fluid = default_fluid;
            return this;
        }

        public Builder disableMobGeneration(boolean disable_mob_generation) {
            this.disable_mob_generation = disable_mob_generation;
            return this;
        }

        public Builder seaLevel(int sea_level) {
            this.sea_level = sea_level;
            return this;
        }

        public Builder noise(DimensionNoiseConfigData noise) {
            this.noise = noise;
            return this;
        }

        public DimensionNoiseSettingsData build() {
            return new DimensionNoiseSettingsData(default_block, default_fluid, disable_mob_generation, sea_level, noise);
        }

    }

}