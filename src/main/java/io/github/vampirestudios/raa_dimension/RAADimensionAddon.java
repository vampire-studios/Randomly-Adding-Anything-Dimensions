package io.github.vampirestudios.raa_dimension;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_dimension.api.namegeneration.CivsLanguageManager;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.raa_dimension.config.DimensionsConfig;
import io.github.vampirestudios.raa_dimension.config.GeneralConfig;
import io.github.vampirestudios.raa_dimension.config.SurfaceBuilderConfig;
import io.github.vampirestudios.raa_dimension.fabric.api.DimensionRenderingRegistry;
import io.github.vampirestudios.raa_dimension.generation.dimensions.RAADimensionSkyProperties;
import io.github.vampirestudios.raa_dimension.generation.feature.portalHub.PortalShrineFeatureConfig;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceBuilderGenerator;
import io.github.vampirestudios.raa_dimension.init.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class RAADimensionAddon implements RAAAddon {

	public static String NAME = "RAA: Dimensions";
	public static final String MOD_ID = "raa_dimensions";
	public static String MOD_VERSION = "v0.0.1-dev";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

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
		LOGGER.info(String.format("You're now running RAA: Dimensions v%s for 21w16a", MOD_VERSION));
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
		Features.init();
		RAAAttributes.initialize();

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
							.bedrockRoofPosition(-2147483648)
							.deepslateEnabled(false)
							.noiseCavesEnabled(true)
							.aquifersEnabled(true)
							.noodleCavesEnabled(true)
							.oreVeinsEnabled(true)
							.minSurfaceLevel(0)
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
										biomeParametersBuilder.altitude(Math.round(dimensionBiomeData.getBiomeParameters().getAltitude()));
										biomeParametersBuilder.humidity(Math.round(dimensionBiomeData.getBiomeParameters().getHumidity()));
										biomeParametersBuilder.offset(Math.round(dimensionBiomeData.getBiomeParameters().getOffset()));
										biomeParametersBuilder.temperature(Math.round(dimensionBiomeData.getBiomeParameters().getTemperature()));
										biomeParametersBuilder.weirdness(Math.round(dimensionBiomeData.getBiomeParameters().getWeirdness()));
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
			try {
				serverResourcePackBuilder.dumpResources("testing", "data");
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		Dimensions.DIMENSIONS.forEach(dimensionData -> {
			RegistryKey<DimensionType> REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimensionData.getId());
			DimensionRenderingRegistry.INSTANCE.setSkyProperty(REGISTRY_KEY, new RAADimensionSkyProperties(dimensionData), true);
		});

		RegistryKey<ConfiguredFeature<?, ?>>  portalHubKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(MOD_ID, "portal_hub"));
		RegistryKey<ConfiguredFeature<?, ?>>  portalShrineKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(MOD_ID, "portal_shrine"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, portalHubKey.getValue(), Features.PORTAL_HUB
				.configure(new DefaultFeatureConfig()).decorate(Decorator.COUNT_EXTRA
						.configure(new CountExtraDecoratorConfig(0, 0.002F, 1))));
		if (CONFIG.shouldSpawnPortalHub) {
			DynamicRegistryCallback.callback(Registry.BIOME_KEY).register((manager, id, biome) -> {
				if (biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.RIVER) BiomesRegistry.registerFeature(manager, biome, GenerationStep.Feature.SURFACE_STRUCTURES, portalHubKey);
				if (biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.RIVER) BiomesRegistry.registerFeature(manager, biome, GenerationStep.Feature.SURFACE_STRUCTURES, () -> Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "portal_shrine" + Rands.getRandom().nextInt()), Features.PORTAL_SHRINE
						.configure(new PortalShrineFeatureConfig(Rands.randIntRange(1, 4), Rands.randIntRange(0, 2))).decorate(Decorator.COUNT_EXTRA
								.configure(new CountExtraDecoratorConfig(0, 0.03F, 1)))));
			});
		}

	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

}
