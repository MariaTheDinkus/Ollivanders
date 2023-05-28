package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.ChildFlooFireBlock;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.item.WandItem;

public class OllivandersEvents {
	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof EnderDragonEntity) {
				var itemEntity = new ItemEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(OllivandersItems.DRAGON_HEARTSTRING, 3));
				entity.getWorld().spawnEntity(itemEntity);
			}
		});
		
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			var state = world.getBlockState(pos);
			if (player.isCreative() && state.getBlock() instanceof FlooFireBlock) {
				if (!world.isClient()) {
					if (state.get(Properties.LIT)) {
						world.syncWorldEvent(null, 1009, pos, 0);
						world.setBlockState(pos, state.with(Properties.LIT, false).with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
						return ActionResult.FAIL;
					} else {
						return ActionResult.PASS;
					}
				}
			}
			
			if (player.isCreative() && state.getBlock() instanceof ChildFlooFireBlock) {
				if (!world.isClient()) {
					var newPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));
					var newState = world.getBlockState(newPos);
					if (newState.getBlock() == OllivandersBlocks.FLOO_FIRE) {
						if (newState.get(Properties.LIT)) {
							world.syncWorldEvent(null, 1009, newPos, 0);
							world.setBlockState(newPos, newState.with(Properties.LIT, false).with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
							return ActionResult.FAIL;
						} else {
							return ActionResult.PASS;
						}
					}
				}
			}
			
			if (!player.getStackInHand(hand).isEmpty() && player.getStackInHand(hand).getItem() instanceof WandItem) {
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		});
	}
}
