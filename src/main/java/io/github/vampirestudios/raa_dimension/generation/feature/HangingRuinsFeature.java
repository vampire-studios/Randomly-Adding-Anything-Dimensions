package io.github.vampirestudios.raa_dimension.generation.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

// Thanks to TelepathicGrunt and the UltraAmplified mod for this class
public class HangingRuinsFeature extends Feature<NoneFeatureConfiguration> {

	public HangingRuinsFeature(Codec<NoneFeatureConfiguration> configDeserializer) {
		super(configDeserializer);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		BlockPos position = context.origin();
		WorldGenLevel world = context.level();
		RandomSource rand = context.random();
		NoneFeatureConfiguration config = context.config();
		//makes sure this ruins does not spawn too close to world height border.
		if (position.getY() < world.getSeaLevel() + 5) {
			return false;
		}

		Rotation rot = Rotation.values()[rand.nextInt(Rotation.values().length)];
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
		BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();

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
					if (!world.getBlockState(mutable.offset(-offset.getX() + 4, 1, -offset.getZ() + 4)).isAir()) {
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
		/*StructureManager templatemanager = world.getServer().getStructureManager();
		Structure template = templatemanager.getStructure(new ResourceLocation(RAADimensionAddon.MOD_ID + ":hanging_ruins"));

		if (template == null)
		{
			RAACore.LOGGER.warn("hanging ruins NTB does not exist!");
			return false;
		}*/

		StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(rot)
				.setIgnoreEntities(false).setRotationPivot(null);

		BlockPos pos = mutable.move(4, -8, 4).immutable();
//		template.place(world, pos, pos, placementsettings, rand, 2);

		return true;

	}


	private boolean shouldMoveDownOne(ServerLevelAccessor world, BlockPos.MutableBlockPos blockpos$Mutable, BlockPos.MutableBlockPos offset, Rotation rot) {

		//if we are on a 1 block thick ledge at any point, move down one so ruins ceiling isn't exposed 
		for (int x = -5; x <= 5; x++)
		{
			for (int z = -5; z <= 5; z++)
			{
				offset.set(x - 4, 0, z - 4).set(offset.rotate(rot));
				if (Math.abs(x * z) < 20 && !world.getBlockState(blockpos$Mutable.offset(-offset.getX() + 4, 2, -offset.getZ() + 4)).canOcclude())
				{
					//world.setBlockState(blockpos$Mutable.add(-offset.getX() + 4, 2, -offset.getZ() + 4), Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
					return true;
				}
			}
		}
		return false;
	}
}