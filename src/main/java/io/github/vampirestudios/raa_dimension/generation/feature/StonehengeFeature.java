package io.github.vampirestudios.raa_dimension.generation.feature;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Set;

// Thanks to TelepathicGrunt and the UltraAmplified mod for this class
public class StonehengeFeature extends Feature<NoneFeatureConfiguration> {

	public StonehengeFeature(Codec<NoneFeatureConfiguration> function) {
		super(function);
	}

	private static int perfectStoneCount = 0;
	private static boolean markedForPerfection = false;

	protected static final Set<BlockState> unAcceptableBlocks = ImmutableSet.of(Blocks.AIR.defaultBlockState(), Blocks.WATER.defaultBlockState(), Blocks.LAVA.defaultBlockState(), Blocks.SLIME_BLOCK.defaultBlockState(), Blocks.CAVE_AIR.defaultBlockState());

	private enum StoneHengeType {
		SIDE, CORNER
	}

	//this structure is a bit more complicated than usual.
	//The goal is to have 8 stone blocks in a circle with a high chance at being unbroken. 
	//If all are unbroken, generates enchanting table in center


	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		BlockPos position = context.origin();
		WorldGenLevel world = context.level();
		RandomSource rand = context.random();
		NoneFeatureConfiguration config = context.config();
		//makes sure this stonehenge does not spawn too close to world height border or it will get cut off.
		if (position.getY() > 248) {
			return false;
		}

		BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
		if (!validatePosition(world, blockpos$Mutable))
		{
			//height is inside a non-air block, move down until we reached an air block
			while (blockpos$Mutable.getY() > world.getSeaLevel())
			{
				blockpos$Mutable.move(Direction.DOWN);
				if (world.isEmptyBlock(blockpos$Mutable))
				{
					break;
				}
			}

			//height is an air block, move down until we reached a solid block. We are now on the surface of a piece of land
			while (blockpos$Mutable.getY() > world.getSeaLevel()) {
				blockpos$Mutable.move(Direction.DOWN);
				if (world.getBlockState(blockpos$Mutable).canOcclude()) {
					break;
				}
			}

			while (blockpos$Mutable.getY() <= world.getSeaLevel()) {
				return false; // Too low to generate. 
			}

			//try seeing if we can spawn here at below layer
			if (!validatePosition(world, blockpos$Mutable.move(Direction.UP))) {
				return false;
			}
		}

		//UltraAmplified.LOGGER.debug("Stonehenge | " + position.getX() + " "+position.getZ());

		//field
		//6.7% of being a perfect stonehenge right off the bat
		markedForPerfection = rand.nextInt(15) == 0;
		perfectStoneCount = 0;
//		StructureManager templatemanager = world.getServer().getStructureManager();
//		Structure template;
		BlockState iblockstate = world.getBlockState(blockpos$Mutable);

		world.setBlock(blockpos$Mutable, iblockstate, 3);

		//SIDE STONES

		/*//north stone
		template = pickStonehengeStyle(StoneHengeType.SIDE, rand, templatemanager);
		if (template == null) {
			RAACore.LOGGER.warn("a side stonehenge NTB does not exist!");
			return false;
		}

		StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, position, blockpos$Mutable.below(2).north(11).west(2), placementsettings, rand, 2);

		//East stone - rotated 90 degrees
		template = pickStonehengeStyle(StoneHengeType.SIDE, rand, templatemanager);
		if (template == null) {
			RAACore.LOGGER.warn("a side stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_90)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).north(2).east(11), placementsettings, rand, 2);

		//south stone - rotated 180 degrees
		template = pickStonehengeStyle(StoneHengeType.SIDE, rand, templatemanager);
		if (template == null) {
			RAACore.LOGGER.warn("a side stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_180)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).south(11).east(2), placementsettings, rand, 2);

		//West stone - rotated 270 degrees
		template = pickStonehengeStyle(StoneHengeType.SIDE, rand, templatemanager);
		if (template == null)
		{
			RAACore.LOGGER.warn("a side stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.COUNTERCLOCKWISE_90)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).south(2).west(11), placementsettings, rand, 2);

		//CORNER STONE

		//north west stone
		template = pickStonehengeStyle(StoneHengeType.CORNER, rand, templatemanager);
		if (template == null)
		{
			RAACore.LOGGER.warn("a corner stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).north(9).west(9), placementsettings, rand, 2);

		//north east stone
		template = pickStonehengeStyle(StoneHengeType.CORNER, rand, templatemanager);
		if (template == null)
		{
			RAACore.LOGGER.warn("a corner stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_90)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).north(9).east(9), placementsettings, rand, 2);

		//south east stone
		template = pickStonehengeStyle(StoneHengeType.CORNER, rand, templatemanager);
		if (template == null)
		{
			RAACore.LOGGER.warn("a corner stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_180)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).south(9).east(9), placementsettings, rand, 2);

		//south west stone
		template = pickStonehengeStyle(StoneHengeType.CORNER, rand, templatemanager);
		if (template == null)
		{
			RAACore.LOGGER.warn("a corner stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.COUNTERCLOCKWISE_90)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below(2).south(9).west(9), placementsettings, rand, 2);

		//center of stonehenge.
		//If all stones are perfect, generates crafting table, otherwise, place a small patch of stones
		if (perfectStoneCount == 8)
		{
			template = templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehengeperfectcenter"));
		}
		else
		{
			template = templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehengecenter"));
		}

		if (template == null)
		{
			RAACore.LOGGER.warn("a center stonehenge NTB does not exist!");
			return false;
		}

		placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE)
				.setIgnoreEntities(false).setRotationPivot(null);

		template.place(world, blockpos$Mutable, blockpos$Mutable.below().north(2).west(2), placementsettings, rand, 2);*/

		return true;
	}


	private boolean validatePosition(ServerLevelAccessor world, BlockPos position) {
		BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
		//makes sure it generates with land around it instead of cutting into cliffs or hanging over an edge by checking if block at north, east, west, and south are acceptable terrain blocks that appear only at top of land.
		for (int x = -10; x <= 10; x = x + 5) {
			for (int z = -10; z <= 10; z = z + 5) {
				blockpos$Mutable.set(position).move(x, 0, z);
				if (Math.abs(x * z) != 100 && (unAcceptableBlocks.contains(world.getBlockState(blockpos$Mutable.below(2))) && unAcceptableBlocks.contains(world.getBlockState(blockpos$Mutable.below(1))) && unAcceptableBlocks.contains(world.getBlockState(blockpos$Mutable)))) {
					return false;
				}
			}
		}
		return true;
	}


	//picks one out of four templates for the stone henge
	/*private Structure pickStonehengeStyle(StoneHengeType type, Random rand, StructureManager templatemanager)
	{
		int hengeType;

		//Generates only perfect stones if markedForPerfection is true.
		//otherwise, chance of being a perfect stone increases as more perfect stones are chosen and picks randomly from the 3 broken stones types otherwise
		if (markedForPerfection || rand.nextInt(8 - (perfectStoneCount / 2 + 3)) == 0)
		{
			perfectStoneCount++;
			hengeType = 0;
		}
		else
		{
			hengeType = rand.nextInt(3) + 1;
		}

		//chooses the correct template for if the stone is on side or corner
		//if the perfect stone is chosen, increased the count for perfect stones
		if (type == StoneHengeType.SIDE)
		{
			if (hengeType == 0)
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge1"));
			}
			else if (hengeType == 1)
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge2"));
			}
			else if (hengeType == 2)
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge3"));
			}
			else
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge4"));
			}
		}
		else
		{
			if (hengeType == 0)
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge5"));
			}
			else if (hengeType == 1)
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge6"));
			}
			else if (hengeType == 2)
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge7"));
			}
			else
			{
				return templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":stonehenge8"));
			}
		}
	}*/

}