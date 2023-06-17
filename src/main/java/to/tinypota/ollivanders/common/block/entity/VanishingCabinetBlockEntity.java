package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.portal.Portal;
import to.tinypota.ollivanders.client.screen.VanishingCabinetGuiDescription;
import to.tinypota.ollivanders.common.block.LatheBlock;
import to.tinypota.ollivanders.common.block.VanishingCabinetBlock;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeTypes;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class VanishingCabinetBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, InventoryProvider, ImplementedInventory, SidedInventory {
	public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
	public Portal portal = null;
	
	public VanishingCabinetBlockEntity(BlockPos pos, BlockState state) {
		super(OllivandersBlockEntityTypes.VANISHING_CABINET, pos, state);
	}
	
	public Portal getPortalOrCache() {
		if (portal == null) {
			var box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
			world.getEntitiesByType(Portal.entityType, box, portal -> true).forEach(newPortal -> portal = newPortal);
		}
		return portal;
	}
	
	public void setPortalVisible(boolean visible) {
		getPortalOrCache().setIsVisible(visible);
	}
	
	public boolean isPortalVisible() {
		return getPortalOrCache().isVisible();
	}
	
	public static <T extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, T t) {
		if (t instanceof VanishingCabinetBlockEntity fire) {
			fire.tick();
		}
	}
	
	private void tick() {
		var portal = getPortalOrCache();
		var open = getCachedState().get(VanishingCabinetBlock.OPEN);
		if (portal != null && open != isPortalVisible()) {
			setPortalVisible(open);
		}
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.put("item", inventory.get(0).writeNbt(new NbtCompound()));
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		inventory.set(0, ItemStack.fromNbt(nbt.getCompound("item")));
		super.readNbt(nbt);
	}
	
	@Override
	public Text getDisplayName() {
		return Text.translatable(getCachedState().getBlock().getTranslationKey());
	}
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
		return new VanishingCabinetGuiDescription(syncId, inventory, ScreenHandlerContext.create(world, pos));
	}
	
	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return this;
	}
	
	@Override
	public int[] getAvailableSlots(Direction var1) {
		// Just return an array of all slots
		int[] result = new int[inventory.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		
		return result;
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return true;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}
}
