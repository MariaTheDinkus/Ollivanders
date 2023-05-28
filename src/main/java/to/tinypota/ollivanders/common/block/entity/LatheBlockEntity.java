package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.ChildFlooFireBlock;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.WandCraftHelper;
import to.tinypota.ollivanders.registry.client.OllivandersParticleTypes;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class LatheBlockEntity extends BlockEntity {
	private ItemStack stack = ItemStack.EMPTY;
	private static final int MAX_PROGRESS = 80;
	private int progress = 0;
	
	public LatheBlockEntity(BlockPos pos, BlockState state) {
		super(OllivandersBlockEntityTypes.LATHE, pos, state);
	}
	
	public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof LatheBlockEntity fire) {
			fire.tick();
		}
	}
	
	private void tick() {
		if (!stack.isEmpty() && WandCraftHelper.canCraft(stack.getItem())) {
			if (progress < MAX_PROGRESS) {
				progress++;
				if (!world.isClient()) {
					((ServerWorld) world).spawnParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 10, 0.25, 0, 0.25, 0);
				}
			} else {
				if (!world.isClient()) {
					setStack(new ItemStack(WandCraftHelper.getCraft(stack.getItem())));
				}
				progress = 0;
			}
		} else {
			progress = 0;
		}
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
		if (!world.isClient())
			markDirty();
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		if(!world.isClient()) {
			var compound = new NbtCompound();
			writeNbt(compound);
			sendPacket((ServerWorld) world, compound);
		}
	}
	
	protected void sendPacket(ServerWorld w, NbtCompound tag) {
		tag.putString("id", BlockEntityType.getId(getType()).toString());
		sendPacket( w, BlockEntityUpdateS2CPacket.create( this, blockEntity -> tag ) );
	}
	
	private void sendPacket(ServerWorld world, BlockEntityUpdateS2CPacket packet) {
		world.getPlayers( player -> player.squaredDistanceTo( Vec3d.of( getPos() ) ) < 40 * 40 ).forEach( player -> player.networkHandler.sendPacket( packet ) );
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.put("stack", stack.writeNbt(new NbtCompound()));
		
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		stack = ItemStack.fromNbt(nbt.getCompound("stack"));
		
		super.readNbt(nbt);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		var compound = new NbtCompound();
		writeNbt(compound);
		return compound;
	}
}
