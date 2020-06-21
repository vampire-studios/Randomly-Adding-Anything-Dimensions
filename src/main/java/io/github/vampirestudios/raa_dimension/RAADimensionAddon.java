package io.github.vampirestudios.raa_dimension;

import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RAADimensionAddon implements RAAAddon {
	@Override
	public String[] shouldLoadAfter() {
		return new String[0];
	}

	@Override
	public String getId() {
		return "raa_dimension";
	}

	public static final int DIMENSION_NUMBER = 5;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Artifice.registerData(new Identifier(getId(), "data_pack"), serverResourcePackBuilder -> {
			int a = 0;
			System.out.println("Generating " + DIMENSION_NUMBER + " dimensions");
			while (a < DIMENSION_NUMBER) {
				System.out.println("Dimension number: " + a);
				serverResourcePackBuilder.addDimensionType(new Identifier(getId(), "test_" + a + "_type"), dimensionTypeBuilder -> {
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
							.ambientLight(Rands.randFloatRange(0F,0.16F))
							.infiniburn(BlockTags.INFINIBURN_OVERWORLD.getId());
					if (Rands.chance(DIMENSION_NUMBER)) {
						dimensionTypeBuilder.fixedTime(Rands.randIntRange(0,16000));
					}
					System.out.println("Dimension Type JSON: " + dimensionTypeBuilder.buildTo(new JsonObject()).toOutputString());
				});

				int finalA = a;
				serverResourcePackBuilder.addDimension(new Identifier(getId(), "test_" + a), dimensionBuilder -> {
					dimensionBuilder.dimensionType(new Identifier(getId(), "test_" + finalA + "_type"));

					dimensionBuilder.flatGenerator(flatChunkGeneratorTypeBuilder -> {
						flatChunkGeneratorTypeBuilder.biome(((Identifier)Rands.values(Registry.BIOME.getIds().toArray())).toString());

						int layerNumber = Rands.randIntRange(1,10);
						int b = 0;
						while (b < layerNumber) {
							flatChunkGeneratorTypeBuilder.addLayer(layersBuilder -> {
								layersBuilder.block(((Identifier)Rands.values(Registry.BLOCK.getIds().toArray())).toString())
										.height(Rands.randIntRange(1,10));
							});
							b++;
						}

						flatChunkGeneratorTypeBuilder.structureManager(structureManagerBuilder -> {
						});
					});
					System.out.println("Dimension Type JSON: " + dimensionBuilder.buildTo(new JsonObject()).toOutputString());
				});

				a++;
			}
		});
	}
}
