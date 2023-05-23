package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.api.spell.SpellType;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.RaycastUtil;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.common.util.WandHelper;

public class OllivandersNetworking {
	public static final Identifier DECREASE_POWER_LEVEL = Ollivanders.id("decrease_power_level");
	public static final Identifier INCREASE_POWER_LEVEL = Ollivanders.id("increase_power_level");
	
	public static final Identifier SET_FLOO_FIRE_NAME = Ollivanders.id("set_floor_fire_name");
	public static final Identifier SWING_WAND_PACKET_ID = Ollivanders.id("swing_wand");
	
	public static final Identifier OPEN_FLOO_FIRE_NAME_SCREEN = Ollivanders.id("open_floo_fire_name_screen");
	public static final Identifier SYNC_POWER_LEVELS = Ollivanders.id("sync_power_levels");
	
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(SYNC_POWER_LEVELS, (server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
				var serverState = OllivandersServerState.getServerState(server);
				serverState.syncPowerLevels(player);
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(DECREASE_POWER_LEVEL, (server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
				var serverState = OllivandersServerState.getServerState(server);
				serverState.decreasePowerLevel(player);
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(INCREASE_POWER_LEVEL, (server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
				var serverState = OllivandersServerState.getServerState(server);
				serverState.increasePowerLevel(player);
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SET_FLOO_FIRE_NAME, (server, player, handler, buf, responseSender) -> {
			var name = buf.readString();
			var pos = buf.readBlockPos();
			var direction = buf.readInt();
			server.execute(() -> {
				var serverState = OllivandersServerState.getServerState(server);
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					Ollivanders.LOGGER.info("Adding fire to floo network under name: " + name + ". The position is " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "." + " The facing direction is: " + Direction.byId(direction).getName());
				}
				serverState.addFlooPosition(name, pos, Direction.byId(direction));
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(SWING_WAND_PACKET_ID, (server, sender, handler, buf, responseSender) -> {
			var uuid = buf.readUuid();
			server.execute(() -> {
				var player = server.getPlayerManager().getPlayer(uuid);
				if (player != null) {
					var world = player.getWorld();
					var stack = player.getMainHandStack();
					var currentSpell = SpellHelper.getCurrentSpell(player);
					if (WandHelper.hasCore(stack) && !currentSpell.isEmpty()) {
						var serverState = OllivandersServerState.getServerState(server);
						var wandMatchLevel = WandHelper.getWandMatch(stack, player);
						var powerLevel = serverState.getPowerLevel(player);
						serverState.addSkillLevel(player, currentSpell, 1 * wandMatchLevel.getExtraSkillGainPercentage());
						player.sendMessage(Text.literal("You just casted the spell " + currentSpell.getCastName() + "!"), true);
						if (currentSpell.getType() == SpellType.SELF) {
							currentSpell.onSelfCast(powerLevel, player);
							player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
							SpellHelper.emptyCurrentSpell(player);
						} else if (currentSpell.getType() == SpellType.RAYCAST) {
							var blockHitResult = RaycastUtil.raycastBlocks(world, player, 100, currentSpell.shouldHitWater());
							var entityHitResult = RaycastUtil.raycastEntities(world, player, 100);
							if (entityHitResult != null) {
								currentSpell.onHitEntity(powerLevel, world, entityHitResult, player);
								player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
								SpellHelper.emptyCurrentSpell(player);
							} else if (blockHitResult != null) {
								currentSpell.onHitBlock(powerLevel, world, blockHitResult, player);
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
		});
	}
}