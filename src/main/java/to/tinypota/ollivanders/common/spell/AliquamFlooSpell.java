package to.tinypota.ollivanders.common.spell;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

public class AliquamFlooSpell extends Spell {
	public AliquamFlooSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, Entity caster) {
		var pos = hitResult.getBlockPos();
		var state = world.getBlockState(pos);
		if (caster instanceof ServerPlayerEntity) {
			if (state.getBlock() == Blocks.FIRE) {
				var newState = OllivandersBlocks.FLOO_FIRE.getDefaultState();
				world.setBlockState(pos, newState);
				
				var buf = PacketByteBufs.create();
				buf.writeString("");
				buf.writeBlockPos(pos);
				buf.writeInt(caster.getHorizontalFacing().getOpposite().getId());
				ServerPlayNetworking.send((ServerPlayerEntity) caster, OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN, buf);
				return ActionResult.SUCCESS;
			}
			
			if (state.getBlock() instanceof FlooFireBlock) {
				var serverState = OllivandersServerState.getServerState(caster.getServer());
				var buf = PacketByteBufs.create();
				var name = serverState.getFlooNameByPos(pos);
				buf.writeString(name != null ? name : "");
				buf.writeBlockPos(pos);
				buf.writeInt(serverState.getFlooPosByName(name).getRight().getId());
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					Ollivanders.LOGGER.info("Removing fire from floo network at position " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ".");
				}
				serverState.removeFlooByPos(pos);
				ServerPlayNetworking.send((ServerPlayerEntity) caster, OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN, buf);
				return ActionResult.SUCCESS;
			}
			return ActionResult.FAIL;
		}
		return ActionResult.FAIL;
	}
}
