package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

public class DiffindoSpell extends Spell {
	public DiffindoSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getMaximumPowerLevel() {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public double getCustomCastPercents(SpellPowerLevel powerLevel) {
		return 0.50;
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity, SpellProjectileEntity spellProjectileEntity) {
		var pos = hitResult.getBlockPos();
		var sidePos = pos.offset(hitResult.getSide());
		// Check sidepos bounding box area of 1 for item entity
		var entities = world.getEntitiesByClass(ItemEntity.class, new Box(sidePos.getX(), sidePos.getY(), sidePos.getZ(), sidePos.getX() + 1, sidePos.getY() + 1, sidePos.getZ() + 1), entity -> true);
		
		for (var itemEntity : entities) {
			if (!itemEntity.getStack().isEmpty() && itemEntity.getStack().getItem().equals(OllivandersItems.CABINET_CORE)) {
				itemEntity.setStack(new ItemStack(OllivandersItems.SPLIT_CABINET_CORE, 2));
				world.playSound(null, sidePos, SoundEvents.ENTITY_ENDER_EYE_DEATH, playerEntity.getSoundCategory(), 1.0F, 1.0F);
				return ActionResult.SUCCESS;
			}
		}
		
		return ActionResult.SUCCESS;
	}
	
	@Override
	protected ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult) {
		var entity = hitResult.getEntity();
		if (entity instanceof ItemEntity) {
			ItemEntity itemEntity = (ItemEntity) entity;
			if (!itemEntity.getStack().isEmpty() && itemEntity.getStack().getItem().equals(OllivandersItems.CABINET_CORE)) {
				itemEntity.setStack(new ItemStack(OllivandersItems.SPLIT_CABINET_CORE, 2));
				world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.SUCCESS;
	}
}
