package to.tinypota.ollivanders.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;

public class OllivandersWallSignBlock extends WallSignBlock {
	public OllivandersWallSignBlock(Settings settings, WoodType woodType) {
		super(settings, woodType);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SignBlockEntity(OllivandersBlockEntityTypes.SIGN, pos, state);
	}
}
