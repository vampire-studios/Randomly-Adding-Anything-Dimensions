package io.github.vampirestudios.raa_dimension;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.*;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.NoiseSettingsBuilder;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_dimension.api.namegeneration.CivsLanguageManager;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.raa_dimension.config.DimensionsConfig;
import io.github.vampirestudios.raa_dimension.config.GeneralConfig;
import io.github.vampirestudios.raa_dimension.generation.dimensions.RAADimensionSkyProperties;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionNoiseSettingsData;
import io.github.vampirestudios.raa_dimension.generation.feature.portalHub.PortalShrineFeatureConfig;
import io.github.vampirestudios.raa_dimension.init.Dimensions;
import io.github.vampirestudios.raa_dimension.init.Features;
import io.github.vampirestudios.raa_dimension.init.RAAAttributes;
import io.github.vampirestudios.raa_dimension.init.Textures;
import io.github.vampirestudios.vampirelib.utils.Rands;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RAADimensionAddon implements RAAAddon {

	public static String NAME = "RAA: Dimensions";
	public static final String MOD_ID = "raa_dimensions";
	public static String MOD_VERSION = "v0.0.1-dev";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	public static final CreativeModeTab RAA_DIMENSION_BLOCKS = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "dimension_blocks"), () ->
			new ItemStack(Items.STONE));
	public static GeneralConfig CONFIG;
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
	public static HashMap<ResourceLocation, ResourceKey<Level>> dims = new HashMap<>();

	@Override
	public void onInitialize() {
		LOGGER.info(String.format("You're now running RAA: Dimensions v%s for 21w16a", MOD_VERSION));
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			for (ResourceKey<Level> registryKey : server.levelKeys()) {
				dims.put(registryKey.location(), registryKey);
			}
		});
		AutoConfig.register(GeneralConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(GeneralConfig.class).getConfig();
		DIMENSION_NUMBER = CONFIG.dimensionGenAmount;
		DimensionLanguageManager.init();
		CivsLanguageManager.init();
		Textures.init();
//		SurfaceBuilders.init();
		Features.init();
		RAAAttributes.initialize();

//		SurfaceBuilderGenerator.registerElements();
//		SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig("surface_builders/surface_builder_config");
//		if (!SURFACE_BUILDER_CONFIG.fileExist()) {
//			SURFACE_BUILDER_CONFIG.generate();
//			SURFACE_BUILDER_CONFIG.save();
//		} else {
//			SURFACE_BUILDER_CONFIG.load();
//		}

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

		Artifice.registerDataPack(new ResourceLocation(getId(), "data_pack"), serverResourcePackBuilder -> {
			Dimensions.DIMENSIONS.forEach(dimensionData -> {
				DimensionTypeBuilder dimensionTypeBuilder = new DimensionTypeBuilder()
						.bedWorks(dimensionData.getTypeData().doesBedWork())
						.piglinSafe(dimensionData.getTypeData().isPiglinSafe())
						.respawnAnchorWorks(dimensionData.getTypeData().doesRespawnAnchorWork())
						.hasRaids(dimensionData.getTypeData().hasRaids())
						.coordinate_scale(dimensionData.getTypeData().getCoordinateScale())
						.hasSkylight(dimensionData.getTypeData().hasSkyLight())
						.hasCeiling(dimensionData.getTypeData().hasCeiling())
						.ultrawarm(dimensionData.getTypeData().isUltrawarm())
						.isNatural(dimensionData.getTypeData().isNatural())
						.hasEnderDragonFight(dimensionData.getTypeData().hasEnderDragonFight())
						.height(dimensionData.getTypeData().getLogicalHeight())
						.logicalHeight(dimensionData.getTypeData().getLogicalHeight())
						.minimumY(dimensionData.getTypeData().getMinimumHeight())
						.ambientLight(dimensionData.getTypeData().getAmbientLight())
						.infiniburn(new ResourceLocation(dimensionData.getTypeData().getInfiniburn()));
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("type", "minecraft:uniform");
				JsonObject valueJsonObject = new JsonObject();
				valueJsonObject.addProperty("min_inclusive", 0);
				valueJsonObject.addProperty("max_inclusive", 7);
				jsonObject.add("value", valueJsonObject);
				dimensionTypeBuilder.add("monster_spawn_light_level", jsonObject);
				dimensionTypeBuilder.add("monster_spawn_block_light_limit", 0);
				if (dimensionData.getTypeData().hasFixedTime()) {
					dimensionTypeBuilder.fixedTime(dimensionData.getTypeData().getFixedTime());
				}
				serverResourcePackBuilder.addDimensionType(new ResourceLocation(MOD_ID, dimensionData.getId().getPath() + "_type"), dimensionTypeBuilder);
				DimensionNoiseSettingsData noiseSettingsData = dimensionData.getNoiseSettingsData();
				StateDataBuilder dimensionFluid = StateDataBuilder
						.name(noiseSettingsData.getDefaultFluid());
				if (!noiseSettingsData.getDefaultFluid().equals("minecraft:air"))
					dimensionFluid.setProperty("level", "0");
				serverResourcePackBuilder.addNoiseSettingsBuilder(new ResourceLocation(MOD_ID, dimensionData.getId().getPath() + "_noise_settings"),
						new NoiseSettingsBuilder()
								.defaultBlock(StateDataBuilder.name(noiseSettingsData.getDefaultBlock()))
								.defaultFluid(dimensionFluid)
								.aquifersEnabled(true)
								.oreVeinsEnabled(true)
								.disableMobGeneration(noiseSettingsData.disableMobGeneration())
								.seaLevel(noiseSettingsData.getSeaLevel())
								.noiseConfig(NoiseConfigBuilder.noiseConfig(
										-64,
										noiseSettingsData.getNoise().getHeight(),
										noiseSettingsData.getNoise().getSizeHorizontal(),
										noiseSettingsData.getNoise().getSizeVertical()
								))
				);
				List<BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder> biomeBuilders = new ArrayList<>();
				dimensionData.getBiomeData().forEach(dimensionBiomeData -> biomeBuilders.add(
						new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
								.biome(dimensionBiomeData.getId().toString())
								.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
										.temperature(Math.round(dimensionBiomeData.getBiomeParameters().getTemperature()))
										.humidity(Math.round(dimensionBiomeData.getBiomeParameters().getHumidity()))
										.continentalness(Math.round(dimensionBiomeData.getBiomeParameters().getContinentalness()))
										.erosion(Math.round(dimensionBiomeData.getBiomeParameters().getErosion()))
										.weirdness(Math.round(dimensionBiomeData.getBiomeParameters().getWeirdness()))
										.depth(Math.round(dimensionBiomeData.getBiomeParameters().getDepth()))
										.offset(Math.round(dimensionBiomeData.getBiomeParameters().getOffset()))
								)
				));
				serverResourcePackBuilder.addDimension(dimensionData.getId(), new DimensionBuilder()
						.dimensionType(new ResourceLocation(MOD_ID, dimensionData.getId().getPath() + "_type"))
						.noiseGenerator(ChunkGeneratorTypeBuilder.NoiseChunks()
								.multiNoiseBiomeSource(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder()
										.biomes(biomeBuilders.toArray(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder[1]))
										.preset("")
								).noiseSettings(new ResourceLocation(MOD_ID, dimensionData.getId().getPath() + "_noise_settings").toString())
						)
				);
			});
			try {
				serverResourcePackBuilder.dumpResources("testing", "data");
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		Dimensions.DIMENSIONS.forEach(dimensionData -> DimensionRenderingRegistry.registerDimensionEffects(dimensionData.getId(), new RAADimensionSkyProperties(dimensionData)));
		ResourceKey<ConfiguredFeature<?, ?>>  portalHubKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(MOD_ID, "portal_hub"));
		ResourceKey<ConfiguredFeature<?, ?>>  portalShrineKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(MOD_ID, "portal_shrine"));
		Holder<ConfiguredFeature<?, ?>> portalHub = Features.register(portalHubKey.location().getPath(), new ConfiguredFeature<>(Features.PORTAL_HUB, new NoneFeatureConfiguration()));
		Holder<ConfiguredFeature<?, ?>> portalShrine = Features.register(portalShrineKey.location().getPath(), new ConfiguredFeature<>(Features.PORTAL_SHRINE, new PortalShrineFeatureConfig(Rands.randIntRange(1, 4), Rands.randIntRange(0, 2))));

		ResourceKey<PlacedFeature>  portalHubPfKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(MOD_ID, "portal_hub_pf"));
		ResourceKey<PlacedFeature>  portalShrinePfKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(MOD_ID, "portal_shrine_pf"));
		/*Holder<PlacedFeature> portalHubPf = Features.register(portalHubPfKey.location().getPath(), new PlacedFeature(portalHub, List.of(
				PlacementUtils.countExtra(0, 0.02F, 1),
				BiomeFilter.biome(),
				InSquarePlacement.spread()
		)));
		Holder<PlacedFeature> portalShrinePf = Features.register(portalShrinePfKey.location().getPath(), new PlacedFeature(portalHub, List.of(
				PlacementUtils.countExtra(0, 0.3F, 1),
				BiomeFilter.biome(),
				InSquarePlacement.spread()
		)));
		if (CONFIG.shouldSpawnPortalHub) {
			DynamicRegistryCallback.callback(Registry.BIOME_REGISTRY).register((manager, id, biome) -> {
				Holder<Biome> biomeHolder = Holder.direct(biome);
				if (!biomeHolder.is(BiomeTags.IS_RIVER) && !biomeHolder.is(BiomeTags.IS_OCEAN)) BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.SURFACE_STRUCTURES, portalHubPfKey);
				if (!biomeHolder.is(BiomeTags.IS_RIVER) && !biomeHolder.is(BiomeTags.IS_OCEAN)) BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.SURFACE_STRUCTURES, portalShrinePfKey);
			});
		}*/

	}

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

}
