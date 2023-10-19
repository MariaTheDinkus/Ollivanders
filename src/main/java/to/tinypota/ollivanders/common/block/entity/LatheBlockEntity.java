package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import to.tinypota.ollivanders.common.block.LatheBlock;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeTypes;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//TODO: Make implement inventory
public class LatheBlockEntity extends BlockEntity {
	private ItemStack stack = ItemStack.EMPTY;
	private int progress = 0;
	private int prevProgress = 0;
	private final RecipeManager.MatchGetter<Inventory, ? extends AbstractCookingRecipe> matchGetter;
	private boolean hasPlayer = false;
	
	public LatheBlockEntity(BlockPos pos, BlockState state) {
		super(OllivandersBlockEntityTypes.LATHE, pos, state);
		matchGetter = RecipeManager.createCachedMatchGetter(OllivandersRecipeTypes.LATHE);
	}
	
	public int getProgress() {
		return progress;
	}
	
	public int getPrevProgress() {
		return prevProgress;
	}
	
	public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof LatheBlockEntity fire) {
			fire.tick();
		}
	}
	
	private void tick() {
		AtomicReference<ItemStack> craftedStack = new AtomicReference<>(ItemStack.EMPTY);
		var inventory = new SimpleInventory(stack);
		var match = matchGetter.getFirstMatch(inventory, world);
		AtomicBoolean hasPlayer = new AtomicBoolean(false);
		var entities = world.getEntitiesByClass(PlayerEntity.class, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), player -> {
			hasPlayer.set(player.getHorizontalFacing() == world.getBlockState(pos).get(LatheBlock.FACING));
			return true;
		});
		if (hasPlayer.get() && !stack.isEmpty() && match.isPresent()) {
			if (progress < match.get().value().getCookingTime() - 1) {
				prevProgress = progress;
				progress++;
			} else {
				if (!world.isClient()) {
					match.ifPresent(recipe -> craftedStack.set(recipe.value().craft(inventory, world.getRegistryManager())));
					int i = MathHelper.floor(match.get().value().getExperience());
					float f = MathHelper.fractionalPart(match.get().value().getExperience());
					if (f != 0.0f && Math.random() < f) {
						++i;
					}
					ExperienceOrbEntity.spawn((ServerWorld) world, pos.toCenterPos(), i);
					((ServerWorld) world).spawnParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 9.5F / 16F, pos.getZ() + 0.5, 30, 0.25, 0.25, 0.25, 0);
					if (!craftedStack.get().isEmpty())
						setStack(craftedStack.get());
				}
				progress = 0;
				prevProgress = 0;
			}
		} else {
			progress = 0;
			prevProgress = 0;
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
