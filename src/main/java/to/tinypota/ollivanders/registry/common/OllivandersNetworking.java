package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.api.spell.SpellType;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.RaycastUtil;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.common.util.WandHelper;

public class OllivandersNetworking {
	public static final Identifier SWING_WAND_PACKET_ID = Ollivanders.id("swing_wand");
	
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(SWING_WAND_PACKET_ID, (server, sender, handler, buf, responseSender) -> {
			var uuid = buf.readUuid();
			var player = server.getPlayerManager().getPlayer(uuid);
			if (player != null) {
				var world = player.getWorld();
				var stack = player.getMainHandStack();
				var currentSpell = SpellHelper.getCurrentSpell(player);
				if (WandHelper.hasCore(stack) && !currentSpell.isEmpty()) {
					var serverState = OllivandersServerState.getServerState(server);
					var wandMatchLevel = WandHelper.getWandMatch(stack, player);
					serverState.addSkillLevel(player, currentSpell, 1 * wandMatchLevel.getExtraSkillGainPercentage());
					player.sendMessage(Text.literal("You just casted the spell " + currentSpell.getCastName() + "!"), true);
					if (currentSpell.getType() == SpellType.SELF) {
						currentSpell.onSelfCast(SpellPowerLevel.NORMAL, player);
						player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
						SpellHelper.emptyCurrentSpell(player);
					} else if (currentSpell.getType() == SpellType.RAYCAST) {
						var blockHitResult = RaycastUtil.raycastBlocks(world, player, 100, currentSpell.shouldHitWater());
						var entityHitResult = RaycastUtil.raycastEntities(world, player, 100);
						if (entityHitResult != null) {
							currentSpell.onHitEntity(SpellPowerLevel.NORMAL, world, entityHitResult, player);
							player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
							SpellHelper.emptyCurrentSpell(player);
						} else if (blockHitResult != null) {
							currentSpell.onHitBlock(SpellPowerLevel.NORMAL, world, blockHitResult, player);
							player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
							SpellHelper.emptyCurrentSpell(player);
						}
					} else if (currentSpell.getType() == SpellType.PROJECTILE) {
						world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
						
						if (!world.isClient) {
							var spellProjectileEntity = new SpellProjectileEntity(currentSpell, player, world);
							spellProjectileEntity.setPosition(spellProjectileEntity.getPos().add(0, 0.1 - 2 / 16F, 0));
							world.spawnEntity(spellProjectileEntity);
						}
						
						player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
						SpellHelper.emptyCurrentSpell(player);
					}
				}
			}
		});
	}
}