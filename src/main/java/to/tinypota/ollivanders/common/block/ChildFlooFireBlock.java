package to.tinypota.ollivanders.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class ChildFlooFireBlock extends Block {
	public static final BooleanProperty LIT = Properties.LIT;
	public static final EnumProperty<FlooActivation> ACTIVATION = EnumProperty.of("activation", FlooActivation.class);
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	
	public ChildFlooFireBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(LIT, true).with(ACTIVATION, FlooActivation.OFF).with(FACING, Direction.NORTH));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var facing = state.get(FACING);
		var parentState = world.getBlockState(pos.offset(facing));
		var newHit = new BlockHitResult(hit.getPos().offset(facing, 1), hit.getSide(), pos.offset(facing), hit.isInsideBlock());
		return parentState.getBlock() == OllivandersBlocks.FLOO_FIRE ? parentState.onUse(world, player, hand, newHit) : super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		var facing = state.get(FACING);
		var parentState = world.getBlockState(pos.offset(facing));
		if (parentState.getBlock() == OllivandersBlocks.FLOO_FIRE) {
			parentState.onEntityCollision(world, pos.offset(facing), entity);
		}
		super.onEntityCollision(state, world, pos, entity);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BASE_SHAPE;
	}
	
	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (state.get(LIT)) {
			if (!world.isClient()) {
				world.syncWorldEvent(null, 1009, pos, 0);
			}
			world.setBlockState(pos, state.with(LIT, false).with(ACTIVATION, FlooActivation.OFF));
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.offset(state.get(FACING))).getBlock() == OllivandersBlocks.FLOO_FIRE && world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP) && super.canPlaceAt(state, world, pos);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT, ACTIVATION, FACING);
	}
	
	@Override
	protected void spawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state) {
	
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.get(LIT)) {
			if (random.nextInt(24) == 0) {
				world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
			
			BlockPos blockPos = pos.down();
			BlockState blockState = world.getBlockState(blockPos);
			for (var i = 0; i < 3; ++i) {
				var d = pos.getX() + random.nextDouble();
				var e = pos.getY() + random.nextDouble() * 0.5D + 0.5D;
				var f = pos.getZ() + random.nextDouble();
				world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
