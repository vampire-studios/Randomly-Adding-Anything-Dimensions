package io.github.vampirestudios.raa_dimension;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;

/**
 * A specialized registry for
 * registration of dimensional layers.
 *
 * When an entity transitions the threshold Y-axis level,
 * it is teleported to the target dimension with the
 * specified placement logic.
 */
public class DimensionLayerRegistry {
	public static final DimensionLayerRegistry INSTANCE = new DimensionLayerRegistry();

	private final Map<ResourceKey<Level>, Tuple<Integer, ResourceKey<Level>>> TOP_ENTRIES = new HashMap<>();

	private final Map<ResourceKey<Level>, Tuple<Integer, ResourceKey<Level>>> BOTTOM_ENTRIES = new HashMap<>();

	private final Map<ResourceKey<Level>, Tuple<EntityPlacer, EntityPlacer>> PLACERS = new HashMap<>();

	/** We only want one instance of this. */
	private DimensionLayerRegistry() {}

	/** Registers a dimensional layer, with a type {@link Type},
	 * with its source dimension {@link RegistryKey<World>},
	 * transition level {@link Integer}, target dimension {@link RegistryKey<World>},
	 * and entity placing logic {@link EntityPlacer}. */
	public void register(Type type, ResourceKey<Level> dimension, int levelY, ResourceKey<Level> newDimension, EntityPlacer placer) {
		Map<ResourceKey<Level>, Tuple<Integer, ResourceKey<Level>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		ENTRIES.put(dimension, new Tuple<>(levelY, newDimension));

		if (PLACERS.containsKey(dimension)) {
			PLACERS.put(dimension, new Tuple<>(type == Type.TOP ? placer : PLACERS.get(dimension).getA(), type == Type.BOTTOM ? placer : PLACERS.get(dimension).getB()));
		} else {
			PLACERS.put(dimension, new Tuple<>(type == Type.TOP ? placer : null, type == Type.BOTTOM ? placer : null));
		}
	}

	/** Retrieves the transition level {@link Integer}
	 * for the given {@link Type} at the specified {@link RegistryKey<World>}. */
	public int getLevel(Type type, ResourceKey<Level> dimension) {
		Map<ResourceKey<Level>, Tuple<Integer, ResourceKey<Level>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		Tuple<Integer, ResourceKey<Level>> pair = ENTRIES.get(dimension);

		return pair == null ? Integer.MIN_VALUE : pair.getA();
	}

	/** Retrieves the upper or low dimension {@link RegistryKey<World>} for the given
	 * {@link Type} at the specified {@link RegistryKey<World>}. */
	public ResourceKey<Level> getDimension(Type type, ResourceKey<Level> dimension) {
		Map<ResourceKey<Level>, Tuple<Integer, ResourceKey<Level>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		Tuple<Integer, ResourceKey<Level>> pair = ENTRIES.get(dimension);

		return pair == null ? null : pair.getB();
	}

	/** Retrieves the entity placing logic {@link EntityPlacer}
	 * for the given {@link Type} at the specified {@link RegistryKey<World>}. */
	public EntityPlacer getPlacer(Type type, ResourceKey<Level> dimension) {
		return type == Type.TOP ? PLACERS.get(dimension).getA() : PLACERS.get(dimension).getB();
	}

	/** Specifies the Y-axis ordering of a dimensional layer. */
	public enum Type {
		TOP,
		BOTTOM
	}
}