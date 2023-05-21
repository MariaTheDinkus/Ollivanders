package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;

public class FlooFireBlockEntity extends BlockEntity {
	private int tick = 0;
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
		if (!world.isClient()) {if (getCachedState().get(FlooFireBlock.ACTIVE)) {
				if (tick < 200) {
					tick++;
				} else {
					tick = 0;
					world.setBlockState(pos, getCachedState().with(FlooFireBlock.ACTIVE, false));
				}
			} else {
				tick = 0;
			}
		} else {
			tick = 0;
		}
	}
}
