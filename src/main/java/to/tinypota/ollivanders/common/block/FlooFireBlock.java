package to.tinypota.ollivanders.common.block;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
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
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.entity.FlooFireBlockEntity;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.client.OllivandersParticleTypes;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

public class FlooFireBlock extends BlockWithEntity {
	public static final BooleanProperty LIT = Properties.LIT;
	public static final EnumProperty<FlooActivation> ACTIVATION = EnumProperty.of("activation", FlooActivation.class);
	private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	
	public FlooFireBlock(Block.Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(LIT, true).with(ACTIVATION, FlooActivation.OFF));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var stack = player.getStackInHand(hand);
		if (!world.isClient() && hand == Hand.MAIN_HAND && player.getStackInHand(hand).isEmpty()) {
			var serverState = OllivandersServerState.getServerState(world.getServer());
			
			if (serverState.getFlooNameByPos(pos) != null) {
				player.sendMessage(Text.literal("This floo fire is called: " + serverState.getFlooNameByPos(pos)), true);
			}
			
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				serverState.getFlooState().getFlooPositions().entrySet().forEach(entry -> {
					var name = entry.getKey();
					var pair = entry.getValue();
					Ollivanders.LOGGER.info("Found floo fireplace called: " + name + ". The position is " + pair.getLeft().getX() + ", " + pair.getLeft().getY() + ", " + pair.getLeft().getZ() + "." + " The facing direction is: " + pair.getRight().getName());
				});
			}
		}
		
		if (!state.get(LIT)) {
			if (!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL) {
				world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
				if (!world.isClient()) {
					world.setBlockState(pos, state.with(LIT, true).with(ACTIVATION, FlooActivation.OFF));
					if (!player.isCreative()) {
						stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
					}
				}
				return ActionResult.SUCCESS;
			}
		}
		
		if (state.get(LIT) && state.get(ACTIVATION) == FlooActivation.OFF && !stack.isEmpty() && stack.getItem() == OllivandersItems.FLOO_POWDER) {
			if (!world.isClient()) {
				world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, 0);
				((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 200, 0.375, 0.375, 0.375, 0);
				world.setBlockState(pos, state.with(ACTIVATION, FlooActivation.ACTIVE));
				if (!player.isCreative()) {
					if (stack.getCount() > 1) {
						stack.decrement(1);
					} else {
						player.setStackInHand(hand, ItemStack.EMPTY);
					}
				}
			}
			return ActionResult.SUCCESS;
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient() && state.get(LIT)) {
			if (state.get(ACTIVATION) == FlooActivation.OFF && entity instanceof ItemEntity itemEntity) {
				var stack = itemEntity.getStack();
				if (!stack.isEmpty() && stack.getItem() == OllivandersItems.FLOO_POWDER) {
					world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, 0);
					((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 200, 0.375, 0.375, 0.375, 0);
					if (stack.getCount() > 1) {
						stack.decrement(1);
					} else {
						entity.discard();
					}
					world.setBlockState(pos, state.with(ACTIVATION, FlooActivation.ACTIVE));
				}
			} else if (state.get(ACTIVATION) == FlooActivation.OFF) {
				if (!entity.isFireImmune()) {
					entity.setFireTicks(entity.getFireTicks() + 1);
					if (entity.getFireTicks() == 0) {
						entity.setOnFireFor(8);
					}
				}
				
				entity.damage(world.getDamageSources().inFire(), 1.0F);
			}
		}
		super.onEntityCollision(state, world, pos, entity);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FlooFireBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return FlooFireBlockEntity::tick;
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
		if (direction == Direction.DOWN && !canPlaceAt(state, world, pos)) {
			if (!world.isClient()) {
				var serverState = OllivandersServerState.getServerState(world.getServer());
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					Ollivanders.LOGGER.info("Removing fire from floo network at position " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ".");
				}
				serverState.removeFlooByPos(pos);
			}
			return Blocks.AIR.getDefaultState();
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP) && super.canPlaceAt(state, world, pos);
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient()) {
			var serverState = OllivandersServerState.getServerState(world.getServer());
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				Ollivanders.LOGGER.info("Removing fire from floo network at position " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ".");
			}
			serverState.removeFlooByPos(pos);
		}
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT, ACTIVATION);
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
