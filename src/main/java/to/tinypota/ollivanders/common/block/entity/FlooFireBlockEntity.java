package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.client.OllivandersParticleTypes;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;

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
		if (!world.isClient()) {
			if (getCachedState().get(FlooFireBlock.ACTIVATION) == FlooActivation.ACTIVE) {
				if (activeTick < 200) {
					activeTick++;
				} else {
					activeTick = 0;
					world.setBlockState(pos, getCachedState().with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
				}
			} else if (getCachedState().get(FlooFireBlock.ACTIVATION) == FlooActivation.LEFT) {
				if (leftTick == 0) {
					((ServerWorld) world).spawnParticles(OllivandersParticleTypes.FLOO_FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2000, 0.375, 0.375 + 0.5, 0.375, 0);
					leftTick++;
				} else if (leftTick < 60) {
					leftTick++;
				} else {
					leftTick = 0;
					world.setBlockState(pos, getCachedState().with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
				}
			} else if (getCachedState().get(FlooFireBlock.ACTIVATION) == FlooActivation.ARRIVED) {
				if (arrivedTick < 60) {
					arrivedTick++;
				} else {
					arrivedTick = 0;
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
