package io.github.vampirestudios.raa_dimension;

import com.google.gson.JsonObject;
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
import io.github.vampirestudios.raa_dimension.init.SurfaceBuilders;
import io.github.vampirestudios.raa_dimension.init.Textures;
import io.github.vampirestudios.raa_dimension.mixin.SkyPropertiesAccessor;
import io.github.vampirestudios.vampirelib.utils.Rands;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

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

	@Override
	public void onInitialize() {
		AutoConfig.register(GeneralConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(GeneralConfig.class).getConfig();
		DIMENSION_NUMBER = CONFIG.dimensionGenAmount;
		DimensionLanguageManager.init();
		CivsLanguageManager.init();
		Textures.init();
		SurfaceBuilders.init();

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

		Artifice.registerDataNew(new Identifier(getId(), "data_pack"), serverResourcePackBuilder -> {
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
							.logicalHeight(dimensionData.getTypeData().getLogicalHeight())
							.ambientLight(dimensionData.getTypeData().getAmbientLight())
							.infiniburn(new Identifier(dimensionData.getTypeData().getInfiniburn()));
					if (dimensionData.getTypeData().hasFixedTime()) {
						dimensionTypeBuilder.fixedTime(dimensionData.getTypeData().getFixedTime());
					}
//					System.out.println("Dimension Type JSON: " + dimensionTypeBuilder.buildTo(new JsonObject()).toOutputString());
				});
				int sizeHorizontal = Rands.randIntRange(1,3);
				if (sizeHorizontal == 3) {
					sizeHorizontal = 4;
				}
				int finalSizeHorizontal = sizeHorizontal;
				serverResourcePackBuilder.addNoiseSettingsBuilder(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_noise_settings"), noiseSettingsBuilder -> {
					noiseSettingsBuilder.defaultBlock(stateDataBuilder -> {
						stateDataBuilder.name(dimensionData.getNoiseSettingsData().getDefaultBlock());
					}).defaultFluid(blockStateBuilder -> {
						blockStateBuilder.name(dimensionData.getNoiseSettingsData().getDefaultFluid());
						if (!dimensionData.getNoiseSettingsData().getDefaultFluid().equals("minecraft:air"))
							blockStateBuilder.setProperty("level", "0");
					}).bedrockFloorPosition(dimensionData.getNoiseSettingsData().getBedrockFloorPosition())
							.bedrockRoofPosition(dimensionData.getNoiseSettingsData().getBedrockRoofPosition())
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
										biomeParametersBuilder.altitude(Rands.randFloatRange(-1.0F, 1.0F));
										biomeParametersBuilder.humidity(Rands.randFloatRange(-1.0F, 1.0F));
										biomeParametersBuilder.offset(Rands.randFloatRange(0.0F, 1.0F));
										biomeParametersBuilder.temperature(Rands.randFloatRange(-1.0F, 1.0F));
										biomeParametersBuilder.weirdness(Rands.randFloatRange(-1.0F, 1.0F));
									});
								});
							});
							multiNoiseBiomeSourceBuilder.seed((int) Rands.getRandom().nextLong());
							multiNoiseBiomeSourceBuilder.altitudeNoise(noiseSettings -> {
								noiseSettings.firstOctave(Rands.randIntRange(1, 4));
								noiseSettings.amplitudes(Rands.randFloatRange(1.0F, 4.0F), Rands.randFloatRange(1.0F, 9.0F));
							});
							multiNoiseBiomeSourceBuilder.weirdnessNoise(noiseSettings -> {
								noiseSettings.firstOctave(Rands.randIntRange(1, 2));
								noiseSettings.amplitudes(Rands.randFloatRange(1.0F, 2.0F), Rands.randFloatRange(1.0F, 5.0F));
							});
							multiNoiseBiomeSourceBuilder.temperatureNoise(noiseSettings -> {
								noiseSettings.firstOctave(Rands.randIntRange(1, 6));
								noiseSettings.amplitudes(Rands.randFloatRange(1.0F, 6.0F), Rands.randFloatRange(1.0F, 6.0F));
							});
							multiNoiseBiomeSourceBuilder.humidityNoise(noiseSettings -> {
								noiseSettings.firstOctave(Rands.randIntRange(1, 9));
								noiseSettings.amplitudes(Rands.randFloatRange(1.0F, 9.0F), Rands.randFloatRange(1.0F, 12.0F));
							});
						});
						//						noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> fixedBiomeSourceBuilder.biome(Utils.appendToPath(dimensionData.getId(), "_biome_0").toString()));
						noiseChunkGeneratorTypeBuilder.noiseSettings(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_noise_settings").toString());
						noiseChunkGeneratorTypeBuilder.seed((int) Rands.getRandom().nextLong());
					});
					System.out.println("Dimension JSON: " + dimensionBuilder.buildTo(new JsonObject()).toOutputString());
				});
			});
		});
		Dimensions.DIMENSIONS.forEach(dimensionData -> SkyPropertiesAccessor.getBY_IDENTIFIER().put(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimensionData.getId()),
				new RAADimensionSkyProperties(dimensionData)));
	}

}
