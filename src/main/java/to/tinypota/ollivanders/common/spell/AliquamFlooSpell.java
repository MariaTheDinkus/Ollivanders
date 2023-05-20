package to.tinypota.ollivanders.common.spell;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.registry.client.OllivandersNetworking;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class AliquamFlooSpell extends Spell {
	public AliquamFlooSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, Entity caster) {
		var pos = hitResult.getBlockPos();
		var state = world.getBlockState(pos);
		if (caster instanceof ServerPlayerEntity && state.getBlock() == Blocks.FIRE && world.getBlockState(pos.up()).getBlock() instanceof AbstractSignBlock) {
			var newState = OllivandersBlocks.FLOO_FIRE.getDefaultState();
			world.setBlockState(pos, newState);
			
			var buf = PacketByteBufs.create();
			buf.writeBlockPos(pos);
			ServerPlayNetworking.send((ServerPlayerEntity) caster, OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN, buf);
			return ActionResult.FAIL;
		}
		return ActionResult.FAIL;
	}
}
