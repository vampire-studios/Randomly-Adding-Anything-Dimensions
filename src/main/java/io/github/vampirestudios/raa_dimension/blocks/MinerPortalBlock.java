package io.github.vampirestudios.raa_dimension.blocks;

import com.google.common.cache.LoadingCache;
import io.github.vampirestudios.raa_dimension.generation.dimensions.data.DimensionData;
import io.github.vampirestudios.raa_dimension.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;
import java.util.Random;

public class MinerPortalBlock extends Block {

    private final DimensionData dimensionData;
    private final DimensionType dimensionType;
    public static final EnumProperty<Direction.Axis> AXIS;
    protected static final VoxelShape X_SHAPE;
    protected static final VoxelShape Z_SHAPE;

    public MinerPortalBlock(DimensionData dimensionData, DimensionType dimensionType) {
        super(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL));
        this.dimensionData = dimensionData;
        this.dimensionType = dimensionType;
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (Objects.requireNonNull(state).getValue(AXIS)) {
            case Z -> Z_SHAPE;
            case X, default -> X_SHAPE;
        };
    }

    public static boolean createPortalAt(DimensionData dimensionData, Level iWorld, BlockPos blockPos) {
        MinerPortalBlock.AreaHelper areaHelper = createAreaHelper(dimensionData, iWorld, blockPos);
        if (areaHelper != null) {
            areaHelper.createPortal();
            return true;
        } else {
            return false;
        }
    }

    public static MinerPortalBlock.AreaHelper createAreaHelper(DimensionData dimensionData, Level iWorld, BlockPos blockPos) {
        MinerPortalBlock.AreaHelper areaHelper = new MinerPortalBlock.AreaHelper(dimensionData, iWorld, blockPos, Direction.Axis.X);
        if (areaHelper.isValid() && areaHelper.foundPortalBlocks == 0) {
            return areaHelper;
        } else {
            MinerPortalBlock.AreaHelper areaHelper2 = new MinerPortalBlock.AreaHelper(dimensionData, iWorld, blockPos, Direction.Axis.Z);
            return areaHelper2.isValid() && areaHelper2.foundPortalBlocks == 0 ? areaHelper2 : null;
        }
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis axis2 = state.getValue(AXIS);
        boolean bl = axis2 != axis && axis.isHorizontal();
        return !bl && newState.getBlock() != this && !(new AreaHelper(dimensionData, world, pos, axis2)).wasAlreadyValid() ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, newState, world, pos, posFrom);
    }

    /*public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals()) {
            if(entity.getEntityWorld().getRegistryKey().getValue().equals(dimensionData.getId())) entity.move(DimensionType.OVERWORLD.);
            else entity.changeDimension(Registry.DIMENSION_TYPE.get(dimensionData.getId()));
        }
    }*/

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double d = (double)pos.getX() + (double)random.nextFloat();
            double e = (double)pos.getY() + (double)random.nextFloat();
            double f = (double)pos.getZ() + (double)random.nextFloat();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;
            if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
                d = (double)pos.getX() + 0.5D + 0.25D * (double)k;
                g = random.nextFloat() * 2.0F * (float)k;
            } else {
                f = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
                j = random.nextFloat() * 2.0F * (float)k;
            }

            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }

    }

    @Environment(EnvType.CLIENT)
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch(state.getValue(AXIS)) {
                    case Z:
                        return state.setValue(AXIS, Direction.Axis.X);
                    case X:
                        return state.setValue(AXIS, Direction.Axis.Z);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    public static BlockPattern.BlockPatternMatch findPortal(DimensionData dimensionData, LevelAccessor iWorld, BlockPos world) {
        Direction.Axis axis = Direction.Axis.Z;
        MinerPortalBlock.AreaHelper areaHelper = new MinerPortalBlock.AreaHelper(dimensionData, iWorld, world, Direction.Axis.X);
        LoadingCache<BlockPos, BlockInWorld> loadingCache = BlockPattern.createLevelCache(iWorld, true);
        if (!areaHelper.isValid()) {
            axis = Direction.Axis.X;
            areaHelper = new MinerPortalBlock.AreaHelper(dimensionData, iWorld, world, Direction.Axis.Z);
        }

        if (!areaHelper.isValid()) {
            return new BlockPattern.BlockPatternMatch(world, Direction.NORTH, Direction.UP, loadingCache, 1, 1, 1);
        } else {
            int[] is = new int[Direction.AxisDirection.values().length];
            Direction direction = areaHelper.negativeDir.getCounterClockWise();
            BlockPos blockPos = areaHelper.lowerCorner.above(areaHelper.getHeight() - 1);
            Direction.AxisDirection[] var8 = Direction.AxisDirection.values();
            int var9 = var8.length;

            int var10;
            for(var10 = 0; var10 < var9; ++var10) {
                Direction.AxisDirection axisDirection = var8[var10];
                BlockPattern.BlockPatternMatch result = new BlockPattern.BlockPatternMatch(direction.getAxisDirection() == axisDirection ? blockPos : blockPos.relative(areaHelper.negativeDir, areaHelper.getWidth() - 1), Direction.get(axisDirection, axis), Direction.UP, loadingCache, areaHelper.getWidth(), areaHelper.getHeight(), 1);

                for(int i = 0; i < areaHelper.getWidth(); ++i) {
                    for(int j = 0; j < areaHelper.getHeight(); ++j) {
                        BlockInWorld cachedBlockPosition = result.getBlock(i, j, 1);
                        if (!cachedBlockPosition.getState().isAir()) {
                            ++is[axisDirection.ordinal()];
                        }
                    }
                }
            }

            Direction.AxisDirection axisDirection2 = Direction.AxisDirection.POSITIVE;
            Direction.AxisDirection[] var17 = Direction.AxisDirection.values();
            var10 = var17.length;

            for(int var18 = 0; var18 < var10; ++var18) {
                Direction.AxisDirection axisDirection3 = var17[var18];
                if (is[axisDirection3.ordinal()] < is[axisDirection2.ordinal()]) {
                    axisDirection2 = axisDirection3;
                }
            }

            return new BlockPattern.BlockPatternMatch(direction.getAxisDirection() == axisDirection2 ? blockPos : blockPos.relative(areaHelper.negativeDir, areaHelper.getWidth() - 1), Direction.get(axisDirection2, axis), Direction.UP, loadingCache, areaHelper.getWidth(), areaHelper.getHeight(), 1);
        }
    }

    static {
        AXIS = BlockStateProperties.HORIZONTAL_AXIS;
        X_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
        Z_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    }

    public static class AreaHelper {
        private final LevelAccessor world;
        private final Direction.Axis axis;
        private final Direction negativeDir;
        private final Direction positiveDir;
        private int foundPortalBlocks;
        private BlockPos lowerCorner;
        private int height;
        private int width;
        private final DimensionData dimensionData;

        public AreaHelper(DimensionData dimensionData, LevelAccessor world, BlockPos pos, Direction.Axis axis) {
            this.world = world;
            this.axis = axis;
            this.dimensionData = dimensionData;
            if (axis == Direction.Axis.X) {
                this.positiveDir = Direction.EAST;
                this.negativeDir = Direction.WEST;
            } else {
                this.positiveDir = Direction.NORTH;
                this.negativeDir = Direction.SOUTH;
            }

            for(BlockPos blockPos = pos; pos.getY() > blockPos.getY() - 21 && pos.getY() > 0 && this.validStateInsidePortal(world.getBlockState(pos.below())); pos = pos.below()) {
            }

            int i = this.distanceToPortalEdge(pos, this.positiveDir) - 1;
            if (i >= 0) {
                this.lowerCorner = pos.relative(this.positiveDir, i);
                this.width = this.distanceToPortalEdge(this.lowerCorner, this.negativeDir);
                if (this.width < 2 || this.width > 21) {
                    this.lowerCorner = null;
                    this.width = 0;
                }
            }

            if (this.lowerCorner != null) {
                this.height = this.findHeight();
            }

        }

        protected int distanceToPortalEdge(BlockPos pos, Direction dir) {
            int i;
            for(i = 0; i < 22; ++i) {
                BlockPos blockPos = pos.relative(dir, i);
                if (!this.validStateInsidePortal(this.world.getBlockState(blockPos)) || this.world.getBlockState(blockPos.below()).getBlock() != Blocks.OBSIDIAN) {
                    break;
                }
            }

            Block block = this.world.getBlockState(pos.relative(dir, i)).getBlock();
            return block == Blocks.OBSIDIAN ? i : 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        protected int findHeight() {
            int i;
            label56:
            for(this.height = 0; this.height < 21; ++this.height) {
                for(i = 0; i < this.width; ++i) {
                    BlockPos blockPos = this.lowerCorner.relative(this.negativeDir, i).above(this.height);
                    BlockState blockState = this.world.getBlockState(blockPos);
                    if (!this.validStateInsidePortal(blockState)) {
                        break label56;
                    }

                    Block block = blockState.getBlock();
                    if (block == Registry.BLOCK.get(Utils.addSuffixToPath(dimensionData.getId(), "_custom_portal"))) {
                        ++this.foundPortalBlocks;
                    }

                    if (i == 0) {
                        block = this.world.getBlockState(blockPos.relative(this.positiveDir)).getBlock();
                        if (block != Blocks.OBSIDIAN) {
                            break label56;
                        }
                    } else if (i == this.width - 1) {
                        block = this.world.getBlockState(blockPos.relative(this.negativeDir)).getBlock();
                        if (block != Blocks.OBSIDIAN) {
                            break label56;
                        }
                    }
                }
            }

            for(i = 0; i < this.width; ++i) {
                if (this.world.getBlockState(this.lowerCorner.relative(this.negativeDir, i).above(this.height)).getBlock() != Blocks.OBSIDIAN) {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            } else {
                this.lowerCorner = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean validStateInsidePortal(BlockState state) {
            Block block = state.getBlock();
            return state.isAir() || state.is(BlockTags.FIRE) || block == Registry.BLOCK.get(Utils.addSuffixToPath(dimensionData.getId(), "_custom_portal"));
        }

        public boolean isValid() {
            return this.lowerCorner != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void createPortal() {
            for(int i = 0; i < this.width; ++i) {
                BlockPos blockPos = this.lowerCorner.relative(this.negativeDir, i);

                for(int j = 0; j < this.height; ++j) {
                    this.world.setBlock(blockPos.above(j), Registry.BLOCK.get(Utils.addSuffixToPath(dimensionData.getId(), "_custom_portal")).defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis), 18);
                }
            }

        }

        private boolean portalAlreadyExisted() {
            return this.foundPortalBlocks >= this.width * this.height;
        }

        public boolean wasAlreadyValid() {
            return this.isValid() && this.portalAlreadyExisted();
        }
    }
}