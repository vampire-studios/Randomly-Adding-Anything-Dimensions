package io.github.vampirestudios.raa_dimension.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DynamicRegistryManagerBlame {

        public static void printUnregisteredWorldgenConfiguredStuff(RegistryAccess.Frozen imp){

            // Create a store here to minimize memory impact and let it get garbaged collected later.
            Map<String, Set<ResourceLocation>> unconfiguredStuffMap = new HashMap<>();
            Set<String> collectedPossibleIssueMods = new HashSet<>();
            HashSet<String> brokenConfiguredStuffSet = new HashSet<>();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Pattern pattern = Pattern.compile("\"(?:Name|type|location)\": *\"([a-z0-9_.-:]+)\"");

            // ConfiguredFeatures
            imp.registry(Registry.PLACED_FEATURE_REGISTRY).ifPresent(configuredFeatureRegistry ->
                    imp.registry(Registry.BIOME_REGISTRY).ifPresent(mutableRegistry -> mutableRegistry.entrySet()
                            .forEach(mapEntry -> findUnregisteredConfiguredFeatures(mapEntry, unconfiguredStuffMap, brokenConfiguredStuffSet, (WritableRegistry<PlacedFeature>) configuredFeatureRegistry, gson))));

            printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredFeature");
            extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

            if(collectedPossibleIssueMods.size() != 0){
                // Add extra info to the log.
                String errorReport = "\n\n-----------------------------------------------------------------------" +
                        "\n****************** Blame Report ******************" +
                        "\n\n This is an experimental report. It is suppose to automatically read" +
                        "\n the JSON of all the unregistered ConfiguredFeatures, ConfiguredStructures," +
                        "\n and ConfiguredCarvers. Then does its best to collect the terms that seem to" +
                        "\n state whose mod the unregistered stuff belongs to." +
                        "\n\nPossible mods responsible for unregistered stuff:\n\n" +
                        collectedPossibleIssueMods.stream().sorted().collect(Collectors.joining("\n")) +
                        "\n\n-----------------------------------------------------------------------\n\n";

                // Log it to the latest.log file as well.
                RAADimensionAddon.LOGGER.log(Level.ERROR, errorReport);
            }
            collectedPossibleIssueMods.clear();
        }

        private static void extractModNames(Map<String, Set<ResourceLocation>> unconfiguredStuffMap, Set<String> collectedPossibleIssueMods, Pattern pattern) {
            unconfiguredStuffMap.keySet()
                    .forEach(jsonString -> {
                        Matcher match = pattern.matcher(jsonString);
                        while(match.find()) {
                            if(!match.group(1).contains("minecraft:")){
                                collectedPossibleIssueMods.add(match.group(1));
                            }
                        }
                    });
            unconfiguredStuffMap.clear();
        }


        /**
         * Prints all unregistered configured features to the log.
         */
    private static void findUnregisteredConfiguredFeatures(
            Map.Entry<ResourceKey<Biome>, Biome>  mapEntry,
            Map<String, Set<ResourceLocation>> unregisteredFeatureMap,
            HashSet<String> brokenConfiguredStuffSet,
            WritableRegistry<PlacedFeature> configuredFeatureRegistry,
            Gson gson)
    {

        for(HolderSet<PlacedFeature> generationStageList : mapEntry.getValue().getGenerationSettings().features()){
            for(Holder<PlacedFeature> configuredFeatureSupplier : generationStageList){

                ResourceLocation biomeID = mapEntry.getKey().location();
                if(configuredFeatureRegistry.getKey(configuredFeatureSupplier.value()) == null &&
                        BuiltinRegistries.PLACED_FEATURE.getKey(configuredFeatureSupplier.value()) == null)
                {
                    try{
                        PlacedFeature.CODEC
                                .encode(configuredFeatureSupplier, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                                .ifPresent(configuredFeatureJSON ->
                                        cacheUnregisteredObject(
                                                configuredFeatureJSON,
                                                unregisteredFeatureMap,
                                                biomeID,
                                                gson));
                    }
                    catch(Throwable e){
                        if(!brokenConfiguredStuffSet.contains(configuredFeatureSupplier.toString())){
                            brokenConfiguredStuffSet.add(configuredFeatureSupplier.toString());

                            PlacedFeature cf = configuredFeatureSupplier.value();

                            // Getting bottommost cf way is from Quark. Very nice!
                            ConfiguredFeature<?, ?> configuredFeature = cf.feature().value();
                            Feature<?> feature = configuredFeature.feature();
                            FeatureConfiguration config = configuredFeature.config();

                            String errorReport = """

                                    ****************** Experimental Blame Report ******************

                                     Found a ConfiguredFeature that was unabled to be turned into JSON which is... bad.""" +
                                    "\n This is all the info we can get about this strange... object." +
                                    "\n Top level cf [feature:" + configuredFeatureSupplier + " | config: " + configuredFeatureSupplier.value().toString() + "]" +
                                    "\n bottomost level cf [feature:" + feature.toString() + " | config: " + config.toString() + "]" +
                                    "\n\n";

                            // Log it to the latest.log file as well.
                            RAADimensionAddon.LOGGER.log(Level.ERROR, errorReport);
                        }
                    }
                }
            }
        }
    }

    private static void cacheUnregisteredObject(
            JsonElement configuredObjectJSON,
            Map<String, Set<ResourceLocation>> unregisteredObjectMap,
            ResourceLocation biomeID,
            Gson gson)
    {
        String cfstring = gson.toJson(configuredObjectJSON);

        if(!unregisteredObjectMap.containsKey(cfstring))
            unregisteredObjectMap.put(cfstring, new HashSet<>());

        unregisteredObjectMap.get(cfstring).add(biomeID);
    }

    private static void printUnregisteredStuff(Map<String, Set<ResourceLocation>> unregisteredStuffMap, String type){
        for(Map.Entry<String, Set<ResourceLocation>> entry : unregisteredStuffMap.entrySet()){

            // Add extra info to the log.
            String errorReport = "\n****************** Blame Report ******************" +
                    "\n\n This " + type + " was found to be not registered. Look at the JSON info and try to" +
                    "\n find which mod it belongs to. Then go tell that mod owner to register their " + type +
                    "\n as otherwise, it will break other mods or datapacks that registered their stuff." +
                    "\n\n JSON info : " + entry.getKey() +
                    "\n\n Biome affected : " + entry.getValue().toString().replaceAll("(([\\w :]*,){7})", "$1\n                  ") + "\n\n";

            // Log it to the latest.log file as well.
            RAADimensionAddon.LOGGER.log(Level.ERROR, errorReport);
        }
    }
}