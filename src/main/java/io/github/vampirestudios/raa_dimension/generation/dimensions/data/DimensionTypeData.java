package io.github.vampirestudios.raa_dimension.generation.dimensions.data;

public class DimensionTypeData {

    private boolean does_bed_work;
    private boolean piglin_safe;
    private boolean respawn_anchor_works;
    private boolean hasRaids;
    private float coordinate_scale;
    private boolean has_sky_light;
    private boolean has_ceiling;
    private boolean ultrawarm;
    private boolean natural;
    private boolean has_ender_dragon_fight;
    private int logical_height;
    private int minimum_height;
    private float ambient_light;
    private String infiniburn;
    private boolean has_fixed_time;
    private int fixed_time;

    public DimensionTypeData(boolean does_bed_work, boolean piglin_safe, boolean respawn_anchor_works, boolean hasRaids, float coordinate_scale, boolean has_sky_light, boolean has_ceiling, boolean ultrawarm, boolean natural, boolean has_ender_dragon_fight, int logical_height, int minimum_height, float ambient_light, String infiniburn, boolean has_fixed_time, int fixed_time) {
        this.does_bed_work = does_bed_work;
        this.piglin_safe = piglin_safe;
        this.respawn_anchor_works = respawn_anchor_works;
        this.hasRaids = hasRaids;
        this.coordinate_scale = coordinate_scale;
        this.has_sky_light = has_sky_light;
        this.has_ceiling = has_ceiling;
        this.ultrawarm = ultrawarm;
        this.natural = natural;
        this.has_ender_dragon_fight = has_ender_dragon_fight;
        this.logical_height = logical_height;
        this.minimum_height = minimum_height;
        this.ambient_light = ambient_light;
        this.infiniburn = infiniburn;
        this.has_fixed_time = has_fixed_time;
        this.fixed_time = fixed_time;
    }

    public boolean doesBedWork() {
        return does_bed_work;
    }

    public boolean isPiglinSafe() {
        return piglin_safe;
    }

    public boolean doesRespawnAnchorWork() {
        return respawn_anchor_works;
    }

    public boolean hasRaids() {
        return hasRaids;
    }

    public float getCoordinateScale() {
        return coordinate_scale;
    }

    public boolean hasSkyLight() {
        return has_sky_light;
    }

    public boolean hasCeiling() {
        return has_ceiling;
    }

    public boolean isUltrawarm() {
        return ultrawarm;
    }

    public boolean isNatural() {
        return natural;
    }

    public boolean hasEnderDragonFight() {
        return has_ender_dragon_fight;
    }

    public int getLogicalHeight() {
        return logical_height;
    }

    public int getMinimumHeight() {
        return minimum_height;
    }

    public float getAmbientLight() {
        return ambient_light;
    }

    public String getInfiniburn() {
        return infiniburn;
    }

    public boolean hasFixedTime() {
        return has_fixed_time;
    }

    public int getFixedTime() {
        return fixed_time;
    }

    public static class Builder {

        private boolean does_bed_work;
        private boolean piglin_safe;
        private boolean respawn_anchor_works;
        private boolean hasRaids;
        private float coordinate_scale;
        private boolean has_sky_light;
        private boolean has_ceiling;
        private boolean ultrawarm;
        private boolean natural;
        private boolean has_ender_dragon_fight;
        private int logical_height;
        private int minimum_height;
        private float ambient_light;
        private String infiniburn;
        private boolean has_fixed_time;
        private int fixed_time;

        public static DimensionTypeData.Builder create() {
            return new DimensionTypeData.Builder();
        }

        public Builder doesBedsWork(boolean does_bed_work) {
            this.does_bed_work = does_bed_work;
            return this;
        }

        public Builder isPiglinSafe(boolean piglin_safe) {
            this.piglin_safe = piglin_safe;
            return this;
        }

        public Builder doesRegenAnchorsWork(boolean respawn_anchor_works) {
            this.respawn_anchor_works = respawn_anchor_works;
            return this;
        }

        public Builder shouldHaveRaids(boolean hasRaids) {
            this.hasRaids = hasRaids;
            return this;
        }

        public Builder coordinateScale(float coordinate_scale) {
            this.coordinate_scale = coordinate_scale;
            return this;
        }

        public Builder hasSkyLight(boolean has_sky_light) {
            this.has_sky_light = has_sky_light;
            return this;
        }

        public Builder hasCeiling(boolean has_ceiling) {
            this.has_ceiling = has_ceiling;
            return this;
        }

        public Builder isUltrawarm(boolean ultrawarm) {
            this.ultrawarm = ultrawarm;
            return this;
        }

        public Builder isNatural(boolean natural) {
            this.natural = natural;
            return this;
        }

        public Builder hasEnderDragonFight(boolean has_ender_dragon_fight) {
            this.has_ender_dragon_fight = has_ender_dragon_fight;
            return this;
        }

        public Builder logicalHeight(int logical_height) {
            this.logical_height = logical_height;
            return this;
        }

        public Builder minimumHeight(int minimum_height) {
            this.minimum_height = minimum_height;
            return this;
        }

        public Builder ambientLight(float ambient_light) {
            this.ambient_light = ambient_light;
            return this;
        }

        public Builder infiniburnTag(String infiniburn) {
            this.infiniburn = infiniburn;
            return this;
        }

        public Builder hasFixedTime(boolean has_fixed_time) {
            this.has_fixed_time = has_fixed_time;
            return this;
        }

        public Builder fixedTime(int fixed_time) {
            this.fixed_time = fixed_time;
            return this;
        }

        public DimensionTypeData build() {
            return new DimensionTypeData(does_bed_work, piglin_safe, respawn_anchor_works, hasRaids, coordinate_scale, has_sky_light, has_ceiling, ultrawarm, natural, has_ender_dragon_fight, logical_height, minimum_height, ambient_light, infiniburn, has_fixed_time, fixed_time);
        }

    }

}