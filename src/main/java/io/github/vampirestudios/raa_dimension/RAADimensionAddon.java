package io.github.vampirestudios.raa_dimension;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_core.api.name_generation.NameGenerator;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

public class RAADimensionAddon implements RAAAddon {

	public static final String MOD_ID = "raa_dimensions";

	public static final ItemGroup RAA_DIMENSION_BLOCKS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "dimension_blocks"), () ->
			new ItemStack(Items.STONE));

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
		String[] fluids = new String[]{"minecraft:water","minecraft:air","minecraft:lava"};
		DimensionLanguageManager.init();
		List<String> CHUNK_GENERATOR_TYPES = ImmutableList.of(
				"overworld",
				"amplified",
				"nether",
				"end",
				"caves",
				"floating_islands"
		);

		Artifice.registerData(new Identifier(getId(), "data_pack"), serverResourcePackBuilder -> {
			int a = 0;
			System.out.println("Generating " + DIMENSION_NUMBER + " dimensions");
			while (a < DIMENSION_NUMBER) {
				NameGenerator nameGenerator = RAACore.CONFIG.getLanguage().getNameGenerator(DimensionLanguageManager.DIMENSION_NAME);
				String name = nameGenerator.generate().toLowerCase();
				System.out.println("Dimension name: " + name);
				serverResourcePackBuilder.addDimensionType(new Identifier(MOD_ID, name + "_type"), dimensionTypeBuilder -> {
					dimensionTypeBuilder.bedWorks(Rands.chance(DIMENSION_NUMBER))
							.piglinSafe(Rands.chance(DIMENSION_NUMBER))
							.respawnAnchorWorks(Rands.chance(DIMENSION_NUMBER))
							.hasRaids(Rands.chance(DIMENSION_NUMBER))
							.shrunk(Rands.chance(DIMENSION_NUMBER))
							.hasSkylight(Rands.chance(DIMENSION_NUMBER))
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

				serverResourcePackBuilder.addDimension(new Identifier(MOD_ID, name), dimensionBuilder -> {
					dimensionBuilder.dimensionType(new Identifier(MOD_ID, name + "_type"));

					dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
						noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> fixedBiomeSourceBuilder.biome(Rands.list(Arrays.asList(BuiltinRegistries.BIOME.getIds().toArray())).toString()));
						noiseChunkGeneratorTypeBuilder.customSettings(generatorSettingsBuilder -> {
							generatorSettingsBuilder.defaultBlock(blockStateBuilder ->
//									blockStateBuilder.name(Rands.list(Arrays.asList(Registry.BLOCK.getIds().toArray())).toString())
									blockStateBuilder.jsonString("Name", /*Rands.list(Arrays.asList(Registry.BLOCK.getIds().toArray())).toString()*/"minecraft:grass_block")
											.jsonObject("Properties", jsonArrayBuilder -> jsonArrayBuilder.buildTo(new JsonObject()))
							).defaultFluid(blockStateBuilder -> {
//								blockStateBuilder.name(Rands.list(Arrays.asList(Registry.FLUID.getIds().toArray())).toString());
								blockStateBuilder.jsonString("Name", Rands.list(Arrays.asList(fluids)))
										.jsonObject("Properties", jsonArrayBuilder -> jsonArrayBuilder.buildTo(new JsonObject()));
							}).bedrockFloorPosition(Rands.randIntRange(0,255))
									.bedrockRoofPosition(Rands.randIntRange(0,255))
									.disableMobGeneration(Rands.chance(3))
									.seaLevel(Rands.randIntRange(0,255)).noiseConfig(noiseConfigBuilder -> {
										noiseConfigBuilder.amplified(Rands.chance(3))
												.densityFactor(Rands.randFloatRange(0.0F,1F))
												.densityOffset(Rands.randIntRange(0,2))
												.height(Rands.randIntRange(0,255))
												.sizeVertical(Rands.randIntRange(1,5))
												.sizeHorizontal(Rands.randIntRange(1,5))
												.simplexSurfaceNoise(Rands.chance(3))
												.randomDensityOffset(Rands.chance(3))
												.islandNoiseOverride(Rands.chance(3))
												.bottomSlide(slideConfigBuilder -> {
													slideConfigBuilder.offset(Rands.randInt(5))
															.size(Rands.randInt(5)).target(Rands.randInt(5));
												})
												.topSlide(slideConfigBuilder -> {
													slideConfigBuilder.offset(Rands.randInt(5))
															.size(Rands.randInt(5)).target(Rands.randInt(5));
												}).sampling(noiseSamplingConfigBuilder -> {
													noiseSamplingConfigBuilder.xzFactor(Rands.randFloat(5))
															.xzScale(Rands.randFloat(5)).yFactor(Rands.randFloat(5))
															.yScale(Rands.randFloat(5));
										});
							}).structureManager(structureManagerBuilder -> {
							});
						});
//						noiseChunkGeneratorTypeBuilder.presetSettings(Rands.list(CHUNK_GENERATOR_TYPES));
						noiseChunkGeneratorTypeBuilder.seed((int) Rands.getRandom().nextLong());
					});
					/*dimensionBuilder.flatGenerator(flatChunkGeneratorTypeBuilder -> {
						flatChunkGeneratorTypeBuilder.biome((Rands.values(Registry.BIOME.getIds().toArray())).toString());

						int layerNumber = Rands.randIntRange(1, 10);
						int b = 0;
						while (b < layerNumber) {
							flatChunkGeneratorTypeBuilder.addLayer(layersBuilder -> {
								layersBuilder.block((Rands.values(Registry.BLOCK.getIds().toArray())).toString())
										.height(Rands.randIntRange(1, 10));
							});
							b++;
						}

						flatChunkGeneratorTypeBuilder.structureManager(structureManagerBuilder -> {
						});
					});*/
					System.out.println("Dimension Type JSON: " + dimensionBuilder.buildTo(new JsonObject()).toOutputString());
				});

				a++;
			}
		});
	}

}
