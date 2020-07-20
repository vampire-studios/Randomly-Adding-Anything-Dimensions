package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

import net.minecraft.util.Identifier;

public class DimensionTextureData {

    private Identifier stoneTexture;
    private Identifier stoneBricksTexture;
    private Identifier mossyStoneBricksTexture;
    private Identifier crackedStoneBricksTexture;
    private Identifier cobblestoneTexture;
    private Identifier mossyCobblestoneTexture;
    private Identifier chiseledTexture;
    private Identifier crackedChiseledTexture;
    private Identifier mossyChiseledTexture;
    private Identifier polishedTexture;
    private Identifier iceTexture;
    private Identifier moonTexture;
    private Identifier sunTexture;

    public DimensionTextureData(Identifier stoneTexture, Identifier stoneBricksTexture, Identifier mossyStoneBricksTexture, Identifier crackedStoneBricksTexture,
                                Identifier cobblestoneTexture, Identifier mossyCobblestoneTexture, Identifier chiseledTexture, Identifier crackedChiseledTexture,
                                Identifier mossyChiseledTexture, Identifier polishedTexture, Identifier iceTexture, Identifier moonTexture, Identifier sunTexture) {
        this.stoneTexture = stoneTexture;
        this.stoneBricksTexture = stoneBricksTexture;
        this.mossyStoneBricksTexture = mossyStoneBricksTexture;
        this.crackedStoneBricksTexture = crackedStoneBricksTexture;
        this.cobblestoneTexture = cobblestoneTexture;
        this.mossyCobblestoneTexture = mossyCobblestoneTexture;
        this.chiseledTexture = chiseledTexture;
        this.crackedChiseledTexture = crackedChiseledTexture;
        this.mossyChiseledTexture = mossyChiseledTexture;
        this.polishedTexture = polishedTexture;
        this.iceTexture = iceTexture;
        this.moonTexture = moonTexture;
        this.sunTexture = sunTexture;
    }

    public Identifier getStoneTexture() {
        return stoneTexture;
    }

    public Identifier getStoneBricksTexture() {
        return stoneBricksTexture;
    }

    public Identifier getMossyStoneBricksTexture() {
        return mossyStoneBricksTexture;
    }

    public Identifier getCrackedStoneBricksTexture() {
        return crackedStoneBricksTexture;
    }

    public Identifier getCobblestoneTexture() {
        return cobblestoneTexture;
    }

    public Identifier getMossyCobblestoneTexture() {
        return mossyCobblestoneTexture;
    }

    public Identifier getChiseledTexture() {
        return chiseledTexture;
    }

    public Identifier getCrackedChiseledTexture() {
        return crackedChiseledTexture;
    }

    public Identifier getMossyChiseledTexture() {
        return mossyChiseledTexture;
    }

    public Identifier getPolishedTexture() {
        return polishedTexture;
    }

    public Identifier getIceTexture() {
        return iceTexture;
    }

    public Identifier getMoonTexture() {
        return moonTexture;
    }

    public Identifier getSunTexture() {
        return sunTexture;
    }

    public static class Builder {

        private Identifier stoneTexture;
        private Identifier stoneBricksTexture;
        private Identifier mossyStoneBricksTexture;
        private Identifier crackedStoneBricksTexture;
        private Identifier cobblestoneTexture;
        private Identifier mossyCobblestoneTexture;
        private Identifier chiseledTexture;
        private Identifier crackedChiseledTexture;
        private Identifier mossyChiseledTexture;
        private Identifier polishedTexture;
        private Identifier iceTexture;
        private Identifier moonTexture;
        private Identifier sunTexture;

        public static Builder create() {
            return new Builder();
        }

        public Builder stoneTexture(Identifier stoneTexture) {
            this.stoneTexture = stoneTexture;
            return this;
        }

        public Builder stoneBricksTexture(Identifier stoneBricksTexture) {
            this.stoneBricksTexture = stoneBricksTexture;
            return this;
        }

        public Builder mossyStoneBricksTexture(Identifier mossyStoneBricksTexture) {
            this.mossyStoneBricksTexture = mossyStoneBricksTexture;
            return this;
        }

        public Builder crackedStoneBricksTexture(Identifier crackedStoneBricksTexture) {
            this.crackedStoneBricksTexture = crackedStoneBricksTexture;
            return this;
        }

        public Builder cobblestoneTexture(Identifier cobblestoneTexture) {
            this.cobblestoneTexture = cobblestoneTexture;
            return this;
        }

        public Builder mossyCobblestoneTexture(Identifier mossyCobblestoneTexture) {
            this.mossyCobblestoneTexture = mossyCobblestoneTexture;
            return this;
        }

        public Builder chiseledTexture(Identifier chiseledTexture) {
            this.chiseledTexture = chiseledTexture;
            return this;
        }

        public Builder crackedChiseledTexture(Identifier crackedChiseledTexture) {
            this.crackedChiseledTexture = crackedChiseledTexture;
            return this;
        }

        public Builder mossyChiseledTexture(Identifier mossyChiseledTexture) {
            this.mossyChiseledTexture = mossyChiseledTexture;
            return this;
        }

        public Builder polishedTexture(Identifier polishedTexture) {
            this.polishedTexture = polishedTexture;
            return this;
        }

        public Builder iceTexture(Identifier iceTexture) {
            this.iceTexture = iceTexture;
            return this;
        }

        public Builder moonTexture(Identifier moonTexture) {
            this.moonTexture = moonTexture;
            return this;
        }

        public Builder sunTexture(Identifier sunTexture) {
            this.sunTexture = sunTexture;
            return this;
        }

        public DimensionTextureData build() {
            return new DimensionTextureData(stoneTexture, stoneBricksTexture, mossyStoneBricksTexture, crackedStoneBricksTexture, cobblestoneTexture, mossyCobblestoneTexture,
                    chiseledTexture, crackedChiseledTexture, mossyChiseledTexture, polishedTexture, iceTexture, moonTexture, sunTexture);
        }

    }

}
