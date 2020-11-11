package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class NoiseSettings {

    private int firstOctave;
    private float[] amplitudes;

    public NoiseSettings(int firstOctave, float[] amplitudes) {
        this.firstOctave = firstOctave;
        this.amplitudes = amplitudes;
    }

    public int getFirstOctave() {
        return firstOctave;
    }

    public float[] getAmplitudes() {
        return amplitudes;
    }

    public static class Builder {

        private int firstOctave;
        private float[] amplitudes;

        public static Builder builder() {
            return new Builder();
        }

        public Builder firstOctave(int firstOctave) {
            this.firstOctave = firstOctave;
            return this;
        }

        public Builder amplitudes(float... amplitudes) {
            this.amplitudes = amplitudes;
            return this;
        }

        public NoiseSettings create() {
            return new NoiseSettings(firstOctave, amplitudes);
        }

    }

}
