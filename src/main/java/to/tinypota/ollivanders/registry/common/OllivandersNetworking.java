package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.spell.PowerLevel;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.spell.SpellType;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.RaycastUtil;

public class OllivandersNetworking {
	public static final Identifier SWING_WAND_PACKET_ID = Ollivanders.id("swing_wand");
	
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(SWING_WAND_PACKET_ID, (server, player, handler, buf, responseSender) -> {
			var uuid = buf.readUuid();
			if (player.getUuid().equals(uuid)) {
				var world = player.getWorld();
				var stack = player.getMainHandStack();
				var currentSpell = OllivandersSpells.getCurrentSpell(player);
				if (WandItem.hasCore(stack) && !currentSpell.isEmpty()) {
					var serverState = OllivandersServerState.getServerState(server);
					var wandMatchLevel = WandItem.getWandMatch(stack, player);
					serverState.addSkillLevel(player, currentSpell, 1 * wandMatchLevel.getExtraSkillGainPercentage());
					player.sendMessage(Text.literal("You just casted the spell " + currentSpell.getCastName() + "!"), true);
					if (currentSpell.getType() == SpellType.SELF) {
						currentSpell.onSelfCast(PowerLevel.NORMAL, player);
						player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
						OllivandersSpells.emptyCurrentSpell(player);
					} else if (currentSpell.getType() == SpellType.RAYCAST) {
						var blockHitResult = RaycastUtil.raycastBlocks(world, player, 100, currentSpell.shouldHitWater());
						var entityHitResult = RaycastUtil.raycastEntities(world, player, 100);
						if (entityHitResult != null) {
							currentSpell.onHitEntity(PowerLevel.NORMAL, world, entityHitResult);
							player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
							OllivandersSpells.emptyCurrentSpell(player);
						} else if (blockHitResult != null) {
							currentSpell.onHitBlock(PowerLevel.NORMAL, world, blockHitResult);
							player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
							OllivandersSpells.emptyCurrentSpell(player);
						}
					} else if (currentSpell.getType() == SpellType.PROJECTILE) {
						world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
						
						if (!world.isClient) {
							var spellProjectileEntity = new SpellProjectileEntity(Spell.EMPTY, player, world);
							spellProjectileEntity.setPosition(spellProjectileEntity.getPos().add(0, 0.1 - 2 / 16F, 0));
							world.spawnEntity(spellProjectileEntity);
						}
						
						player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
						OllivandersSpells.emptyCurrentSpell(player);
					}
				}
			}
		});
	}
}