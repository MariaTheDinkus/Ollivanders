package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;

public class OllivandersHangingSignBlockEntity extends SignBlockEntity {
	private static final int MAX_TEXT_WIDTH = 60;
	private static final int TEXT_LINE_HEIGHT = 9;
	
	public OllivandersHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(OllivandersBlockEntityTypes.HANGING_SIGN, blockPos, blockState);
	}
	
	@Override
	public int getTextLineHeight() {
		return 9;
	}
	
	@Override
	public int getMaxTextWidth() {
		return 60;
	}
}
