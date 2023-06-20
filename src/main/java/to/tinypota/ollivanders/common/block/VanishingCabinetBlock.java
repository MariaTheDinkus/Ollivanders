package to.tinypota.ollivanders.common.block;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.DQuaternion;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.entity.VanishingCabinetBlockEntity;
import to.tinypota.ollivanders.common.item.SplitCabinetCoreItem;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.ShapeHelper;

public class VanishingCabinetBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
	public static final BooleanProperty OPEN = Properties.OPEN;
	
	public VanishingCabinetBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER).with(OPEN, false));
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient() && state.get(HALF) == DoubleBlockHalf.LOWER) {
			var serverState = OllivandersServerState.getServerState(world.getServer());
			var vanishingCabinetState = serverState.getVanishingCabinetState();
			var facing = state.get(FACING);
			world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
			
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				Ollivanders.LOGGER.info("Adding cabinet at position " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ".");
			}
			vanishingCabinetState.setVanishingCabinet(pos, facing.getOpposite(), world.getRegistryKey().getValue());
			
			var open = state.get(OPEN);
			Portal portal = Portal.entityType.create(world);
			var originPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			var axisW = new Vec3d(0, 0, 0);
			if (facing == Direction.NORTH) {
				axisW = new Vec3d(1, 0, 0);
			} else if (facing == Direction.EAST) {
				axisW = new Vec3d(0, 0, 1);
			} else if (facing == Direction.SOUTH) {
				axisW = new Vec3d(-1, 0, 0);
			} else if (facing == Direction.WEST) {
				axisW = new Vec3d(0, 0, -1);
			}
			portal.setOriginPos(originPos);
			portal.setDestinationDimension(World.OVERWORLD);
			portal.setDestination(originPos);
			portal.setOrientationAndSize(
					axisW, // axisW
					new Vec3d(0, 1, 0), // axisH
					1, // width
					2 // height
			);
			portal.setIsVisible(false);
			portal.setTeleportable(false);
			portal.getWorld().spawnEntity(portal);
		}
		super.onPlaced(world, pos, state, placer, itemStack);
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient()) {
			var serverState = OllivandersServerState.getServerState(world.getServer());
			var vanishingCabinetState = serverState.getVanishingCabinetState();
			
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				Ollivanders.LOGGER.info("Removing cabinet at position " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ".");
			}
			vanishingCabinetState.removeVanishingCabinet(pos);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SidedInventory) {
				SidedInventory inventory = (SidedInventory) blockEntity;
				
				for (int i = 0; i < inventory.size(); i++) {
					ItemStack stack = inventory.getStack(i);
					dropStack(world, pos, stack);
					inventory.removeStack(i);
				}
			}
		}
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
			return neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf ? state.with(FACING, neighborState.get(FACING)).with(OPEN, neighborState.get(OPEN)) : Blocks.AIR.getDefaultState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}
	
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		return state.get(HALF) == DoubleBlockHalf.LOWER ? world.isAir(pos.up()) && blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) : blockState.isOf(this);
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		var box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
		if (state.getBlock() != newState.getBlock()) {
			world.getEntitiesByType(Portal.entityType, box, portal -> true).forEach(Entity::discard);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING).add(HALF).add(OPEN);
	}
	
	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var left = VoxelShapes.cuboid(0, 0, 0, 1F / 16F, 1, 1);
		var right = VoxelShapes.cuboid(15F / 16F, 0, 0, 1, 1, 1);
		var half = state.get(HALF) == DoubleBlockHalf.LOWER ? VoxelShapes.cuboid(0, 0, 0, 1, 1F / 16F, 1) : VoxelShapes.cuboid(0, 15F / 16F, 0, 1, 1, 1);
		var back = VoxelShapes.cuboid(0, 0, 0, 1 , 1, 1F / 16F);
		var front = VoxelShapes.cuboid(0, 0, 15F / 16F, 1, 1, 1);
		var union = VoxelShapes.union(left, right, back, half, front);
		return ShapeHelper.rotateShapeTowards(state.get(FACING), union);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var left = VoxelShapes.cuboid(0, 0, 0, 1F / 16F, 1, 1);
		var right = VoxelShapes.cuboid(15F / 16F, 0, 0, 1, 1, 1);
		var half = state.get(HALF) == DoubleBlockHalf.LOWER ? VoxelShapes.cuboid(0, 0, 0, 1, 1F / 16F, 1) : VoxelShapes.cuboid(0, 15F / 16F, 0, 1, 1, 1);
		var back = VoxelShapes.cuboid(0, 0, 0, 1 , 1, 1F / 16F);
		var front = VoxelShapes.cuboid(0, 0, 15F / 16F, 1, 1, 1);
		var union = VoxelShapes.union(left, right, back, half);
		if (!state.get(OPEN))
			union = VoxelShapes.union(union, front);
		return ShapeHelper.rotateShapeTowards(state.get(FACING), union);
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var stack = player.getStackInHand(hand);
		if (!stack.isEmpty() && stack.getItem() instanceof SplitCabinetCoreItem) {
			var blockEntity = (VanishingCabinetBlockEntity) world.getBlockEntity(pos);
			if (blockEntity.isEmpty()) {
				if (!world.isClient) {
					blockEntity.setStack(0, stack.copyWithCount(1));
					stack.decrement(1);
				}
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.PASS;
			}
		}
		
		if (player.isSneaking()){
			if (state.get(HALF) == DoubleBlockHalf.UPPER) {
				player.openHandledScreen(world.getBlockState(pos.down()).createScreenHandlerFactory(world, pos.down()));
				return ActionResult.SUCCESS;
			} else {
				player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
				return ActionResult.SUCCESS;
			}
		}
		
		BlockPos otherPos;
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			otherPos = pos.offset(Direction.UP);
		} else {
			otherPos = pos.offset(Direction.DOWN);
		}
		world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
		world.setBlockState(otherPos, world.getBlockState(otherPos).with(OPEN, !state.get(OPEN)));
		world.playSound(player, pos, state.get(OPEN) ? BlockSetType.OAK.doorOpen() : BlockSetType.OAK.doorClose(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
		return ActionResult.SUCCESS;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER ? new VanishingCabinetBlockEntity(pos, state) : null;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return VanishingCabinetBlockEntity::tick;
	}
}
