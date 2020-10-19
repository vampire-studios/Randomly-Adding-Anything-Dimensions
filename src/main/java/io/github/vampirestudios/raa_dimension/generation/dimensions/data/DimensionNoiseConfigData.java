package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class DimensionNoiseConfigData {

    private boolean amplified;
    private float density_factor;
    private float density_offset;
    private int height;
    private int size_vertical;
    private int size_horizontal;
    private boolean simplex_surface_noise;
    private boolean random_density_offset;
    private boolean island_noise_override;
    private SlideData bottom_slide;
    private SlideData top_slide;
    private Sampling sampling;

    public static class SlideData {

        private int offset;
        private int size;
        private int target;

        public SlideData(int offset, int size, int target) {
            this.offset = offset;
            this.size = size;
            this.target = target;
        }

        public int getOffset() {
            return offset;
        }

        public int getSize() {
            return size;
        }

        public int getTarget() {
            return target;
        }

        public static class Builder {

            private int offset;
            private int size;
            private int target;

            public static SlideData.Builder create() {
                return new SlideData.Builder();
            }

            public Builder offset(int offset) {
                this.offset = offset;
                return this;
            }

            public Builder size(int size) {
                this.size = size;
                return this;
            }

            public Builder target(int target) {
                this.target = target;
                return this;
            }

            public SlideData build() {
                return new SlideData(offset, size, target);
            }
        }
    }

    public static class Sampling {

        private int xz_factor;
        private int xz_scale;
        private int y_factor;
        private int y_scale;

        public Sampling(int xz_factor, int xz_scale, int y_factor, int y_scale) {
            this.xz_factor = xz_factor;
            this.xz_scale = xz_scale;
            this.y_factor = y_factor;
            this.y_scale = y_scale;
        }

        public int getXzFactor() {
            return xz_factor;
        }

        public int getXzScale() {
            return xz_scale;
        }

        public int getYFactor() {
            return y_factor;
        }

        public int getYScale() {
            return y_scale;
        }

        public static class Builder {

            private int xz_factor;
            private int xz_scale;
            private int y_factor;
            private int y_scale;

            public static Sampling.Builder create() {
                return new Sampling.Builder();
            }

            public Builder xzFactor(int xz_factor) {
                this.xz_factor = xz_factor;
                return this;
            }

            public Builder xzScale(int xz_scale) {
                this.xz_scale = xz_scale;
                return this;
            }

            public Builder yFactor(int y_factor) {
                this.y_factor = y_factor;
                return this;
            }

            public Builder yScale(int y_scale) {
                this.y_scale = y_scale;
                return this;
            }

            public Sampling build() {
                return new Sampling(xz_factor, xz_scale, y_factor, y_scale);
            }
        }
    }

    public DimensionNoiseConfigData(boolean amplified, float density_factor, float density_offset, int height, int size_vertical, int size_horizontal, boolean simplex_surface_noise, boolean random_density_offset, boolean island_noise_override, SlideData bottom_slide, SlideData top_slide, Sampling sampling) {
        this.amplified = amplified;
        this.density_factor = density_factor;
        this.density_offset = density_offset;
        this.height = height;
        this.size_vertical = size_vertical;
        this.size_horizontal = size_horizontal;
        this.simplex_surface_noise = simplex_surface_noise;
        this.random_density_offset = random_density_offset;
        this.island_noise_override = island_noise_override;
        this.bottom_slide = bottom_slide;
        this.top_slide = top_slide;
        this.sampling = sampling;
    }

    public boolean isAmplified() {
        return amplified;
    }

    public float getDensityFactor() {
        return density_factor;
    }

    public float getDensityOffset() {
        return density_offset;
    }

    public int getHeight() {
        return height;
    }

    public int getSizeVertical() {
        return size_vertical;
    }

    public int getSizeHorizontal() {
        return size_horizontal;
    }

    public boolean isSimplexSurfaceNoise() {
        return simplex_surface_noise;
    }

    public boolean hasRandomDensityOffset() {
        return random_density_offset;
    }

    public boolean hasIslandNoiseOverride() {
        return island_noise_override;
    }

    public SlideData getBottomSlide() {
        return bottom_slide;
    }

    public SlideData getTopSlide() {
        return top_slide;
    }

    public Sampling getSampling() {
        return sampling;
    }

    public static class Builder {

        private boolean amplified;
        private float density_factor;
        private float density_offset;
        private int height;
        private int size_vertical;
        private int size_horizontal;
        private boolean simplex_surface_noise;
        private boolean random_density_offset;
        private boolean island_noise_override;
        private SlideData bottom_slide;
        private SlideData top_slide;
        private Sampling sampling;

        public static DimensionNoiseConfigData.Builder create() {
            return new DimensionNoiseConfigData.Builder();
        }

        public Builder amplified(boolean amplified) {
            this.amplified = amplified;
            return this;
        }

        public Builder densityFactor(float density_factor) {
            this.density_factor = density_factor;
            return this;
        }

        public Builder densityOffset(float density_offset) {
            this.density_offset = density_offset;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder sizeVertical(int size_vertical) {
            this.size_vertical = size_vertical;
            return this;
        }

        public Builder sizeHorizontal(int size_horizontal) {
            this.size_horizontal = size_horizontal;
            return this;
        }

        public Builder simplexSurfaceNoise(boolean simplex_surface_noise) {
            this.simplex_surface_noise = simplex_surface_noise;
            return this;
        }

        public Builder randomDensityOffset(boolean random_density_offset) {
            this.random_density_offset = random_density_offset;
            return this;
        }

        public Builder islandNoiseOverride(boolean island_noise_override) {
            this.island_noise_override = island_noise_override;
            return this;
        }

        public Builder bottomSlide(SlideData bottom_slide) {
            this.bottom_slide = bottom_slide;
            return this;
        }

        public Builder topSlide(SlideData top_slide) {
            this.top_slide = top_slide;
            return this;
        }

        public Builder sampling(Sampling sampling) {
            this.sampling = sampling;
            return this;
        }

        public DimensionNoiseConfigData build() {
            return new DimensionNoiseConfigData(amplified, density_factor, density_offset, height, size_vertical, size_horizontal, simplex_surface_noise, random_density_offset, island_noise_override, bottom_slide, top_slide, sampling);
        }

    }

}
