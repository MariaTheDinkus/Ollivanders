package to.tinypota.ollivanders.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.spell.SpellType;
import to.tinypota.ollivanders.common.util.RaycastUtil;
import to.tinypota.ollivanders.registry.common.OllivandersSpells;

public class WandItem extends Item {
	private static final String KEY_PLAYER_UUID = "player_uuid";
	
	public WandItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		var currentSpell = OllivandersSpells.getCurrentSpell(user);
		if (!currentSpell.isEmpty()) {
			user.sendMessage(Text.literal("You just casted the spell " + currentSpell.getCastName() + "!"), true);
			if (currentSpell.getType() == SpellType.SELF) {
				currentSpell.onSelfCast(user);
				OllivandersSpells.emptyCurrentSpell(user);
			} else if (currentSpell.getType() == SpellType.RAYCAST) {
				BlockHitResult blockHitResult = RaycastUtil.raycastBlocks(world, user, 100, currentSpell.shouldHitWater());
				EntityHitResult entityHitResult = RaycastUtil.raycastEntities(world, user, 100);
				if (entityHitResult != null) {
					currentSpell.onHitEntity(world, entityHitResult);
					OllivandersSpells.emptyCurrentSpell(user);
				} else if (blockHitResult != null) {
					currentSpell.onHitBlock(world, blockHitResult);
					OllivandersSpells.emptyCurrentSpell(user);
				}
			} else if (currentSpell.getType() == SpellType.PROJECTILE) {
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
				
				if (!world.isClient) {
					var spellProjectileEntity = new SpellProjectileEntity(Spell.EMPTY, user, world);
					spellProjectileEntity.setPosition(spellProjectileEntity.getPos().add(0, 0.1 - 2 / 16F, 0));
					world.spawnEntity(spellProjectileEntity);
				}
				
				user.incrementStat(Stats.USED.getOrCreateStat(this));
				OllivandersSpells.emptyCurrentSpell(user);
			}
			return TypedActionResult.success(stack, world.isClient());
		}
		return TypedActionResult.pass(stack);
	}
}
