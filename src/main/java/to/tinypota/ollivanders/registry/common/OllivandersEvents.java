package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import to.tinypota.ollivanders.common.storage.OllivandersPlayerState;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;

import java.util.ArrayList;
import java.util.List;

public class OllivandersEvents {
	public static void init() {
//		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//			OllivandersServerState serverState = OllivandersServerState.getServerState(handler.player.getWorld().getServer());
//			OllivandersPlayerState playerState = OllivandersServerState.getPlayerState(handler.player);
//
//			// Sending the packet to the player (look at the networking page for more information)
//			PacketByteBuf data = PacketByteBufs.create();
//			data.writeInt(serverState.totalFurnacesCrafted);
//			data.writeInt(playerState.furnacesCrafted);
//			ServerPlayNetworking.send(handler.player, NetworkingMessages.CRAFTED_FURNACES, data);
//		});
	}
	
	public static List<BlockPos> getBlocksInBoundingBox(Entity entity) {
		var blockPositions = new ArrayList<BlockPos>();
		
		var boundingBox = entity.getBoundingBox();
		var minX = (int) Math.floor(boundingBox.minX);
		var maxX = (int) Math.ceil(boundingBox.maxX);
		var minZ = (int) Math.floor(boundingBox.minZ);
		var maxZ = (int) Math.ceil(boundingBox.maxZ);
		
		for (int x = minX; x < maxX; x++) {
			for (int z = minZ; z < maxZ; z++) {
				blockPositions.add(new BlockPos(x, (int) (entity.getY() + 1), z));
			}
		}
		
		return blockPositions;
	}
}
