package io.github.vampirestudios.raa_dimension;

import net.minecraft.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

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

	private final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> TOP_ENTRIES = new HashMap<>();

	private final Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> BOTTOM_ENTRIES = new HashMap<>();

	private final Map<RegistryKey<World>, Pair<EntityPlacer, EntityPlacer>> PLACERS = new HashMap<>();

	/** We only want one instance of this. */
	private DimensionLayerRegistry() {}

	/** Registers a dimensional layer, with a type {@link Type},
	 * with its source dimension {@link RegistryKey<World>},
	 * transition level {@link Integer}, target dimension {@link RegistryKey<World>},
	 * and entity placing logic {@link EntityPlacer}. */
	public void register(Type type, RegistryKey<World> dimension, int levelY, RegistryKey<World> newDimension, EntityPlacer placer) {
		Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		ENTRIES.put(dimension, new Pair<>(levelY, newDimension));

		if (PLACERS.containsKey(dimension)) {
			PLACERS.put(dimension, new Pair<>(type == Type.TOP ? placer : PLACERS.get(dimension).getLeft(), type == Type.BOTTOM ? placer : PLACERS.get(dimension).getRight()));
		} else {
			PLACERS.put(dimension, new Pair<>(type == Type.TOP ? placer : null, type == Type.BOTTOM ? placer : null));
		}
	}

	/** Retrieves the transition level {@link Integer}
	 * for the given {@link Type} at the specified {@link RegistryKey<World>}. */
	public int getLevel(Type type, RegistryKey<World> dimension) {
		Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		Pair<Integer, RegistryKey<World>> pair = ENTRIES.get(dimension);

		return pair == null ? Integer.MIN_VALUE : pair.getLeft();
	}

	/** Retrieves the upper or low dimension {@link RegistryKey<World>} for the given
	 * {@link Type} at the specified {@link RegistryKey<World>}. */
	public RegistryKey<World> getDimension(Type type, RegistryKey<World> dimension) {
		Map<RegistryKey<World>, Pair<Integer, RegistryKey<World>>> ENTRIES = type == Type.TOP ? this.TOP_ENTRIES : this.BOTTOM_ENTRIES;

		Pair<Integer, RegistryKey<World>> pair = ENTRIES.get(dimension);

		return pair == null ? null : pair.getRight();
	}

	/** Retrieves the entity placing logic {@link EntityPlacer}
	 * for the given {@link Type} at the specified {@link RegistryKey<World>}. */
	public EntityPlacer getPlacer(Type type, RegistryKey<World> dimension) {
		return type == Type.TOP ? PLACERS.get(dimension).getLeft() : PLACERS.get(dimension).getRight();
	}

	/** Specifies the Y-axis ordering of a dimensional layer. */
	public enum Type {
		TOP,
		BOTTOM
	}
}