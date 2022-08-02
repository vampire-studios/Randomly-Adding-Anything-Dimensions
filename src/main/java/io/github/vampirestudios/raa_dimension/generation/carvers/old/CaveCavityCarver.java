package io.github.vampirestudios.raa_dimension.generation.carvers.old;

import com.google.common.collect.ImmutableSet;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.OpenSimplexNoise;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

// Thanks to TelepathicGrunt and the UltraAmplified mod for this class
public class CaveCavityCarver extends WorldCarver<ProbabilityFeatureConfiguration> {

	private final float[] ledgeWidthArrayYIndex = new float[1024];
	protected static long noiseSeed;
	protected static OpenSimplexNoise noiseGen;
	protected static final BlockState STONE = Blocks.STONE.defaultBlockState();
	protected static final BlockState LAVA = Blocks.LAVA.defaultBlockState();
	protected static final BlockState WATER = Blocks.WATER.defaultBlockState();
	protected static final BlockState MAGMA = Blocks.MAGMA_BLOCK.defaultBlockState();
	protected static final BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();

	// Blocks that we can carve out.
	/*private static final Map<BlockState, BlockState> canReplaceMap;
	static {
		Map<BlockState, BlockState> result = new HashMap<BlockState, BlockState>();

		result.put(Blocks.NETHERRACK.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
		result.put(Blocks.ICE.getDefaultState(), Blocks.ICE.getDefaultState());
		result.put(Blocks.SNOW_BLOCK.getDefaultState(), Blocks.ICE.getDefaultState());
		result.put(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
		result.put(Blocks.LAVA.getDefaultState(), Blocks.LAVA.getDefaultState());

		canReplaceMap = result;
	}*/

	// Used to keep track of what block to use to fill in certain air/liquids
	protected BlockState replacementBlock = Blocks.STONE.defaultBlockState();
	private final DimensionData dimensionData;

	/**
	 * Sets the internal seed for this carver after we get the world seed. (Based on Nether's surface builder code)
	 */
	public static void setSeed(long seed) {
		if (noiseSeed != seed || noiseGen == null) {
			noiseGen = new OpenSimplexNoise(seed);
			noiseSeed = seed;
		}
	}


	public CaveCavityCarver(DimensionData dimensionData) {
		super(ProbabilityFeatureConfiguration.CODEC);
//		this.alwaysCarvableBlocks = ImmutableSet.of(Registry.BLOCK.get(new ResourceLocation(RAADimensionAddon.MOD_ID, dimensionData.getName().toLowerCase() + "_stone")),
//				Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL,
//				Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA,
//				Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA,
//				Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
//				Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM,
//				Blocks.SNOW, Blocks.PACKED_ICE);
		this.dimensionData = dimensionData;
	}

	/**
	 * Checks whether the entire cave can spawn or not. (Not the individual parts)
	 */
	@Override
	public boolean isStartChunk(ProbabilityFeatureConfiguration config, RandomSource randomSource) {
		return randomSource.nextFloat() <= 5 / 1000f;
	}

	@Override
	public boolean carve(CarvingContext region, ProbabilityFeatureConfiguration config, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeBlockPos, RandomSource random, Aquifer aquifer, ChunkPos pos, CarvingMask mask)
	{

		int i = (this.getBranchFactor() * 2 - 1) * 16;
		double xpos = pos.x * 16 + random.nextInt(16);
		double height = random.nextInt(random.nextInt(2) + 1) + 34;
		double zpos = pos.z * 16 + random.nextInt(16);
		float xzNoise2 = random.nextFloat() * ((float) Math.PI * 1F);
		float xzCosNoise = (random.nextFloat() - 0.5F) / 16.0F;
		float widthHeightBase = (random.nextFloat() + random.nextFloat()) / 16; // width And Height Modifier
		this.carveCavity(chunk, biomeBlockPos, random, pos.x, pos.z, xpos, height, zpos, widthHeightBase, xzNoise2, xzCosNoise, 0, i, random.nextDouble() + 20D, mask);
		return true;
	}


	private void carveCavity(ChunkAccess world, Function<BlockPos, Holder<Biome>> biomeBlockPos, RandomSource random, int mainChunkX, int mainChunkZ, double randomBlockX, double randomBlockY, double randomBlockZ, float widthHeightBase, float xzNoise2, float xzCosNoise, int startIteration, int maxIteration, double heightMultiplier, CarvingMask mask) {
		float ledgeWidth = 1.0F;

		// CONTROLS THE LEDGES' WIDTH! FINALLY FOUND WHAT THIS JUNK DOES
		for (int currentHeight = 0; currentHeight <= 70; ++currentHeight)
		{

			//attempt at creating dome ceilings
			if (currentHeight > 44 && currentHeight < 60)
			{
				ledgeWidth = 1.0F + random.nextFloat() * 0.3f;
				ledgeWidth = (float) (ledgeWidth + Math.max(0, Math.pow((currentHeight - 44) * 0.15F, 2)));
			}

			//normal ledges on walls
			else
			{
				if (currentHeight == 0 || random.nextInt(3) == 0)
				{
					ledgeWidth = 1.0F + random.nextFloat() * 0.5F;
				}
			}

			this.ledgeWidthArrayYIndex[currentHeight] = ledgeWidth;
		}

		float f4 = 0.0F;
		float f1 = 0.0F;

		double placementXZBound = 2D + Mth.sin(1 * (float) Math.PI / maxIteration) * widthHeightBase;
		double placementYBound = placementXZBound * heightMultiplier;
		placementXZBound = placementXZBound * 32D; // thickness of the "room" itself
		placementYBound = placementYBound * 2.2D;
		xzCosNoise = xzCosNoise * 0.5F + f1 * 0.04F;
		xzNoise2 += f4 * 0.05F;
		f1 = f1 * 0.8F;
		f4 = f4 * 0.5F;
		f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.5F;
		f4 = f4 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

		this.carveAtTarget(world, biomeBlockPos, random, mainChunkX, mainChunkZ, randomBlockX, randomBlockY, randomBlockZ, placementXZBound, placementYBound, mask);

	}


	protected boolean carveAtTarget(ChunkAccess world, Function<BlockPos, Holder<Biome>> biomeBlockPos, RandomSource random, int mainChunkX, int mainChunkZ, double xRange, double yRange, double zRange, double placementXZBound, double placementYBound, CarvingMask mask)
	{

		double xPos = mainChunkX * 16 + 8;
		double zPos = mainChunkZ * 16 + 8;
		double multipliedXZBound = placementXZBound * 2.0D;

		if (!(xRange < xPos - 16.0D - multipliedXZBound) && !(zRange < zPos - 16.0D - multipliedXZBound) && !(xRange > xPos + 16.0D + multipliedXZBound) && !(zRange > zPos + 16.0D + multipliedXZBound))
		{

			int xMin = Math.max(Mth.floor(xRange - placementXZBound) - mainChunkX * 16 - 1, 0);
			int xMax = Math.min(Mth.floor(xRange + placementXZBound) - mainChunkX * 16 + 1, 16);
			int yMin = Math.max(Mth.floor(yRange - placementYBound) - 1, 5);
			int yMax = Math.min(Mth.floor(yRange + placementYBound) + 1, world.getHeight());
			int zMin = Math.max(Mth.floor(zRange - placementXZBound) - mainChunkZ * 16 - 1, 0);
			int zMax = Math.min(Mth.floor(zRange + placementXZBound) - mainChunkZ * 16 + 1, 16);
			if (xMin <= xMax && yMin <= yMax && zMin <= zMax)
			{
				boolean flag = false;
				BlockState secondaryFloorBlockstate;
				BlockState currentBlockstate;
				BlockState aboveBlockstate;
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				BlockPos.MutableBlockPos mutableUp = new BlockPos.MutableBlockPos();
				BlockPos.MutableBlockPos mutableDown = new BlockPos.MutableBlockPos();
				Biome biome;
				double stalagmiteDouble = 0;

				for (int smallX = xMin; smallX < xMax; ++smallX)
				{
					int x = smallX + mainChunkX * 16;
					double xSquaringModified = (x + 0.5D - xRange) / placementXZBound;

					for (int smallZ = zMin; smallZ < zMax; ++smallZ)
					{
						int z = smallZ + mainChunkZ * 16;
						double zSquaringModified = (z + 0.5D - zRange) / placementXZBound;
						double xzSquaredModified = xSquaringModified * xSquaringModified + zSquaringModified * zSquaringModified;

						if (xzSquaredModified < 1.0D) {
							if (yMax < yMin) {
								continue;
							}

							mutable.set(x, 60, z);
							biome = biomeBlockPos.apply(mutable).value();
							/*replacementBlock = fillerBiomeMap.get(biome);
							if (replacementBlock == null) {
								replacementBlock = STONE;
							}*/
							secondaryFloorBlockstate = Blocks.NETHERRACK.defaultBlockState();

							for (int y = yMax; y > yMin; y--)
							{

								// sets a trial and error value that widens base of pillar and makes paths
								// through lava that look good
								double yPillarModifier = y;

								
								if(y > 30)
								{
									//increase multiplier on end to widen top of pillar
									yPillarModifier = (Math.pow((yPillarModifier - 30D) * 0.033333D, 2) * 30D - (y * 0.016666D)) * 18D;
								}
								else 
								{
									//increase multiplier on end to widen bottom of pillar
									yPillarModifier = Math.pow((Math.pow(yPillarModifier - 30D, 2) * 0.033333D), 2) * 2.8D;
								}
								
								if (yPillarModifier <= 0)
								{
									// prevents divide by 0 or by negative numbers (decreasing negative would make
									// more terrain be carved instead of not carve)
									yPillarModifier = 0.00001D;
								}
								else if (y < 10)
								{
									// creates a deep lava pool that starts 2 blocks deep automatically at edges.
									yPillarModifier -= 50D;
								}

								
								//limits calling pillar and stalagmite perlin generators to reduce gen time
								if (y < 60)
								{
									// Creates pillars that are widen at bottom.
									//
									// simplex field creates the main body for pillar by stepping slowly through x
									// and z and extremely slowly through y.
									// Then subtracted modified target height to flatten bottom of pillar to
									// make a path through lava.
									// Next, adds a random value to add some noise to the pillar.
									// And lastly, sets the greater than value to be very low so most of the cave gets carved
									// out.
									//
									//Increase step in X and Z to make pillars less frequent but thicker
									boolean flagPillars = noiseGen.eval(
																x * 0.045D + (x % 16) * 0.002D, 
																z * 0.045D + (z % 16) * 0.002D, 
																y * 0.015D) - (yPillarModifier * 0.001D) + 
														 (random.nextDouble() * 0.01D) 
														 > -0.32D;

									if (!flagPillars)
									{
										//skip position if we are in pillar space
										continue;
									}

									//limits calling stalagmite perlin generators to reduce gen time
									if (y > 32)
									{
										// Creates large stalagmites that cannot reach floor of cavern.
										//
										// simplex field creates the main stalagmite shape and placement by stepping
										// though x and z pretty fast and through y very slowly.
										// Then adds 400/y so that as the y value gets lower, the more area gets carved
										// which sets the limit on how far down the stalagmites can go.
										// Next, add a random value to add some noise to the pillar.
										// And lastly, sets the greater than value to be high so more stalagmites can be made
										// while the 400/y has already carved out the rest of the cave.
										//
										//Increase step in X and Z to decrease number of stalagmites and make them slightly thicker
										stalagmiteDouble = noiseGen.eval(x * 0.25D, z * 0.25D, y * 0.005D) * 15.0D + (480D / (y));

										//adds more tiny stalagmites to ceiling
										if (y > 48)
										{
											stalagmiteDouble -= (y - 53D) / 3D;
										}

										//decrease constant to make stalagmites smaller and thinner
										boolean flagStalagmites = stalagmiteDouble > 4.9D;

										if (!flagStalagmites)
										{
											//skip position if we are in stalagmite space
											continue;
										}
									}
								}

								double ySquaringModified = (y - 1 + 0.5D - yRange) / placementYBound;

								// Where the pillar flag and stalagmite flag both flagged this block to be
								// carved, begin carving.
								// Thus the pillar and stalagmite is what is left after carving.
								if (xzSquaredModified * this.ledgeWidthArrayYIndex[y - 1] + ySquaringModified * ySquaringModified / 6.0D + random.nextFloat() * 0.015f < 1.0D)
								{

									mutable.set(x, y, z);
									currentBlockstate = world.getBlockState(mutable);
									mutableUp.set(mutable).move(Direction.UP);
									mutableDown.set(mutable).move(Direction.DOWN);
									aboveBlockstate = world.getBlockState(mutableUp);

									if (y >= 60)
									{
										//Creates the messy but cool plateau of stone on the ocean floor 
										//above this cave to help players locate caves when exploring
										//ocean biomes. Also helps to break up the blandness of ocean
										//floors.

										/*if (!currentBlockstate.getFluidState().isEmpty())
										{
											world.setBlockState(mutable, replacementBlock, false);
										}
										else if (!aboveBlockstate.getFluidState().isEmpty())
										{
											world.setBlockState(mutable, replacementBlock, false);
											world.setBlockState(mutableUp, replacementBlock, false);
											world.setBlockState(mutableDown, replacementBlock, false);
											flag = true;
										}*/
										if (!currentBlockstate.getFluidState().isEmpty())
										{
											world.setBlockState(mutable, replacementBlock, false);
										}
										else if (!aboveBlockstate.getFluidState().isEmpty())
										{
											world.setBlockState(mutable, replacementBlock, false);
											world.setBlockState(mutableUp, replacementBlock, false);
											world.setBlockState(mutableDown, replacementBlock, false);
											flag = true;
										}
									}
									/*else if (this.canCarveBlock(currentBlockstate, aboveBlockstate)*//* || canReplaceMap.containsKey(currentBlockstate)*//*)
									{

										if (y < 11)
										{
											currentBlockstate = LAVA;
											if (secondaryFloorBlockstate != null)
											{
												if (secondaryFloorBlockstate == OBSIDIAN)
												{
													currentBlockstate = MAGMA;
												}

												if (stalagmiteDouble > 13.5D)
												{
													if (y == 10)
													{
														currentBlockstate = secondaryFloorBlockstate;
													}
													else if (y == 9 && random.nextBoolean())
													{
														currentBlockstate = secondaryFloorBlockstate;
													}
												}
											}
											world.setBlockState(mutable, currentBlockstate, false);
										}
										else
										{
											//carves the cave
											world.setBlockState(mutable, CAVE_AIR, false);
										}

										flag = true;
									}*/

								}
							}
						}
					}
				}

				return flag;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

}