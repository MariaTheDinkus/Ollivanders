package to.tinypota.ollivanders.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

import java.util.List;

public class CabinetCoreItem extends Item {
	public CabinetCoreItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.literal("This core has not been split.").formatted(Formatting.GRAY));
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//		var stack = user.getStackInHand(hand);
//		if (!world.isClient()) {
//			if (!user.isCreative()) {
//				user.setStackInHand(hand, ItemStack.EMPTY);
//			}
//			user.getInventory().offerOrDrop(new ItemStack(OllivandersItems.SPLIT_CABINET_CORE, 2));
//			return TypedActionResult.success(user.getStackInHand(hand));
//		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}
