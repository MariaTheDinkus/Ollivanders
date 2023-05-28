package to.tinypota.ollivanders.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import to.tinypota.ollivanders.common.block.entity.OllivandersHangingSignBlockEntity;

public class OllivandersWallHangingSignBlock extends WallHangingSignBlock {
	public OllivandersWallHangingSignBlock(Settings settings, WoodType woodType) {
		super(settings, woodType);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new OllivandersHangingSignBlockEntity(pos, state);
	}
}
