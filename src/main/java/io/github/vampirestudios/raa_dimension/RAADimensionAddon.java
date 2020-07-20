package io.github.vampirestudios.raa_dimension;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_dimension.api.namegeneration.CivsLanguageManager;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.raa_dimension.config.DimensionsConfig;
import io.github.vampirestudios.raa_dimension.config.GeneralConfig;
import io.github.vampirestudios.raa_dimension.config.SurfaceBuilderConfig;
import io.github.vampirestudios.raa_dimension.generation.surface.random.SurfaceBuilderGenerator;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import io.github.vampirestudios.raa_dimension.init.SurfaceBuilders;
import io.github.vampirestudios.raa_dimension.init.Textures;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.Utils;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

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

	public static final int DIMENSION_NUMBER = 10;

	@Override
	public void onInitialize() {
		AutoConfig.register(GeneralConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(GeneralConfig.class).getConfig();
		String[] fluids = new String[]{"minecraft:water","minecraft:air","minecraft:lava"};
		DimensionLanguageManager.init();
		CivsLanguageManager.init();
		List<String> CHUNK_GENERATOR_TYPES = ImmutableList.of(
				"overworld",
				"amplified",
				"nether",
				"end",
				"caves",
				"floating_islands"
		);
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

		Artifice.registerData(new Identifier(getId(), "data_pack"), serverResourcePackBuilder -> {
			Dimensions.DIMENSIONS.forEach(dimensionData -> {
				serverResourcePackBuilder.addDimensionType(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_type"), dimensionTypeBuilder -> {
					dimensionTypeBuilder.bedWorks(dimensionData.canSleep())
							.piglinSafe(Rands.chance(DIMENSION_NUMBER))
							.respawnAnchorWorks(Rands.chance(DIMENSION_NUMBER))
							.hasRaids(Rands.chance(DIMENSION_NUMBER))
							.shrunk(Rands.chance(DIMENSION_NUMBER))
							.hasSkylight(dimensionData.getCustomSkyInformation().hasSkyLight())
							.hasCeiling(Rands.chance(DIMENSION_NUMBER))
							.ultrawarm(Rands.chance(DIMENSION_NUMBER))
							.natural(Rands.chance(DIMENSION_NUMBER))
							.hasEnderDragonFight(Rands.chance(DIMENSION_NUMBER))
							.logicalHeight(Rands.randIntRange(70, 256))
							.ambientLight(Rands.randFloatRange(0F, 0.16F))
							.infiniburn(BlockTags.INFINIBURN_OVERWORLD.getId());
					if (Rands.chance(DIMENSION_NUMBER)) {
						dimensionTypeBuilder.fixedTime(Rands.randIntRange(0, 16000));
					}
					System.out.println("Dimension Type JSON: " + dimensionTypeBuilder.buildTo(new JsonObject()).toOutputString());
				});
				serverResourcePackBuilder.addDimension(dimensionData.getId(), dimensionBuilder -> {
					dimensionBuilder.dimensionType(new Identifier(MOD_ID, dimensionData.getId().getPath() + "_type"));
					dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
						noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> fixedBiomeSourceBuilder.biome(Utils.appendToPath(dimensionData.getId(), "_biome_0").toString()));
						noiseChunkGeneratorTypeBuilder.customSettings(generatorSettingsBuilder -> {
							generatorSettingsBuilder.defaultBlock(stateDataBuilder -> {
								stateDataBuilder.name(Utils.appendToPath(dimensionData.getId(), "_stone").toString());
							}).defaultFluid(blockStateBuilder -> {
								blockStateBuilder.jsonString("Name", Rands.list(Arrays.asList(fluids)))
										.jsonObject("Properties", jsonArrayBuilder -> jsonArrayBuilder.buildTo(new JsonObject()));
							}).bedrockFloorPosition(Rands.randIntRange(0, 255))
									.bedrockRoofPosition(Rands.randIntRange(0, 255))
									.disableMobGeneration(Rands.chance(3))
									.seaLevel(Rands.randIntRange(0, 255))
									.noiseConfig(noiseConfigBuilder ->
											noiseConfigBuilder.amplified(Rands.chance(3))
													.densityFactor(Rands.randFloatRange(0.0F, 1F))
													.densityOffset(Rands.randFloatRange(0, 1))
													.height(Rands.randIntRange(0, 255))
													.sizeVertical(Rands.randIntRange(1, 4))
													.sizeHorizontal(Rands.randIntRange(1, 4))
													.simplexSurfaceNoise(Rands.chance(3))
													.randomDensityOffset(Rands.chance(3))
													.islandNoiseOverride(Rands.chance(3))
													.bottomSlide(slideConfigBuilder -> {
														slideConfigBuilder.offset(Rands.randInt(5))
																.size(Rands.randInt(5))
																.target(Rands.randInt(5));
													}).topSlide(slideConfigBuilder -> {
												slideConfigBuilder.offset(Rands.randInt(5))
														.size(Rands.randInt(5))
														.target(Rands.randInt(5));
											}).sampling(noiseSamplingConfigBuilder -> {
												noiseSamplingConfigBuilder.xzFactor(Rands.randFloatRange(0.001F, 5))
														.xzScale(Rands.randFloatRange(0.001F, 5))
														.yFactor(Rands.randFloatRange(0.001F, 5))
														.yScale(Rands.randFloatRange(0.001F, 1.0F));
											})
									).structureManager(structureManagerBuilder -> {

							});
						});
						noiseChunkGeneratorTypeBuilder.seed((int) Rands.getRandom().nextLong());
					});
				});
			});
		});
	}

}
