package io.github.vampirestudios.raa_dimension.history;

import io.github.vampirestudios.raa_dimension.utils.Utils;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

/**
 * A holder for dimension data for use in civilization simulation, converted to a proper dimension later
 *
 * @author SuperCoder79
 */
public class ProtoDimension {
    private Tuple<String, ResourceLocation> name;
    private int flags;
    private float temperature;
    private float scale;
    private double x, y;
    private HashMap<String, Double> civilizationInfluences;

    //The least amount of dimension data needed for civilization simulation
    public ProtoDimension(Tuple<String, ResourceLocation> name, int flags, float temperature, float scale) {
        this.name = name;
        this.flags = flags;
        this.temperature = temperature;
        this.scale = scale;
        civilizationInfluences = new HashMap<>();
    }

    public void setDead() {
        this.flags |= Utils.DEAD;
    }

    public void setAbandoned() {
        this.flags |= Utils.ABANDONED;
    }

    public void setCivilized() {
        this.flags |= Utils.CIVILIZED;
    }

    public void removeLush() {
        this.flags &= ~Utils.LUSH;
    }

    public void addInfluence(String name, double percent) {
        civilizationInfluences.put(name, percent);
    }

    public void setXandY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Tuple<String, ResourceLocation> getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getScale() {
        return scale;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public HashMap<String, Double> getCivilizationInfluences() {
        return civilizationInfluences;
    }
}
