package to.tinypota.ollivanders.common.spell;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

public class AliquamFlooSpell extends Spell {
	public AliquamFlooSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getMaximumPowerLevel() {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public double getCustomCastPercents(SpellPowerLevel powerLevel) {
		return 0.20;
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity, SpellProjectileEntity spellProjectileEntity) {
		var pos = spellProjectileEntity.getBlockPos();
		var state = world.getBlockState(pos);
		if (state.getBlock() != Blocks.FIRE || state.getBlock() != OllivandersBlocks.FLOO_FIRE) {
			var sideState = world.getBlockState(hitResult.getBlockPos().offset(hitResult.getSide()));
			if (sideState.getBlock() == Blocks.FIRE || sideState.getBlock() == OllivandersBlocks.FLOO_FIRE) {
				pos = hitResult.getBlockPos().offset(hitResult.getSide());
				state = sideState;
			}
		}
		
		if (playerEntity instanceof ServerPlayerEntity) {
			if (state.getBlock() == Blocks.FIRE) {
				var newState = OllivandersBlocks.FLOO_FIRE.getDefaultState();
				world.setBlockState(pos, newState);
				
				var buf = PacketByteBufs.create();
				buf.writeString("");
				buf.writeBlockPos(pos);
				buf.writeInt(playerEntity.getHorizontalFacing().getOpposite().getId());
				buf.writeBoolean(true);
				ServerPlayNetworking.send((ServerPlayerEntity) playerEntity, OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN, buf);
				return ActionResult.SUCCESS;
			}
			
			if (state.getBlock() == OllivandersBlocks.FLOO_FIRE) {
				var serverState = OllivandersServerState.getServerState(playerEntity.getServer());
				var flooState = serverState.getFlooState();
				var buf = PacketByteBufs.create();
				var name = flooState.getFlooNameByPos(pos);
				buf.writeString(name.orElse(""));
				buf.writeBlockPos(pos);
				var storage = flooState.getFlooPosByName(name.orElse(""));
				buf.writeInt((storage.isPresent() ? storage.get().getDirection() : playerEntity.getHorizontalFacing().getOpposite()).getId());
				buf.writeBoolean(storage.isEmpty() || storage.get().isChosenRandomly());
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					Ollivanders.LOGGER.info("Removing fire from floo network at position " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ".");
				}
				flooState.removeFlooByPos(pos);
				ServerPlayNetworking.send((ServerPlayerEntity) playerEntity, OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN, buf);
				return ActionResult.SUCCESS;
			}
			return ActionResult.FAIL;
		}
		return ActionResult.FAIL;
	}
}
