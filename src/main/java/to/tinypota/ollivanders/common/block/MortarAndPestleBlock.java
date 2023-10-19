package to.tinypota.ollivanders.common.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.common.block.entity.MortarAndPestleBlockEntity;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeTypes;

public class MortarAndPestleBlock extends BlockWithEntity {
	public MortarAndPestleBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		//TODO Appropriate shape
		return VoxelShapes.cuboid(4F / 16F, 0, 4F / 16F, 12F / 16F, 4F / 16F, 12F / 16F);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof MortarAndPestleBlockEntity) {
			var blockEntity = (MortarAndPestleBlockEntity) world.getBlockEntity(pos);
			var heldStack = player.getStackInHand(hand);
			var inventory = new SimpleInventory(heldStack);
			var matchGetter = RecipeManager.createCachedMatchGetter(OllivandersRecipeTypes.MORTAR_AND_PESTLE);
			var match = matchGetter.getFirstMatch(inventory, world);
			if (blockEntity.getStack().isEmpty() && !heldStack.isEmpty() && match.isPresent()) {
				var heldStackItem = heldStack.getItem();
				if (!world.isClient()) {
					blockEntity.setStack(heldStack.copy());
					player.setStackInHand(hand, ItemStack.EMPTY);
//					if (heldStack.getCount() > 1) {
//						blockEntity.setStack(heldStack.copyWithCount(1));
//						heldStack.decrement(1);
//					} else {
//						blockEntity.setStack(heldStack.copyWithCount(1));
//						player.setStackInHand(hand, ItemStack.EMPTY);
//					}
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
		return new MortarAndPestleBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return MortarAndPestleBlockEntity::tick;
	}
}
