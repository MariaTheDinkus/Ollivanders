package to.tinypota.ollivanders.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.common.util.RandomUtil;

import java.util.List;
import java.util.UUID;

public class SplitCabinetCoreItem extends Item {
	private static final String KEY_CORE_UUID = "core_uuid";
	private static final String KEY_CORE_STRING = "core_string";
	
	public SplitCabinetCoreItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains(KEY_CORE_UUID, NbtElement.INT_ARRAY_TYPE)) {
			var hash = compound.getString(KEY_CORE_STRING);
			tooltip.add(Text.literal("This core is linked to ").formatted(Formatting.GRAY).append(Text.literal(hash).formatted(Formatting.AQUA)));
		}
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient() && entity instanceof PlayerEntity) {
			var compound = stack.getOrCreateNbt();
			if (!compound.contains(KEY_CORE_UUID, NbtElement.INT_ARRAY_TYPE)) {
				var uuid = UUID.randomUUID();
				compound.putUuid(KEY_CORE_UUID, uuid);
				compound.putString(KEY_CORE_STRING, RandomUtil.getRandomWordsFromUUID(uuid, 3));
			}
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}
