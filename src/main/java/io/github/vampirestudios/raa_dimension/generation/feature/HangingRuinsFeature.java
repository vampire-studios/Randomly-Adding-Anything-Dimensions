package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

// Thanks to TelepathicGrunt and the UltraAmplified mod for this class
public class HangingRuinsFeature extends Feature<DefaultFeatureConfig> {

	public HangingRuinsFeature(Codec<DefaultFeatureConfig> configDeserializer) {
		super(configDeserializer);
	}

	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
		BlockPos position = context.getOrigin();
		StructureWorldAccess world = context.getWorld();
		Random rand = context.getRandom();
		DefaultFeatureConfig config = context.getConfig();
		//makes sure this ruins does not spawn too close to world height border.
		if (position.getY() < world.getSeaLevel() + 5) {
			return false;
		}

		BlockRotation rot = BlockRotation.values()[rand.nextInt(BlockRotation.values().length)];
		BlockPos.Mutable mutable = new BlockPos.Mutable(position.getX(), position.getY(), position.getZ());
		BlockPos.Mutable offset = new BlockPos.Mutable();

		//makes sure there is enough solid blocks on ledge to hold this feature.
		for (int x = -5; x <= 5; x++)
		{
			for (int z = -5; z <= 5; z++)
			{
				if (Math.abs(x * z) > 9 && Math.abs(x * z) < 20)
				{
					//match rotation of structure as it rotates around 0, 0 I think.
					//The -4 is to make the check rotate the same way as structure and 
					//then we do +4 to get the actual position again.
					offset.set(x - 4, 0, z - 4).set(offset.rotate(rot));
					if (!world.getBlockState(mutable.add(-offset.getX() + 4, 1, -offset.getZ() + 4)).isAir()) {
						return false;
					}
				}
			}
		}

		//makes sure top won't be exposed to air
		if (shouldMoveDownOne(world, mutable, offset, rot))
		{
			mutable.move(Direction.DOWN);
		}

		//UltraAmplified.LOGGER.debug("Hanging Ruins | " + position.getX() + " " + position.getY() + " "+position.getZ());
		StructureManager templatemanager = world.getServer().getStructureManager();
		Structure template = templatemanager.getStructure(new Identifier(RAADimensionAddon.MOD_ID + ":hanging_ruins"));

		if (template == null)
		{
			RAACore.LOGGER.warn("hanging ruins NTB does not exist!");
			return false;
		}

		StructurePlacementData placementsettings = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(rot)
				.setIgnoreEntities(false).setPosition(null);

		BlockPos pos = mutable.move(4, -8, 4).toImmutable();
		template.place(world, pos, pos, placementsettings, rand, 2);

		return true;

	}


	private boolean shouldMoveDownOne(ServerWorldAccess world, BlockPos.Mutable blockpos$Mutable, BlockPos.Mutable offset, BlockRotation rot) {

		//if we are on a 1 block thick ledge at any point, move down one so ruins ceiling isn't exposed 
		for (int x = -5; x <= 5; x++)
		{
			for (int z = -5; z <= 5; z++)
			{
				offset.set(x - 4, 0, z - 4).set(offset.rotate(rot));
				if (Math.abs(x * z) < 20 && !world.getBlockState(blockpos$Mutable.add(-offset.getX() + 4, 2, -offset.getZ() + 4)).isOpaque())
				{
					//world.setBlockState(blockpos$Mutable.add(-offset.getX() + 4, 2, -offset.getZ() + 4), Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
					return true;
				}
			}
		}
		return false;
	}
}