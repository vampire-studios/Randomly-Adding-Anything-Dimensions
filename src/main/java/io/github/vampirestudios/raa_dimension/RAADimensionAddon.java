package io.github.vampirestudios.raa_dimension;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_dimension.api.namegeneration.CivsLanguageManager;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.raa_dimension.config.DimensionsConfig;
import io.github.vampirestudios.raa_dimension.config.GeneralConfig;
import io.github.vampirestudios.raa_dimension.config.SurfaceBuilderConfig;
import io.github.vampirestudios.raa_dimension.generation.dimensions.RAADimensionSkyProperties;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceBuilderGenerator;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import io.github.vampirestudios.raa_dimension.init.Features;
import io.github.vampirestudios.raa_dimension.init.SurfaceBuilders;
import io.github.vampirestudios.raa_dimension.init.Textures;
import io.github.vampirestudios.raa_dimension.mixin.SkyPropertiesAccessor;
import io.github.vampirestudios.vampirelib.utils.Rands;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.mixin.biome.VanillaLayeredBiomeSourceAccessor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.HashMap;

public class RAADimensionAddon implements RAAAddon {

	public static final String MOD_ID = "raa_dimensions";

	public static final ItemGroup RAA_DIMENSION_BLOCKS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "dimension_blocks"), () ->
			new ItemStack(Items.STONE));
	public static GeneralConfig CONFIG;
	public static SurfaceBuilderConfig SURFACE_BUILDER_CONFIG;
	public static DimensionsConfig DIMENSIONS_CONFIG;

	@Override
	public String[] shouldLoadAfter() {
		return new String[]{};
	}

	@Override
	public String getId() {
		return MOD_ID;
	}

	public static int DIMENSION_NUMBER = 0;
	public static HashMap<Identifier, RegistryKey<World>> dims = new HashMap<>();

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			for (RegistryKey<World> registryKey : server.getWorldRegistryKeys()) {
				dims.put(registryKey.getValue(), registryKey);
			}
		});
		AutoConfig.register(GeneralConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(GeneralConfig.class).getConfig();
		DIMENSION_NUMBER = CONFIG.dimensionGenAmount;
		DimensionLanguageManager.init();
		CivsLanguageManager.init();
		Textures.init();
		SurfaceBuilders.init();
		Textures.init();
		Features.init();

		SurfaceBuilderGenerator.registerElements();
		SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig("surface_builders/surface_builder_config");
		if (!SURFACE_BUILDER_CONFIG.fileExist()) {
			SURFACE_BUILDER_CONFIG.generate();
			SURFACE_BUILDER_CONFIG.save();
		} else {
			SURFACE_BUILDER_CONFIG.load();
		}

		DIMENSIONS_CONFIG = new DimensionsConfig("dimensions/dimension_config");
		if (CONFIG.dimensionGenAmount > 0) {
			if (!DIMENSIONS_CONFIG.fileExist()) {
				DIMENSIONS_CONFIG.generate();
				DIMENSIONS_CONFIG.save();
			} else {
				DIMENSIONS_CONFIG.load();
			}
		}
		Dimensions.createDimensions();

		Artifice.registerDataPack(new Identifier(getId(), "data_pack"), serverResourcePackBuilder -> {
			Dimensions.DIMENSIONS.forEach(dimensionData -> {
				serverResourcePackBuilder.addDimensionType(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_type"), dimensionTypeBuilder -> {
					dimensionTypeBuilder.bedWorks(dimensionData.getTypeData().doesBedWork())
							.piglinSafe(dimensionData.getTypeData().isPiglinSafe())
							.respawnAnchorWorks(dimensionData.getTypeData().doesRespawnAnchorWork())
							.hasRaids(dimensionData.getTypeData().isHasRaids())
							.coordinate_scale(dimensionData.getTypeData().getCoordinateScale())
							.hasSkylight(dimensionData.getTypeData().hasSkyLight())
							.hasCeiling(dimensionData.getTypeData().hasCeiling())
							.ultrawarm(dimensionData.getTypeData().isUltrawarm())
							.natural(dimensionData.getTypeData().isNatural())
							.hasEnderDragonFight(dimensionData.getTypeData().hasEnderDragonFight())
							.height(dimensionData.getTypeData().getLogicalHeight())
							.logicalHeight(dimensionData.getTypeData().getLogicalHeight())
							.ambientLight(dimensionData.getTypeData().getAmbientLight())
							.infiniburn(new Identifier(dimensionData.getTypeData().getInfiniburn()))
							.minimumY(-64);
					if (dimensionData.getTypeData().hasFixedTime()) {
						dimensionTypeBuilder.fixedTime(dimensionData.getTypeData().getFixedTime());
					}
				});
				serverResourcePackBuilder.addNoiseSettingsBuilder(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_noise_settings"), noiseSettingsBuilder -> {
					noiseSettingsBuilder.defaultBlock(stateDataBuilder -> {
						stateDataBuilder.name(dimensionData.getNoiseSettingsData().getDefaultBlock());
					}).defaultFluid(blockStateBuilder -> {
						blockStateBuilder.name(dimensionData.getNoiseSettingsData().getDefaultFluid());
						if (!dimensionData.getNoiseSettingsData().getDefaultFluid().equals("minecraft:air"))
							blockStateBuilder.setProperty("level", "0");
					}).bedrockFloorPosition(0)
							.bedrockRoofPosition(0)
							.deepslateEnabled(false)
							.noiseCavesEnabled(true)
							.aquifersEnabled(true)
							.oreVeinsEnabled(true)
							.minSurfaceLevel(Integer.MIN_VALUE)
							.disableMobGeneration(dimensionData.getNoiseSettingsData().disableMobGeneration())
							.seaLevel(dimensionData.getNoiseSettingsData().getSeaLevel())
							.noiseConfig(noiseConfigBuilder -> {
								noiseConfigBuilder.amplified(dimensionData.getNoiseSettingsData().getNoise().isAmplified())
										.densityFactor(dimensionData.getNoiseSettingsData().getNoise().getDensityFactor())
										.densityOffset(dimensionData.getNoiseSettingsData().getNoise().getDensityOffset())
										.height(dimensionData.getNoiseSettingsData().getNoise().getHeight())
										.sizeVertical(dimensionData.getNoiseSettingsData().getNoise().getSizeVertical())
										.sizeHorizontal(dimensionData.getNoiseSettingsData().getNoise().getSizeHorizontal())
										.simplexSurfaceNoise(dimensionData.getNoiseSettingsData().getNoise().isSimplexSurfaceNoise())
										.randomDensityOffset(dimensionData.getNoiseSettingsData().getNoise().hasRandomDensityOffset())
										.islandNoiseOverride(dimensionData.getNoiseSettingsData().getNoise().hasIslandNoiseOverride())
										.minimumY(-64)
										.bottomSlide(slideConfigBuilder -> {
											slideConfigBuilder.offset(dimensionData.getNoiseSettingsData().getNoise().getBottomSlide().getOffset())
													.size(dimensionData.getNoiseSettingsData().getNoise().getBottomSlide().getSize())
													.target(dimensionData.getNoiseSettingsData().getNoise().getBottomSlide().getTarget());
								}).topSlide(slideConfigBuilder -> {
									slideConfigBuilder.offset(dimensionData.getNoiseSettingsData().getNoise().getTopSlide().getOffset())
											.size(dimensionData.getNoiseSettingsData().getNoise().getTopSlide().getSize())
											.target(dimensionData.getNoiseSettingsData().getNoise().getTopSlide().getTarget());
								}).sampling(noiseSamplingConfigBuilder -> {
									noiseSamplingConfigBuilder.xzFactor(dimensionData.getNoiseSettingsData().getNoise().getSampling().getXzFactor())
											.xzScale(dimensionData.getNoiseSettingsData().getNoise().getSampling().getXzScale())
											.yFactor(dimensionData.getNoiseSettingsData().getNoise().getSampling().getYFactor())
											.yScale(dimensionData.getNoiseSettingsData().getNoise().getSampling().getYScale());
								});
							}).structureManager(structureManagerBuilder -> {
					});
				});
				serverResourcePackBuilder.addDimension(dimensionData.getId(), dimensionBuilder -> {
					dimensionBuilder.dimensionType(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_type"));
					dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
						noiseChunkGeneratorTypeBuilder.multiNoiseBiomeSource(multiNoiseBiomeSourceBuilder -> {
							multiNoiseBiomeSourceBuilder.addBiome(biomeBuilder -> {
								dimensionData.getBiomeData().forEach(dimensionBiomeData -> {
									biomeBuilder.biome(dimensionBiomeData.getId().toString());
									biomeBuilder.parameters(biomeParametersBuilder -> {
										biomeParametersBuilder.altitude(dimensionBiomeData.getBiomeParameters().getAltitude());
										biomeParametersBuilder.humidity(dimensionBiomeData.getBiomeParameters().getHumidity());
										biomeParametersBuilder.offset(dimensionBiomeData.getBiomeParameters().getOffset());
										biomeParametersBuilder.temperature(dimensionBiomeData.getBiomeParameters().getTemperature());
										biomeParametersBuilder.weirdness(dimensionBiomeData.getBiomeParameters().getWeirdness());
									});
								});
							});
							multiNoiseBiomeSourceBuilder.seed((int) Rands.getRandom().nextLong());
							multiNoiseBiomeSourceBuilder.altitudeNoise(noiseSettings -> {
								noiseSettings.firstOctave(dimensionData.getAltitudeNoise().getFirstOctave());
								noiseSettings.amplitudes(dimensionData.getAltitudeNoise().getAmplitudes());
							});
							multiNoiseBiomeSourceBuilder.weirdnessNoise(noiseSettings -> {
								noiseSettings.firstOctave(dimensionData.getWeirdnessNoise().getFirstOctave());
								noiseSettings.amplitudes(dimensionData.getWeirdnessNoise().getAmplitudes());
							});
							multiNoiseBiomeSourceBuilder.temperatureNoise(noiseSettings -> {
								noiseSettings.firstOctave(dimensionData.getTemperatureNoise().getFirstOctave());
								noiseSettings.amplitudes(dimensionData.getTemperatureNoise().getAmplitudes());
							});
							multiNoiseBiomeSourceBuilder.humidityNoise(noiseSettings -> {
								noiseSettings.firstOctave(dimensionData.getHumidityNoise().getFirstOctave());
								noiseSettings.amplitudes(dimensionData.getHumidityNoise().getAmplitudes());
							});
						});
						noiseChunkGeneratorTypeBuilder.noiseSettings(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_noise_settings").toString());
						noiseChunkGeneratorTypeBuilder.seed((int) Rands.getRandom().nextLong());
					});
				});
			});
		});
		Dimensions.DIMENSIONS.forEach(dimensionData -> SkyPropertiesAccessor.getBY_IDENTIFIER().put(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimensionData.getId()),
				new RAADimensionSkyProperties(dimensionData)));

		RegistryKey<ConfiguredFeature<?, ?>>  portalHubKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(MOD_ID, "portal_hub"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, portalHubKey.getValue(), Features.PORTAL_HUB
				.configure(new DefaultFeatureConfig()).decorate(Decorator.COUNT_EXTRA
						.configure(new CountExtraDecoratorConfig(0, 0.3F, 1))));
		if (CONFIG.shouldSpawnPortalHub) {
			BiomeModifications.addFeature((context) -> {
				RegistryKey<Biome> biomeKey = context.getBiomeKey();
				Biome biome = BuiltinRegistries.BIOME.get(biomeKey.getValue());
				return biomeKey == BiomeKeys.BAMBOO_JUNGLE_HILLS || biomeKey == BiomeKeys.BAMBOO_JUNGLE || VanillaLayeredBiomeSourceAccessor.getBIOMES().contains(biomeKey)
						|| biome.getCategory() != Biome.Category.OCEAN;
			}, GenerationStep.Feature.SURFACE_STRUCTURES, portalHubKey);
		}

	}

}
