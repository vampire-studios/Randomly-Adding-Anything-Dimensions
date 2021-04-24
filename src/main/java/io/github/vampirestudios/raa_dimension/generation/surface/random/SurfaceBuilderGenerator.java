package io.github.vampirestudios.raa_dimension.generation.surface.random;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.name_generation.NameGenerator;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.raa_dimension.generation.surface.random.elements.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import java.util.*;

public class SurfaceBuilderGenerator {
    private static final SimpleRegistry<SurfaceBuilderHolder> SURFACE_BUILDERS = new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier(RAADimensionAddon.MOD_ID, "surface_builders")), Lifecycle.stable());
    public static SimpleRegistry<SurfaceBuilder> RANDOM_SURFACE_BUILDER = new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier(RAADimensionAddon.MOD_ID, "random_surface_builders")), Lifecycle.stable());
    private static final Map<String, Class<? extends SurfaceElement>> ID_SURFACE_ELEMENT_MAP = new HashMap<>();
    private static final WeightedList<Class<? extends SurfaceElement>> WEIGHTED_ELEMENTS = new WeightedList<>();

    public static void registerElements() {
        //grass has special spawning rules
        ID_SURFACE_ELEMENT_MAP.put(new GrassSurfaceElement().getType().toString(), GrassSurfaceElement.class);

        //TODO: randomize more parts of the elements
        registerElement(new DesertSurfaceElement(), 3);
        registerElement(new RedDesertSurfaceElement(), 3);
        registerElement(new GravelSurfaceElement(), 3);
        registerElement(new PatchyBadlandsSurfaceElement(), 3);
        registerElement(new PatchyDarkBadlandsSurfaceElement(), 3);
        registerElement(new DunesSurfaceElement(), 2);
        registerElement(new SandyDunesSurfaceElement(), 2);
        registerElement(new FloatingIslandSurfaceElement(), 2);
        registerElement(new ClassicCliffsSurfaceElement(), 2);
        registerElement(new StratifiedCliffsSurfaceElement(), 2);
        registerElement(new RandomSpiresSurfaceElement(), 1);
    }

    private static void registerElement(SurfaceElement e, int weight) {
        ID_SURFACE_ELEMENT_MAP.put(e.getType().toString(), e.getClass());
        WEIGHTED_ELEMENTS.add(e.getClass(), weight);
    }

    public static void generate() {
        Set<Identifier> names = new HashSet<>();
        for (int i = 0; i < RAADimensionAddon.CONFIG.surfaceBuilderGenAmount; i++) {
            //generate names
            NameGenerator nameGenerator = RAACore.CONFIG.getLanguage().getNameGenerator(DimensionLanguageManager.DIMENSION_NAME);
            Pair<String, Identifier> name = nameGenerator.generateUnique(names, RAADimensionAddon.MOD_ID);
            names.add(name.getRight());

            //add the surface builder to the registry
            List<SurfaceElement> elements = new ArrayList<>();
            String[] amount = RAADimensionAddon.CONFIG.surfaceBuilderSubAmount.split("-");
            for (int j = 0; j < Rands.randIntRange(Integer.parseInt(amount[0]), Integer.parseInt(amount[1])); j++) { //add 2 elements
                try { //catch exceptions because yay reflection
                    SurfaceElement e = WEIGHTED_ELEMENTS.shuffle().stream().findFirst().get().newInstance();

                    //ensure that every element is unique
                    while (elements.contains(e)) {
                        e = WEIGHTED_ELEMENTS.shuffle().stream().findFirst().get().newInstance();
                    }

                    //add to list
                    elements.add(e);
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }

            //add grass randomly
            if (Rands.randInt(10) > 2) {
                elements.add(new GrassSurfaceElement());
            }
            Registry.register(SURFACE_BUILDERS, name.getRight(), new SurfaceBuilderHolder("basic", elements));
//            SURFACE_BUILDERS.add(RegistryKey.ofRegistry(name.getRight()), new SurfaceBuilderHolder("basic", elements));
            elements.sort(Comparator.comparingInt(SurfaceElement::getPriority));

            //register the actual surface builder
            RandomSurfaceBuilder sb = new RandomSurfaceBuilder(elements);
            Registry.register(Registry.SURFACE_BUILDER, name.getRight(), sb);
            Registry.register(RANDOM_SURFACE_BUILDER, name.getRight(), sb);
        }
    }

    public static void load(JsonObject obj) {
        obj.entrySet().forEach(e -> {
            if (e.getKey().startsWith("raa_dimensions")) { //check if the key is a new surface builder key
                List<SurfaceElement> elements = new ArrayList<>();
                //blessed code to get the elements in the data
                e.getValue().getAsJsonObject().get("elements").getAsJsonArray().forEach(se -> {
                    String type = se.getAsJsonObject().get("type").getAsString();
                    try {
                        //add the element to the array
                        SurfaceElement element = ID_SURFACE_ELEMENT_MAP.get(type).newInstance();
                        //deserialize the element
                        element.deserialize(se.getAsJsonObject().get("data").getAsJsonObject());

                        elements.add(element);
                    } catch (InstantiationException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                });

                //sort the elements
                elements.sort(Comparator.comparingInt(SurfaceElement::getPriority));

                //register the surface builder
                RandomSurfaceBuilder sb = new RandomSurfaceBuilder(elements);
                Registry.register(Registry.SURFACE_BUILDER, e.getKey(), sb);
                Registry.register(RANDOM_SURFACE_BUILDER, e.getKey(), sb);
            }
        });
    }

    public static void save(JsonObject obj) {
        SURFACE_BUILDERS.getIds().forEach(id -> {
            SurfaceBuilderHolder sb = SURFACE_BUILDERS.get(id);
            if (sb == null) return;

            //add the type to the json
            JsonObject holder = new JsonObject();
            holder.addProperty("type", sb.type);

            //this holds every SurfaceElement
            JsonArray elements = new JsonArray();
            //iterate through the elements and serialize them all
            sb.elements.forEach(se -> {
                JsonObject element = new JsonObject();
                element.addProperty("type", se.getType().toString());
                JsonObject data = new JsonObject();
                se.serialize(data);
                element.add("data", data);
                elements.add(element);
            });
            //add the elements
            holder.add("elements", elements);


            obj.add(id.toString(), holder);
        });
    }

    private static class SurfaceBuilderHolder {
        private final String type;
        private final List<SurfaceElement> elements;

        private SurfaceBuilderHolder(String type, List<SurfaceElement> elements) {
            this.type = type;
            this.elements = elements;
        }
    }
}
