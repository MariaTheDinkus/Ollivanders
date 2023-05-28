package to.tinypota.ollivanders.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.common.block.entity.FlooFireBlockEntity;
import to.tinypota.ollivanders.common.block.entity.LatheBlockEntity;
import to.tinypota.ollivanders.common.util.WandCraftHelper;

public class LatheBlock extends BlockWithEntity {
	public LatheBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof LatheBlockEntity) {
			var blockEntity = (LatheBlockEntity) world.getBlockEntity(pos);
			var heldStack = player.getStackInHand(hand);
			if (blockEntity.getStack().isEmpty() && !heldStack.isEmpty() && WandCraftHelper.canCraft(heldStack.getItem())) {
				var heldStackItem = heldStack.getItem();
				if (!world.isClient()) {
					if (heldStack.getCount() > 1) {
						blockEntity.setStack(heldStack.copyWithCount(1));
						heldStack.decrement(1);
					} else {
						blockEntity.setStack(heldStack.copyWithCount(1));
						player.setStackInHand(hand, ItemStack.EMPTY);
					}
				}
				return ActionResult.SUCCESS;
			} else if (!blockEntity.getStack().isEmpty()) {
				if (!world.isClient()) {
					var itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, blockEntity.getStack().copy(), 0, 0, 0);
					world.spawnEntity(itemEntity);
					blockEntity.setStack(ItemStack.EMPTY);
				}
				
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LatheBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return LatheBlockEntity::tick;
	}
}
