package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

import net.minecraft.resources.ResourceLocation;

public class DimensionTextureData {

    private ResourceLocation stoneTexture;
    private ResourceLocation tilesTexture;
    private ResourceLocation stoneBricksTexture;
    private ResourceLocation mossyStoneBricksTexture;
    private ResourceLocation crackedStoneBricksTexture;
    private ResourceLocation cobblestoneTexture;
    private ResourceLocation mossyCobblestoneTexture;
    private ResourceLocation chiseledTexture;
    private ResourceLocation crackedChiseledTexture;
    private ResourceLocation mossyChiseledTexture;
    private ResourceLocation polishedTexture;
    private ResourceLocation iceTexture;
    private ResourceLocation moonTexture;
    private ResourceLocation sunTexture;

    public DimensionTextureData(ResourceLocation stoneTexture, ResourceLocation tilesTexture, ResourceLocation stoneBricksTexture, ResourceLocation mossyStoneBricksTexture, ResourceLocation crackedStoneBricksTexture,
                                ResourceLocation cobblestoneTexture, ResourceLocation mossyCobblestoneTexture, ResourceLocation chiseledTexture, ResourceLocation crackedChiseledTexture,
                                ResourceLocation mossyChiseledTexture, ResourceLocation polishedTexture, ResourceLocation iceTexture, ResourceLocation moonTexture, ResourceLocation sunTexture) {
        this.stoneTexture = stoneTexture;
        this.tilesTexture = tilesTexture;
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

    public ResourceLocation getStoneTexture() {
        return stoneTexture;
    }

    public ResourceLocation getTilesTexture() {
        return tilesTexture;
    }

    public ResourceLocation getStoneBricksTexture() {
        return stoneBricksTexture;
    }

    public ResourceLocation getMossyStoneBricksTexture() {
        return mossyStoneBricksTexture;
    }

    public ResourceLocation getCrackedStoneBricksTexture() {
        return crackedStoneBricksTexture;
    }

    public ResourceLocation getCobblestoneTexture() {
        return cobblestoneTexture;
    }

    public ResourceLocation getMossyCobblestoneTexture() {
        return mossyCobblestoneTexture;
    }

    public ResourceLocation getChiseledTexture() {
        return chiseledTexture;
    }

    public ResourceLocation getCrackedChiseledTexture() {
        return crackedChiseledTexture;
    }

    public ResourceLocation getMossyChiseledTexture() {
        return mossyChiseledTexture;
    }

    public ResourceLocation getPolishedTexture() {
        return polishedTexture;
    }

    public ResourceLocation getIceTexture() {
        return iceTexture;
    }

    public ResourceLocation getMoonTexture() {
        return moonTexture;
    }

    public ResourceLocation getSunTexture() {
        return sunTexture;
    }

    public static class Builder {

        private ResourceLocation stoneTexture;
        private ResourceLocation tilesTexture;
        private ResourceLocation stoneBricksTexture;
        private ResourceLocation mossyStoneBricksTexture;
        private ResourceLocation crackedStoneBricksTexture;
        private ResourceLocation cobblestoneTexture;
        private ResourceLocation mossyCobblestoneTexture;
        private ResourceLocation chiseledTexture;
        private ResourceLocation crackedChiseledTexture;
        private ResourceLocation mossyChiseledTexture;
        private ResourceLocation polishedTexture;
        private ResourceLocation iceTexture;
        private ResourceLocation moonTexture;
        private ResourceLocation sunTexture;

        public static Builder create() {
            return new Builder();
        }

        public Builder stoneTexture(ResourceLocation stoneTexture) {
            this.stoneTexture = stoneTexture;
            return this;
        }

        public Builder tilesTexture(ResourceLocation tilesTexture) {
            this.tilesTexture = tilesTexture;
            return this;
        }

        public Builder stoneBricksTexture(ResourceLocation stoneBricksTexture) {
            this.stoneBricksTexture = stoneBricksTexture;
            return this;
        }

        public Builder mossyStoneBricksTexture(ResourceLocation mossyStoneBricksTexture) {
            this.mossyStoneBricksTexture = mossyStoneBricksTexture;
            return this;
        }

        public Builder crackedStoneBricksTexture(ResourceLocation crackedStoneBricksTexture) {
            this.crackedStoneBricksTexture = crackedStoneBricksTexture;
            return this;
        }

        public Builder cobblestoneTexture(ResourceLocation cobblestoneTexture) {
            this.cobblestoneTexture = cobblestoneTexture;
            return this;
        }

        public Builder mossyCobblestoneTexture(ResourceLocation mossyCobblestoneTexture) {
            this.mossyCobblestoneTexture = mossyCobblestoneTexture;
            return this;
        }

        public Builder chiseledTexture(ResourceLocation chiseledTexture) {
            this.chiseledTexture = chiseledTexture;
            return this;
        }

        public Builder crackedChiseledTexture(ResourceLocation crackedChiseledTexture) {
            this.crackedChiseledTexture = crackedChiseledTexture;
            return this;
        }

        public Builder mossyChiseledTexture(ResourceLocation mossyChiseledTexture) {
            this.mossyChiseledTexture = mossyChiseledTexture;
            return this;
        }

        public Builder polishedTexture(ResourceLocation polishedTexture) {
            this.polishedTexture = polishedTexture;
            return this;
        }

        public Builder iceTexture(ResourceLocation iceTexture) {
            this.iceTexture = iceTexture;
            return this;
        }

        public Builder moonTexture(ResourceLocation moonTexture) {
            this.moonTexture = moonTexture;
            return this;
        }

        public Builder sunTexture(ResourceLocation sunTexture) {
            this.sunTexture = sunTexture;
            return this;
        }

        public DimensionTextureData build() {
            return new DimensionTextureData(stoneTexture, tilesTexture, stoneBricksTexture, mossyStoneBricksTexture, crackedStoneBricksTexture, cobblestoneTexture, mossyCobblestoneTexture,
                    chiseledTexture, crackedChiseledTexture, mossyChiseledTexture, polishedTexture, iceTexture, moonTexture, sunTexture);
        }

    }

}
