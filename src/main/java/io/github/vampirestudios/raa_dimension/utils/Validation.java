/*
package io.github.vampirestudios.raa_dimension.utils;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.List;
import java.util.function.Supplier;

public class Validation {

    public static void validate(RegistryAccess dynMgr) {
        RAADimensionAddon.LOGGER.info("VALIDATING FEATURES AFTER DATAPACK WAS APPLIED:");
        RAADimensionAddon.LOGGER.info("--------------------------------------------");

        // Validate registration status of configured features et al
        Registry<Biome> biomes = dynMgr.registryOrThrow(Registry.BIOME_REGISTRY);
        Registry<ConfiguredFeature<?, ?>> cfs = dynMgr.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
        biomes.forEach(biome -> {
            boolean printedBiome = false;
            for (List<Supplier<ConfiguredFeature<?, ?>>> stages : biome.getGenerationSettings().features()) {
                for (Supplier<ConfiguredFeature<?, ?>> stage : stages) {
                    ConfiguredFeature<?, ?> cf = null;
                    String error = null;
                    try {
                      cf = stage.get();
                    } catch (Throwable e) {
                        error = e.toString();
                    }
                    
                    if (cf == null) {
                        if (error == null) {
                            error = " - NULL FEATURE ENTRY FOUND!!!";
                        }
                    } else {
                        ResourceLocation fid = cfs.getKey(cf);
                        if (fid == null) {
                            RegistryReadingOps<JsonElement> ops = RegistryReadingOps.of(JsonOps.INSTANCE, dynMgr);
                            DataResult<JsonElement> encodedJson = ConfiguredFeature.DIRECT_CODEC.encodeStart(ops, cf);
                            error = " - UNRESOLVABLE: CONFIGURED " + encodedJson;
                        }
                    }

                    if (error != null) {
                        if (!printedBiome) {
                            RAADimensionAddon.LOGGER.warn(biomes.getKey(biome));
                            printedBiome = true;
                        }
                        RAADimensionAddon.LOGGER.warn(error);
                    }
                }
            }
        });
    }

}*/
