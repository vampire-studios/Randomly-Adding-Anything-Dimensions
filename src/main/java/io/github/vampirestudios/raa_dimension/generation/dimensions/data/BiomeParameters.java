package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class BiomeParameters {

    private float altitude;
    private float weirdness;
    private float offset;
    private float temperature;
    private float humidity;

    public BiomeParameters(float altitude, float weirdness, float offset, float temperature, float humidity) {
        this.altitude = altitude;
        this.weirdness = weirdness;
        this.offset = offset;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public float getAltitude() {
        return altitude;
    }

    public float getWeirdness() {
        return weirdness;
    }

    public float getOffset() {
        return offset;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public static class Builder {

        private float altitude;
        private float weirdness;
        private float offset;
        private float temperature;
        private float humidity;

        public static Builder builder() {
            return new Builder();
        }

        public Builder altitude(float altitude) {
            this.altitude = altitude;
            return this;
        }

        public Builder weirdness(float weirdness) {
            this.weirdness = weirdness;
            return this;
        }

        public Builder offset(float offset) {
            this.offset = offset;
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder humidity(float humidity) {
            this.humidity = humidity;
            return this;
        }

        public BiomeParameters create() {
            return new BiomeParameters(altitude, weirdness, offset, temperature, humidity);
        }
    }

}
