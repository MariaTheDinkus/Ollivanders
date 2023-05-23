package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.ChildFlooFireBlock;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.client.OllivandersParticleTypes;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class FlooFireBlockEntity extends BlockEntity {
	private int activeTick = 0;
	private int leftTick = 0;
	private int arrivedTick = 0;
	private OllivandersServerState serverState;
	
	public FlooFireBlockEntity(BlockPos pos, BlockState state) {
		super(OllivandersBlockEntityTypes.FLOO_FIRE, pos, state);
	}
	
	public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof FlooFireBlockEntity fire) {
			fire.tick();
		}
	}
	
	public OllivandersServerState getServerState() {
		if (!world.isClient() && serverState == null) {
			serverState = OllivandersServerState.getServerState(world.getServer());
		}
		return serverState;
	}
	
	private void tick() {
		var serverState = getServerState();
		var state = getCachedState();
		if (!world.isClient()) {
			if (world.getTime() % 5 == 0) {
				Direction[] directions = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
				for (Direction dir : directions) {
					BlockPos newPos = pos.offset(dir);
					BlockState newState = world.getBlockState(newPos);
					if (newState.isOf(Blocks.FIRE)) {
						world.setBlockState(newPos, OllivandersBlocks.CHILD_FLOO_FIRE.getDefaultState()
																					 .with(ChildFlooFireBlock.LIT, state.get(FlooFireBlock.LIT))
																					 .with(ChildFlooFireBlock.ACTIVATION, state.get(FlooFireBlock.ACTIVATION))
																					 .with(ChildFlooFireBlock.FACING, dir.getOpposite()));
					}
//				else if (newState.isOf(OllivandersBlocks.CHILD_FLOO_FIRE) && newState.get(ChildFlooFireBlock.FACING) == dir.getOpposite()) {
//					if (!newState.get(ChildFlooFireBlock.LIT).equals(state.get(FlooFireBlock.LIT)) || !newState.get(ChildFlooFireBlock.ACTIVATION).equals(state.get(FlooFireBlock.ACTIVATION))) {
//						world.setBlockState(newPos, newState
//								.with(ChildFlooFireBlock.LIT, state.get(FlooFireBlock.LIT))
//								.with(ChildFlooFireBlock.ACTIVATION, state.get(FlooFireBlock.ACTIVATION)));
//					}
//				}
				}
			}
			
			if (getCachedState().get(FlooFireBlock.ACTIVATION) == FlooActivation.ACTIVE) {
				if (activeTick < 200) {
					activeTick++;
				} else {
					activeTick = 0;
					world.setBlockState(pos, getCachedState().with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
				}
			} else if (getCachedState().get(FlooFireBlock.ACTIVATION) == FlooActivation.LEFT) {
				if (leftTick == 0) {
					((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 500, 0.375, 0.375 + 0.5, 0.375, 0);
					Direction[] directions = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
					for (Direction dir : directions) {
						BlockPos newPos = pos.offset(dir);
						BlockState newState = world.getBlockState(newPos);
						
						if (newState.isOf(OllivandersBlocks.CHILD_FLOO_FIRE)) {
							((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5, 500, 0.375, 0.375 + 0.5, 0.375, 0);
						}
					}
					leftTick++;
				} else if (leftTick < 60) {
					leftTick++;
				} else {
					leftTick = 0;
					((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 200, 0.375, 0.375 + 0.5, 0.375, 0);
					Direction[] directions = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
					for (Direction dir : directions) {
						BlockPos newPos = pos.offset(dir);
						BlockState newState = world.getBlockState(newPos);
						
						if (newState.isOf(OllivandersBlocks.CHILD_FLOO_FIRE)) {
							((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5, 200, 0.375, 0.375 + 0.5, 0.375, 0);
						}
					}
					world.setBlockState(pos, getCachedState().with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
				}
			} else if (getCachedState().get(FlooFireBlock.ACTIVATION) == FlooActivation.ARRIVED) {
				if (arrivedTick == 0) {
					((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 200, 0.375, 0.375, 0.375, 0);
					Direction[] directions = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
					for (Direction dir : directions) {
						BlockPos newPos = pos.offset(dir);
						BlockState newState = world.getBlockState(newPos);
						
						if (newState.isOf(OllivandersBlocks.CHILD_FLOO_FIRE)) {
							((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5, 200, 0.375, 0.375, 0.375, 0);
						}
					}
					arrivedTick++;
				} else if (arrivedTick < 60) {
					arrivedTick++;
				} else {
					arrivedTick = 0;
					((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 200, 0.375, 0.375 + 0.5, 0.375, 0);
					Direction[] directions = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
					for (Direction dir : directions) {
						BlockPos newPos = pos.offset(dir);
						BlockState newState = world.getBlockState(newPos);
						
						if (newState.isOf(OllivandersBlocks.CHILD_FLOO_FIRE)) {
							((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5, 200, 0.375, 0.375 + 0.5, 0.375, 0);
						}
					}
					world.setBlockState(pos, getCachedState().with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
				}
			} else {
				activeTick = 0;
				leftTick = 0;
				arrivedTick = 0;
			}
		} else {
			activeTick = 0;
			leftTick = 0;
			arrivedTick = 0;
		}
	}
}
