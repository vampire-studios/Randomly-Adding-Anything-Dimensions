package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class BiomeParameters {

    private float temperature;
    private float humidity;
    private float continentalness;
    private float erosion;
    private float weirdness;
    private float depth;
    private float offset;

    public BiomeParameters(float temperature, float humidity, float continentalness, float erosion, float weirdness, float depth, float offset) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.weirdness = weirdness;
        this.depth = depth;
        this.offset = offset;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getContinentalness() {
        return continentalness;
    }

    public float getErosion() {
        return erosion;
    }

    public float getWeirdness() {
        return weirdness;
    }

    public float getDepth() {
        return depth;
    }

    public float getOffset() {
        return offset;
    }

    public static class Builder {

        private float temperature;
        private float humidity;
        private float continentalness;
        private float erosion;
        private float weirdness;
        private float depth;
        private float offset;

        public static Builder builder() {
            return new Builder();
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder humidity(float humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder continentalness(float continentalness) {
            this.continentalness = continentalness;
            return this;
        }

        public Builder erosion(float erosion) {
            this.erosion = erosion;
            return this;
        }

        public Builder weirdness(float weirdness) {
            this.weirdness = weirdness;
            return this;
        }

        public Builder depth(float depth) {
            this.depth = depth;
            return this;
        }

        public Builder offset(float offset) {
            this.offset = offset;
            return this;
        }

        public BiomeParameters create() {
            return new BiomeParameters(temperature, humidity, continentalness, erosion, weirdness, depth, offset);
        }
    }

}
