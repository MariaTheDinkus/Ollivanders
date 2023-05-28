package to.tinypota.ollivanders.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import to.tinypota.ollivanders.common.block.entity.OllivandersHangingSignBlockEntity;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;

public class OllivandersHangingSignBlock extends HangingSignBlock {
	public OllivandersHangingSignBlock(Settings settings, WoodType woodType) {
		super(settings, woodType);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new OllivandersHangingSignBlockEntity(pos, state);
	}
}
