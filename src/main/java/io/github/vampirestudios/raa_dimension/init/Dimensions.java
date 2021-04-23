package io.github.vampirestudios.raa_dimension.init;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.name_generation.NameGenerator;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.api.enums.TextureTypes;
import io.github.vampirestudios.raa_dimension.api.namegeneration.DimensionLanguageManager;
import io.github.vampirestudios.raa_dimension.blocks.DimensionalBlock;
import io.github.vampirestudios.raa_dimension.blocks.DimensionalStone;
import io.github.vampirestudios.raa_dimension.blocks.PortalBlock;
import io.github.vampirestudios.raa_dimension.generation.dimensions.CustomDimensionalBiome;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.*;
import io.github.vampirestudios.raa_dimension.history.Civilization;
import io.github.vampirestudios.raa_dimension.history.ProtoDimension;
import io.github.vampirestudios.raa_dimension.item.RAABlockItemAlt;
import io.github.vampirestudios.raa_dimension.utils.RegistryUtils;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import io.github.vampirestudios.vampirelib.blocks.SlabBaseBlock;
import io.github.vampirestudios.vampirelib.blocks.StairsBaseBlock;
import io.github.vampirestudios.vampirelib.blocks.WallBaseBlock;
import io.github.vampirestudios.vampirelib.utils.Color;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

public class Dimensions {
    public static final Set<Identifier> DIMENSION_NAMES = new HashSet<>();
    public static final DefaultedRegistry<DimensionData> DIMENSIONS = new DefaultedRegistry<>("raa_dimensions:dimensions", RegistryKey.ofRegistry(new Identifier(RAADimensionAddon.MOD_ID, "dimensions")), Lifecycle.experimental());

    public static void generate() {
        //pre generation of dimensions: basic data, flags, and name
        //This is only the data needed for civilization simulation
        ArrayList<ProtoDimension> protoDimensions = new ArrayList<>();
        for (int a = 0; a < RAADimensionAddon.CONFIG.dimensionGenAmount; a++) {
            float temperature = Rands.randFloat(2.0F);
            int flags = generateDimensionFlags();

            NameGenerator nameGenerator = RAACore.CONFIG.getLanguage().getNameGenerator(DimensionLanguageManager.DIMENSION_NAME);
            Pair<String, Identifier> name = nameGenerator.generateUnique(DIMENSION_NAMES, RAADimensionAddon.MOD_ID);
            DIMENSION_NAMES.add(name.getRight());

            protoDimensions.add(new ProtoDimension(name, flags, temperature, Rands.randFloat(2F)));
        }

        for (ProtoDimension dimension : protoDimensions) {
            dimension.setXandY(Rands.randFloatRange(0, 1), Rands.randFloatRange(0, 1));
        }

        //perform the civilization handling

        //generate the civilizations
        ArrayList<Civilization> civs = new ArrayList<>();
        Set<Identifier> civNames = new HashSet<>();
        Set<ProtoDimension> usedDimensions = new HashSet<>();
        for (int i = 0; i < 15; i++) {
            NameGenerator nameGenerator = RAACore.CONFIG.getLanguage().getNameGenerator(DimensionLanguageManager.DIMENSION_NAME);
            Pair<String, Identifier> name = nameGenerator.generateUnique(civNames, RAADimensionAddon.MOD_ID);
            civNames.add(name.getRight());
            ProtoDimension generatedDimension = Rands.list(protoDimensions);
            if (usedDimensions.contains(generatedDimension)) continue;
            else usedDimensions.add(generatedDimension);
            civs.add(new Civilization(WordUtils.capitalizeFully(name.getLeft()), generatedDimension));
        }

        //tick the civs and get their influence
        civs.forEach(Civilization::simulate);

        for (Civilization civ : civs) {

            //tech level 0 civs get no influence
            if (civ.getTechLevel() == 0) continue;

            for (ProtoDimension dimension : protoDimensions) {
                if (dimension != civ.getHomeDimension()) {
                    //get distance to set the influence radius
                    double d = Utils.dist(dimension.getX(), dimension.getY(), civ.getHomeDimension().getX(), civ.getHomeDimension().getY());
                    if (d <= civ.getInfluenceRadius()) { //if the dimension is within the influence radius, it has an influence
                        double percent = (civ.getInfluenceRadius() - d) / civ.getInfluenceRadius();
                        dimension.addInfluence(civ.getName(), percent);

                        //modify the dimension based on the civ tech level and influence
                        if (percent > 0.40) {
                            if (civ.getTechLevel() >= 2) if (Rands.chance(5)) dimension.setAbandoned();
                        }
                        if (percent > 0.60) {
                            if (civ.getTechLevel() >= 2) if (Rands.chance(4)) dimension.setAbandoned();
                            if (civ.getTechLevel() >= 3) if (Rands.chance(5)) dimension.setDead();
                        }
                        if (percent > 0.80) {
                            if (civ.getTechLevel() >= 2) if (Rands.chance(3)) dimension.setAbandoned();
                            if (civ.getTechLevel() >= 3) if (Rands.chance(4)) dimension.setDead();
                        }
                        if (percent > 0.70) {
                            if (civ.getTechLevel() >= 3) dimension.setCivilized();
                        }
                    }
                } else {
                    //a civ's home dimension has 100% influence by that civ
                    dimension.addInfluence(civ.getName(), 1.0);
                }

                //Ensure that both dead and lush flags don't coexist
                if (Utils.checkBitFlag(dimension.getFlags(), Utils.DEAD) && Utils.checkBitFlag(dimension.getFlags(), Utils.LUSH))
                    dimension.removeLush();
            }
        }

        //post generation of dimensions: do everything to actually register the dimension
        for (ProtoDimension dimension : protoDimensions) {
            int difficulty = 0;
            int flags = dimension.getFlags();
            Pair<String, Identifier> name = dimension.getName();
            float hue = Rands.randFloatRange(0, 1.0F);
            float foliageColor = hue + Rands.randFloatRange(-0.15F, 0.15F);
            float stoneColor = hue + Rands.randFloatRange(-0.45F, 0.45F);
            float fogHue = hue + 0.3333f;
            float skyHue = fogHue + 0.3333f;

            float saturation = Rands.randFloatRange(0.5F, 1.0F);
            float stoneSaturation = Rands.randFloatRange(0.2F, 0.6F);
            if (Utils.checkBitFlag(flags, Utils.DEAD)) {
                saturation = Rands.randFloatRange(0.0F, 0.2F);
                stoneSaturation = saturation;
                difficulty += 2;
                if (Utils.checkBitFlag(flags, Utils.CIVILIZED)) difficulty++;
            }
            if (Utils.checkBitFlag(flags, Utils.LUSH)) saturation = Rands.randFloatRange(0.7F, 1.0F);
            if (Utils.checkBitFlag(flags, Utils.CORRUPTED)) difficulty += 2;
            if (Utils.checkBitFlag(flags, Utils.MOLTEN)) difficulty += 2;
            if (Utils.checkBitFlag(flags, Utils.DRY)) difficulty += 2;
            if (Utils.checkBitFlag(flags, Utils.TECTONIC)) difficulty++;
            float value = Rands.randFloatRange(0.5F, 1.0F);
            Color GRASS_COLOR = new Color(Color.HSBtoRGB(hue, saturation, value));
            Color FOLIAGE_COLOR = new Color(Color.HSBtoRGB(foliageColor, saturation, value));
            Color FOG_COLOR = new Color(Color.HSBtoRGB(fogHue, saturation, value));
            Color SKY_COLOR = new Color(Color.HSBtoRGB(skyHue, saturation, value));
            Color WATER_COLOR = new Color(Color.HSBtoRGB(skyHue, saturation + Rands.randFloatRange(0.3F, 1.0F), value + Rands.randFloatRange(0.3F, 1.0F)));
            Color STONE_COLOR = new Color(Color.HSBtoRGB(stoneColor, stoneSaturation, value));
            Color MOON_COLOR = new Color(Color.HSBtoRGB(hue, saturation, value));
            Color SUN_COLOR = new Color(Color.HSBtoRGB(hue, saturation, value));

            Color BLOOD_MOON = new Color(Color.HSBtoRGB(0F, 100F, 71F));
            Color BLUE_MOON = new Color(Color.HSBtoRGB(196F, 69F, 65F));
            Color VENUS = new Color(Color.HSBtoRGB(345F, 2.7F, 58.04F));

            /*DimensionChunkGenerators gen = Utils.randomCG(Rands.randIntRange(0, 100));
            if (gen == DimensionChunkGenerators.FLOATING) difficulty++;
            if (gen == CAVES) difficulty += 2;*/
            float scale = dimension.getScale();
            if (scale > 0.8) difficulty++;
            if (scale > 1.6) difficulty++;
            Pair<Integer, HashMap<String, int[]>> difficultyAndMobs = generateDimensionMobs(flags, difficulty);

            float gravity = 1F;
            if (Rands.chance(4)) {
                gravity = Rands.randFloatRange(0.75F, 1.25F);
            } else if(Rands.chance(8)) {
                gravity = Rands.randFloatRange(0.25F, 1.75F);
            }

            if (Rands.chance(10)) {
                MOON_COLOR = BLOOD_MOON;
            } else if (Rands.chance(60)) {
                MOON_COLOR = BLUE_MOON;
            } else if (Rands.chance(100)) {
                MOON_COLOR = VENUS;
            }

            int stoneAmount = Rands.randIntRange(1, 3);

            DimensionData.Builder builder = DimensionData.Builder.create(name.getRight(), WordUtils.capitalizeFully(name.getLeft()))
                    .canSleep(Rands.chance(4))
                    .waterVaporize(Rands.chance(100))
                    .shouldRenderFog(Rands.chance(40))
//                    .chunkGenerator(gen)
                    .flags(flags)
                    .difficulty(difficultyAndMobs.getLeft())
                    .mobs(difficultyAndMobs.getRight())
                    .civilizationInfluences(dimension.getCivilizationInfluences())
                    .cloudHeight(Rands.randFloatRange(80F, 256F))
                    .stoneHardness(Rands.randFloatRange(0.2f, 5f), Rands.randFloatRange(3, 18))
                    .stoneJumpHeight(Rands.randFloatRange(1.0F, 10.0F))
                    .gravity(gravity);

            DimensionTextureData texturesInformation = DimensionTextureData.Builder.create()
                    .stoneTexture(Rands.list(TextureTypes.STONE_TEXTURES))
                    .stoneBricksTexture(Rands.list(TextureTypes.STONE_BRICKS_TEXTURES))
                    .mossyStoneBricksTexture(Rands.list(TextureTypes.MOSSY_STONE_BRICKS_TEXTURES))
                    .crackedStoneBricksTexture(Rands.list(TextureTypes.CRACKED_STONE_BRICKS_TEXTURES))
                    .cobblestoneTexture(Rands.list(TextureTypes.COBBLESTONE_TEXTURES))
                    .mossyCobblestoneTexture(Rands.list(TextureTypes.MOSSY_COBBLESTONE_TEXTURES))
                    .chiseledTexture(Rands.list(TextureTypes.CHISELED_STONE_TEXTURES))
                    .mossyChiseledTexture(Rands.list(TextureTypes.MOSSY_CHISELED_STONE_TEXTURES))
                    .crackedChiseledTexture(Rands.list(TextureTypes.CRACKED_CHISELED_STONE_TEXTURES))
                    .polishedTexture(Rands.list(TextureTypes.POLISHED_STONE_TEXTURES))
                    .iceTexture(Rands.list(TextureTypes.ICE_TEXTURES))
                    .sunTexture(Rands.list(TextureTypes.SUNS))
                    .moonTexture(Rands.list(TextureTypes.MOONS))
                    .build();
            builder.texturesInformation(texturesInformation);

            //TODO: make proper number generation

            for (int i = 0; i < Rands.randIntRange(1, 20); i++) {
                float grassColor = hue + Rands.randFloatRange(-0.25f, 0.25f);

                SurfaceBuilder<?> surfaceBuilder = Utils.newRandomSurfaceBuilder();
                TernarySurfaceConfig surfaceConfig = Utils.randomSurfaceBuilderConfig();

                List<CarverType> carvers = new ArrayList<>();

                //cave generation
                if (!Rands.chance(5)) { //80% chance of normal caves
                    carvers.add(CarverType.CAVES);
                }

                //cave generation
                if (!Rands.chance(10)) { //80% chance of normal caves
                    carvers.add(CarverType.CREVICES);
                }

                if (!Rands.chance(4)) { //75% chance of normal ravines
                    carvers.add(CarverType.RAVINES);
                }

                if (Rands.chance(10)) { //10% chance of cave cavity
                    carvers.add(CarverType.CAVE_CAVITY);
                }

                if (Rands.chance(3)) { //33% chance of teardrops
                    carvers.add(CarverType.TEARDROPS);
                }

                if (Rands.chance(4)) { //25% chance of vertical caves
                    carvers.add(CarverType.VERTICAL);
                }

                if (Rands.chance(10)) { //10% chance of big rooms
                    carvers.add(CarverType.BIG_ROOM);
                }

                BiomeParameters biomeParameters = BiomeParameters.Builder.builder()
                        .altitude(Rands.randFloatRange(-1.0F, 1.0F))
                        .humidity(Rands.randFloatRange(-1.0F, 1.0F))
                        .offset(Rands.randFloatRange(0.0F, 1.0F))
                        .temperature(Rands.randFloatRange(-1.0F, 1.0F))
                        .weirdness(Rands.randFloatRange(-1.0F, 1.0F))
                        .create();

                DimensionBiomeData biomeData = DimensionBiomeData.Builder.create(Utils.addSuffixToPath(name.getRight(), "_biome" + "_" + i), name.getLeft())
                        .biomeParameters(biomeParameters)
                        .depth(Rands.randFloatRange(-1F, 3F))
                        .scale(Math.max(scale + Rands.randFloatRange(-0.75f, 0.75f), 0)) //ensure the scale is never below 0
                        .temperature(dimension.getTemperature() + Rands.randFloatRange(-0.5f, 0.5f))
                        .downfall(Rands.randFloat(1F))
                        .waterColor(WATER_COLOR.getColor())
                        .grassColor(new Color(Color.HSBtoRGB(grassColor, saturation, value)).getColor())
                        .foliageColor(new Color(Color.HSBtoRGB(grassColor + Rands.randFloatRange(-0.1f, 0.1f), saturation, value)).getColor())
//                        .treeType(treeType)
//                        .treeData(treeDataList)
                        .largeSkeletonTreeChance(Rands.randFloatRange(0, 0.5F))
                        .spawnsCratersInNonCorrupted(Rands.chance(4))
                        //TODO: make these based on civ tech level
                        .campfireChance(Rands.randFloatRange(0.003F, 0.005F))
                        .outpostChance(Rands.randFloatRange(0.001F, 0.003F))
                        .towerChance(Rands.randFloatRange(0.001F, 0.0015F))
                        .hasMushrooms(Rands.chance(6))
                        .hasMossyRocks(Rands.chance(8))
                        .nonCorruptedCratersChance(Rands.randFloatRange(0, 0.05F))
                        .corruptedCratersChance(Rands.randFloatRange(0, 0.05F))
                        .surfaceBuilder(Registry.SURFACE_BUILDER.getId(surfaceBuilder))
                        .surfaceConfig(Utils.fromConfigToIdentifier(surfaceConfig))
                        .carvers(carvers)
                        .build();
                builder.biome(biomeData);
            }

            DimensionColorPalette colorPalette = DimensionColorPalette.Builder.create()
                    .skyColor(SKY_COLOR.getColor())
                    .grassColor(GRASS_COLOR.getColor())
                    .fogColor(FOG_COLOR.getColor())
                    .foliageColor(FOLIAGE_COLOR.getColor())
                    .stoneColor(STONE_COLOR.getColor()).build();
            builder.colorPalette(colorPalette);

            DimensionCustomSkyInformation customSkyInformation = DimensionCustomSkyInformation.Builder.create()
                    .hasSky(!Rands.chance(2))
                    .customSun(Rands.chance(2))
                    .sunSize(Rands.randFloatRange(30F, 120F))
                    .sunTint(SUN_COLOR.getColor())
                    .customMoon(Rands.chance(2))
                    .moonSize(Rands.randFloatRange(20F, 80F))
                    .moonTint(MOON_COLOR.getColor()).build();
            builder.customSkyInformation(customSkyInformation);

            DimensionTypeData typeData = DimensionTypeData.Builder.create()
                    .doesBedsWork(Rands.chance(4))
                    .isPiglinSafe(Rands.chance(100))
                    .doesRegenAnchorsWork(Rands.chance(20))
                    .shouldHaveRaids(Rands.chance(90))
                    .coordinateScale(Rands.randFloatRange(1.0F, 3.0F))
                    .hasSkyLight(Rands.chance(1))
                    .hasCeiling(Rands.chance(10))
                    .isUltrawarm(Rands.chance(10))
                    .isNatural(Rands.chance(10))
                    .hasEnderDragonFight(Rands.chance(1000))
                    .logicalHeight(Rands.randIntRange(78 / 16, 1024 / 16) * 16)
                    .ambientLight(Rands.randFloatRange(0F, 0.16F))
                    .infiniburnTag(BlockTags.INFINIBURN_OVERWORLD.getId().toString())
                    .hasFixedTime(Rands.chance(10))
                    .fixedTime(Rands.randIntRange(0, 24000))
                    .build();
            builder.dimensionType(typeData);

            NoiseSettings altitudeNoise = NoiseSettings.Builder.builder()
                    .firstOctave(-7)
                    .amplitudes(1, 1)
                    .create();
            builder.altitudeNoise(altitudeNoise);

            NoiseSettings weirdnessNoise = NoiseSettings.Builder.builder()
                    .firstOctave(-7)
                    .amplitudes(1, 1)
                    .create();
            builder.weirdnessNoise(weirdnessNoise);

            NoiseSettings temperatureNoise = NoiseSettings.Builder.builder()
                    .firstOctave(-7)
                    .amplitudes(1, 1)
                    .create();
            builder.temperatureNoise(temperatureNoise);

            NoiseSettings humidityNoise = NoiseSettings.Builder.builder()
                    .firstOctave(-7)
                    .amplitudes(1, 1)
                    .create();
            builder.humidityNoise(humidityNoise);

            DimensionNoiseSettingsData.Builder noiseSettingsData = DimensionNoiseSettingsData.Builder.create()
                    .defaultDlock(io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(name.getRight(), "_stone").toString())
                    .defaultFluid(Rands.list(ImmutableList.of(
                            "minecraft:water",
                            "minecraft:lava",
                            "minecraft:air"
                    )))
                    .disableMobGeneration(Rands.chance(10));

            int sizeHorizontal = Rands.randIntRange(1,3);
            if (sizeHorizontal == 3) {
                sizeHorizontal = 4;
            }
            int finalSizeHorizontal = sizeHorizontal;
            float densityFactor = Rands.randFloatRange(0.5F,3.0F);
            if (densityFactor == 3.0F) {
                densityFactor = 4.0F;
            }
            float finalDensityFactor = densityFactor;
            DimensionNoiseConfigData.Builder noiseConfigData = DimensionNoiseConfigData.Builder.create()
                    .amplified(Rands.chance(3))
                    .densityFactor(finalDensityFactor)
                    .densityOffset(Rands.randFloatRange(-0.25F, -1.0F))
                    .height(Rands.randIntRange(0, 1024 / 16) * 16)
                    .sizeVertical(Rands.randIntRange(1, 4))
                    .sizeHorizontal(finalSizeHorizontal)
                    .simplexSurfaceNoise(Rands.chance(10))
                    .islandNoiseOverride(Rands.chance(20))
                    .randomDensityOffset(Rands.chance(3));

            DimensionNoiseConfigData.SlideData bottomSlideData = DimensionNoiseConfigData.SlideData.Builder.create()
                    .offset(Rands.randInt(5))
                    .size(Rands.randInt(5))
                    .target(Rands.randInt(5))
                    .build();
            noiseConfigData.bottomSlide(bottomSlideData);

            DimensionNoiseConfigData.SlideData topSlideData = DimensionNoiseConfigData.SlideData.Builder.create()
                    .offset(Rands.randInt(5))
                    .size(Rands.randIntRange(0, 255))
                    .target(Rands.randInt(5))
                    .build();
            noiseConfigData.topSlide(topSlideData);

            DimensionNoiseConfigData.Sampling sampling = DimensionNoiseConfigData.Sampling.Builder.create()
                    .xzFactor(80)
                    .xzScale(1)
                    .yFactor(160)
                    .yScale(1)
                    .build();
            noiseConfigData.sampling(sampling);

            noiseSettingsData.noise(noiseConfigData.build());
            builder.noiseSettings(noiseSettingsData.build());

            DimensionData dimensionData = builder.build();

            Registry.register(DIMENSIONS, dimensionData.getId(), dimensionData);
        }
    }

    public static void createDimensions() {
        DIMENSIONS.forEach(dimensionData -> {
            Identifier dimensionDataId = dimensionData.getId();

            Block stoneBlock = RegistryUtils.register(new DimensionalStone(dimensionData), Utils.addSuffixToPath(dimensionDataId,
                    "_stone"), RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stone");

            RegistryUtils.register(new StairsBaseBlock(stoneBlock.getDefaultState()), Utils.addSuffixToPath(dimensionDataId, "_stone_stairs"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneStairs");
            RegistryUtils.register(new SlabBaseBlock(Block.Settings.copy(Blocks.STONE_SLAB)), Utils.addSuffixToPath(dimensionDataId, "_stone_slab"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneSlab");
            RegistryUtils.register(new WallBaseBlock(Block.Settings.copy(Blocks.COBBLESTONE_WALL)), Utils.addSuffixToPath(dimensionDataId, "_stone_wall"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneWall");

            Block stoneBrick = RegistryUtils.register(new DimensionalBlock(), Utils.addSuffixToPath(dimensionDataId, "_stone_bricks"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneBricks");
            RegistryUtils.register(new StairsBaseBlock(stoneBrick.getDefaultState()), Utils.addSuffixToPath(dimensionDataId, "_stone_brick_stairs"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneBrickStairs");
            RegistryUtils.register(new SlabBaseBlock(Block.Settings.copy(Blocks.STONE_SLAB)), Utils.addSuffixToPath(dimensionDataId, "_stone_brick_slab"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneBrickSlab");
            RegistryUtils.register(new WallBaseBlock(Block.Settings.copy(Blocks.COBBLESTONE_WALL)), Utils.addSuffixToPath(dimensionDataId, "_stone_brick_wall"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "stoneBrickWall");
            RegistryUtils.register(new DimensionalBlock(), Utils.addPrefixAndSuffixToPath(dimensionDataId, "mossy_", "_stone_bricks"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "mossyStoneBricks");
            RegistryUtils.register(new DimensionalBlock(), Utils.addPrefixAndSuffixToPath(dimensionDataId, "cracked_", "_stone_bricks"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "crackedStoneBricks");
            Block cobblestone = RegistryUtils.register(new DimensionalBlock(), new Identifier(RAADimensionAddon.MOD_ID,
                            dimensionData.getId().getPath().toLowerCase() + "_cobblestone"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "cobblestone");
            RegistryUtils.register(new StairsBaseBlock(cobblestone.getDefaultState()), Utils.addSuffixToPath(dimensionDataId, "_cobblestone_stairs"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "cobblestoneStairs");
            RegistryUtils.register(new SlabBaseBlock(Block.Settings.copy(Blocks.STONE_SLAB)), Utils.addSuffixToPath(dimensionDataId, "_cobblestone_slab"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "cobblestoneSlab");
            RegistryUtils.register(new WallBaseBlock(Block.Settings.copy(Blocks.COBBLESTONE_WALL)), Utils.addSuffixToPath(dimensionDataId, "_cobblestone_wall"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "cobblestoneWall");
            RegistryUtils.register(new DimensionalBlock(), Utils.addPrefixAndSuffixToPath(dimensionDataId, "mossy_", "_cobblestone"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "mossyCobblestone");
            RegistryUtils.register(new DimensionalBlock(), new Identifier(RAADimensionAddon.MOD_ID,
                            "chiseled_" + dimensionData.getId().getPath().toLowerCase() + "_stone_bricks"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "chiseled_stone_bricks");
            RegistryUtils.register(new DimensionalBlock(), Utils.addPrefixAndSuffixToPath(dimensionDataId, "cracked_", "_chiseled_stone_bricks"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "crackedChiseledStoneBricks");
            RegistryUtils.register(new DimensionalBlock(), Utils.addPrefixAndSuffixToPath(dimensionDataId, "mossy_", "_chiseled_stone_bricks"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "mossyChiseledStoneBricks");
            Block polished = RegistryUtils.register(new DimensionalBlock(), new Identifier(RAADimensionAddon.MOD_ID,
                            "polished_" + dimensionData.getId().getPath().toLowerCase()),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "polished");
            RegistryUtils.register(new StairsBaseBlock(polished.getDefaultState()), Utils.addPrefixAndSuffixToPath(dimensionDataId, "polished_", "_stairs"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "polishedStairs");
            RegistryUtils.register(new SlabBaseBlock(Block.Settings.copy(Blocks.STONE_SLAB)), Utils.addPrefixAndSuffixToPath(dimensionDataId, "polished_", "_slab"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "polishedSlab");
            RegistryUtils.register(new WallBaseBlock(Block.Settings.copy(Blocks.COBBLESTONE_WALL)), Utils.addPrefixAndSuffixToPath(dimensionDataId, "polished_", "_wall"),
                    RAADimensionAddon.RAA_DIMENSION_BLOCKS, dimensionData.getName(), "polishedWall");
            Set<Biome> biomes = new LinkedHashSet<>();
            for (int i = 0; i < dimensionData.getBiomeData().size(); i++) {
                Biome.Builder biome = new Biome.Builder();
                SpawnSettings.Builder SPAWN_SETTINGS = new SpawnSettings.Builder();
                GenerationSettings.Builder GENERATION_SETTINGS = new GenerationSettings.Builder();
                biome.precipitation(Utils.checkBitFlag(dimensionData.getFlags(), Utils.FROZEN) ? Biome.Precipitation.SNOW : Rands.chance(10) ? Biome.Precipitation.RAIN : Biome.Precipitation.NONE)
                        .temperature(dimensionData.getBiomeData().get(i).getTemperature())
                        .temperatureModifier(Biome.TemperatureModifier.NONE)
                        .downfall(dimensionData.getBiomeData().get(i).getDownfall())
                        .category(Biome.Category.PLAINS)
                        .effects(new BiomeEffects.Builder()
                            .fogColor(dimensionData.getDimensionColorPalette().getFogColor())
                            .waterColor(dimensionData.getBiomeData().get(i).getWaterColor())
                            .waterFogColor(dimensionData.getBiomeData().get(i).getWaterColor())
                            .skyColor(dimensionData.getDimensionColorPalette().getSkyColor())
                            .grassColor(dimensionData.getDimensionColorPalette().getGrassColor())
                            .foliageColor(dimensionData.getDimensionColorPalette().getFoliageColor())
                            .build()
                        )
                        .scale(dimensionData.getBiomeData().get(i).getScale())
                        .depth(dimensionData.getBiomeData().get(i).getDepth());
                CustomDimensionalBiome.addFeatures(dimensionData, dimensionData.getBiomeData().get(i), GENERATION_SETTINGS, SPAWN_SETTINGS);
                biome.generationSettings(GENERATION_SETTINGS.build())
                        .spawnSettings(SPAWN_SETTINGS.build());
//                CustomDimensionalBiome biome = new CustomDimensionalBiome(dimensionData, dimensionData.getBiomeData().get(i));
                biomes.add(RegistryUtils.registerBiome(dimensionData.getBiomeData().get(i).getId(), biome.build()));
            }

            /*if (dimensionData.getDimensionChunkGenerator() == CAVES || dimensionData.getDimensionChunkGenerator() == FLAT_CAVES || dimensionData.getDimensionChunkGenerator() == HIGH_CAVES) {
                builder.defaultPlacer(PlayerPlacementHandlers.CAVE_WORLD.getEntityPlacer());
            } else if (dimensionData.getDimensionChunkGenerator() == FLOATING || dimensionData.getDimensionChunkGenerator() == LAYERED_FLOATING || dimensionData.getDimensionChunkGenerator() == PRE_CLASSIC_FLOATING) {
                builder.defaultPlacer(PlayerPlacementHandlers.FLOATING_WORLD.getEntityPlacer());
            } else {
                builder.defaultPlacer(PlayerPlacementHandlers.SURFACE_WORLD.getEntityPlacer());
            }*/

            /*DimensionType type = builder.buildAndRegister(dimensionData.getId());
            DimensionType dimensionType;
            if (Registry.DIMENSION_TYPE.get(dimensionData.getId()) == null) {
                dimensionType = Registry.register(Registry.DIMENSION_TYPE, dimensionData.getId(), type);
            }
            else {
                dimensionType = Registry.DIMENSION_TYPE.get(dimensionData.getId());
            }*/

//            RegistryUtils.registerBlockWithoutItem(new CustomPortalBlock(dimensionData, dimensionType), Utils.addSuffixToPath(dimensionData.getId(), "_custom_portal"));

//            RegistryUtils.registerItem(new DimensionalPortalKeyItem(dimensionData), Utils.addSuffixToPath(dimensionDataId, "_portal_key"));
            Block portalBlock = RegistryUtils.registerBlockWithoutItem(new PortalBlock(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimensionDataId), dimensionData),
                    new Identifier(RAADimensionAddon.MOD_ID, dimensionData.getId().getPath().toLowerCase() + "_portal"));
            RegistryUtils.registerItem(new RAABlockItemAlt(dimensionData.getName(), "portal", portalBlock, new Item.Settings().group(ItemGroup.TRANSPORTATION)),
                    new Identifier(RAADimensionAddon.MOD_ID, dimensionData.getId().getPath().toLowerCase() + "_portal"));
        });
    }

    public static Pair<Integer, HashMap<String, int[]>> generateDimensionMobs(int flags, int difficulty) {
        HashMap<String, int[]> list = new HashMap<>();
        if (Utils.checkBitFlag(flags, Utils.LUSH)) {
            String[] names = new String[]{"cow", "pig", "chicken", "horse", "donkey", "sheep", "llama"};
            for (String name : names) {
                int spawnSize = Rands.randIntRange(4, 16);
                list.put(name, new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 8)});
            }
        } else {
            if (!Utils.checkBitFlag(flags, Utils.DEAD)) {
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 12);
                    list.put("cow", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                } else {
                    difficulty++;
                }
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 12);
                    list.put("pig", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                } else {
                    difficulty++;
                }
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 12);
                    list.put("chicken", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                } else {
                    difficulty++;
                }
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 8);
                    list.put("horse", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                }
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 8);
                    list.put("donkey", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                }
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 12);
                    list.put("sheep", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                } else {
                    difficulty++;
                }
                if (Rands.chance(2)) {
                    int spawnSize = Rands.randIntRange(2, 8);
                    list.put("llama", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
                }
            } else {
                difficulty += 4;
            }
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 12);
            list.put("bat", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 8);
            list.put("spider", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
        } else {
            difficulty--;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 12);
            list.put("zombie", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
        } else {
            difficulty--;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 4);
            list.put("zombie_villager", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + 1});
        } else {
            --difficulty;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 12);
            list.put("skeleton", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
        } else {
            difficulty--;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 8);
            list.put("creeper", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
        } else {
            difficulty -= 2;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 4);
            list.put("slime", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize + Rands.randIntRange(2, 4)});
        } else {
            difficulty--;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 4);
            list.put("enderman", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize});
        } else {
            difficulty -= 2;
        }
        if (Rands.chance(2)) {
            int spawnSize = Rands.randIntRange(2, 3);
            list.put("witch", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize});
        } else {
            difficulty -= 2;
        }

        if (Rands.chance(10)) {
            int spawnSize = Rands.randIntRange(2, 3);
            list.put("blaze", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize});
        } else {
            difficulty -= 2;
        }
        if (Rands.chance(10)) {
            int spawnSize = Rands.randIntRange(2, 3);
            list.put("piglin", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize});
        } else {
            difficulty -= 2;
        }
        if (Rands.chance(10)) {
            int spawnSize = Rands.randIntRange(2, 3);
            list.put("zombified_piglin", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize});
        } else {
            difficulty -= 2;
        }
        if (Rands.chance(10)) {
            int spawnSize = Rands.randIntRange(2, 3);
            list.put("ghast", new int[]{Rands.randIntRange(1, 300), spawnSize, spawnSize});
        } else {
            difficulty -= 2;
        }
        return new Pair<>(difficulty, list);
    }

    //generate the flags for a dimension, ensures that dimensions can't have conflicting flags
    public static int generateDimensionFlags() {
        int flags = 0;

        //post apocalyptic dimensions are a rare combination of the worst flags
        if (Rands.chance(35)) {
            flags = Utils.POST_APOCALYPTIC;
            return flags;
        }

        //any dimension can be a lucid dimension
        //TODO: fix skybox
//        if (Rands.chance(65)) {
//            flags |= Utils.LUCID;
//        }

        //any dimension can be a tectonic dimension
        if (Rands.chance(10)) {
            flags |= Utils.TECTONIC;
        }

        //corrupted dimensions
        if (Rands.chance(20)) {
            flags |= Utils.CORRUPTED;
            if (Rands.chance(8)) {
                flags |= Utils.DEAD;
            }
            if (Rands.chance(3)) {
                flags |= Utils.MOLTEN;
            }
            if (Rands.chance(4)) {
                flags |= Utils.DRY;
            }
        } else {
            //dead dimensions
            if (Rands.chance(18)) {
                flags |= Utils.DEAD;
                if (Rands.chance(6)) {
                    flags |= Utils.MOLTEN;
                }
                if (Rands.chance(5)) {
                    flags |= Utils.DRY;
                }
            } else { // lush dimensions
                if (Rands.chance(4)) {
                    flags |= Utils.LUSH;
                }

                if (Rands.chance(10)) {
                    flags |= Utils.FROZEN;
                }
            }
        }

        return flags;
    }
}