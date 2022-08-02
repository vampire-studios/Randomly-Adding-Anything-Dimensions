package io.github.vampirestudios.raa_dimension.blocks;

import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.CustomTeleporter;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PortalBlock extends Block {

    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    private final DimensionData dimensionData;

    public PortalBlock(DimensionData dimensionData) {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(8.0f, 80.f).noOcclusion());
        this.dimensionData = dimensionData;
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVATED, false));
    }

    @Override
    public InteractionResult use(BlockState blockState_1, Level world_1, BlockPos pos, Player playerEntity_1, InteractionHand hand_1, BlockHitResult blockHitResult_1) {
        if (!Objects.requireNonNull(world_1).isClientSide) {
            if (blockState_1.getValue(ACTIVATED)) {
                BlockPos playerPos = playerEntity_1.blockPosition();
                if (playerPos.getX() == pos.getX() && playerPos.getZ() == pos.getZ() && playerPos.getY() == pos.getY() + 1) {
                    if (world_1.dimension().location().equals(dimensionData.getId())) {
                        CustomTeleporter.TPToDim(BuiltinDimensionTypes.OVERWORLD.location(), dimensionData.getId(), world_1, playerEntity_1);
                    } else {
                        CustomTeleporter.TPToDim(dimensionData.getId(), BuiltinDimensionTypes.OVERWORLD.location(), world_1, playerEntity_1);
                    }
                }
            }

            if (!blockState_1.getValue(ACTIVATED) && playerEntity_1.getMainHandItem().getItem() == Registry.ITEM.get(Utils.addSuffixToPath(dimensionData.getId(), "_portal_key"))) {
                world_1.setBlockAndUpdate(pos, blockState_1.setValue(ACTIVATED, true));
            }
        }
        return super.use(blockState_1, world_1, pos, playerEntity_1, hand_1, blockHitResult_1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(state.getBlock().asItem()));
        return list;
    }

}