package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class DimensionColorPalette {

    private int skyColor;
    private int grassColor;
    private int fogColor;
    private int foliageColor;
    private int stoneColor;

    public DimensionColorPalette(int skyColor, int grassColor, int fogColor, int foliageColor, int stoneColor) {
        this.skyColor = skyColor;
        this.grassColor = grassColor;
        this.fogColor = fogColor;
        this.foliageColor = foliageColor;
        this.stoneColor = stoneColor;
    }

    public int getSkyColor() {
        return skyColor;
    }

    public void setSkyColor(int skyColor) {
        this.skyColor = skyColor;
    }

    public int getGrassColor() {
        return grassColor;
    }

    public void setGrassColor(int grassColor) {
        this.grassColor = grassColor;
    }

    public int getFogColor() {
        return fogColor;
    }

    public void setFogColor(int fogColor) {
        this.fogColor = fogColor;
    }

    public int getFoliageColor() {
        return foliageColor;
    }

    public void setFoliageColor(int foliageColor) {
        this.foliageColor = foliageColor;
    }

    public int getStoneColor() {
        return stoneColor;
    }

    public void setStoneColor(int stoneColor) {
        this.stoneColor = stoneColor;
    }

    public static class Builder {

        private int skyColor;
        private int grassColor;
        private int fogColor;
        private int foliageColor;
        private int stoneColor;

        public static Builder create() {
            return new Builder();
        }

        public Builder skyColor(int skyColor) {
            this.skyColor = skyColor;
            return this;
        }

        public Builder grassColor(int grassColor) {
            this.grassColor = grassColor;
            return this;
        }

        public Builder fogColor(int fogColor) {
            this.fogColor = fogColor;
            return this;
        }

        public Builder foliageColor(int foliageColor) {
            this.foliageColor = foliageColor;
            return this;
        }

        public Builder stoneColor(int stoneColor) {
            this.stoneColor = stoneColor;
            return this;
        }

        public DimensionColorPalette build() {
            return new DimensionColorPalette(skyColor, grassColor, fogColor, foliageColor, stoneColor);
        }

    }

}