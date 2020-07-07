package io.github.vampirestudios.raa_dimension;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_core.api.name_generation.NameGenerator;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

public class RAADimensionAddon implements RAAAddon {

	public static final String MOD_ID = "raa_dimensions";

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
				String name = nameGenerator.generate();
				System.out.println("Dimension number: " + a);
				serverResourcePackBuilder.addDimensionType(new Identifier(getId(), name + "_type"), dimensionTypeBuilder -> {
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

				int finalA = a;
				serverResourcePackBuilder.addDimension(new Identifier(getId(), name), dimensionBuilder -> {
					dimensionBuilder.dimensionType(new Identifier(getId(), name + "_type"));

					dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
						noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> fixedBiomeSourceBuilder.biome("minecraft:plains"));
						noiseChunkGeneratorTypeBuilder.customSettings(generatorSettingsBuilder ->
								generatorSettingsBuilder.defaultBlock(blockStateBuilder ->
										blockStateBuilder.name(Rands.list(Arrays.asList(Registry.BLOCK.getIds().toArray())).toString())
								)
						);
						noiseChunkGeneratorTypeBuilder.presetSettings(Rands.list(CHUNK_GENERATOR_TYPES));
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
