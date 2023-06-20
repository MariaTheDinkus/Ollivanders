package to.tinypota.ollivanders.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
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
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
import qouteall.q_misc_util.my_util.DQuaternion;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.client.screen.VanishingCabinetGuiDescription;
import to.tinypota.ollivanders.common.block.LatheBlock;
import to.tinypota.ollivanders.common.block.VanishingCabinetBlock;
import to.tinypota.ollivanders.common.item.SplitCabinetCoreItem;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeTypes;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class VanishingCabinetBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, InventoryProvider, ImplementedInventory, SidedInventory {
	public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
	public Portal portal = null;
	
	public VanishingCabinetBlockEntity(BlockPos pos, BlockState state) {
		super(OllivandersBlockEntityTypes.VANISHING_CABINET, pos, state);
	}
	
	public Portal getPortalOrCache() {
		if (portal == null && world != null) {
			var box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
			world.getEntitiesByType(Portal.entityType, box, portal -> true).forEach(newPortal -> portal = newPortal);
		}
		return portal;
	}
	
	public void setPortalVisible(boolean visible) {
		getPortalOrCache().setIsVisible(visible);
		getPortalOrCache().setTeleportable(visible);
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
		if (!world.isClient()) {
			if (world.getTime() % 20 == 0) {
				var serverState = OllivandersServerState.getServerState(world.getServer());
				var vanishingCabinetState = serverState.getVanishingCabinetState();
				var vanishingCabinetStorage = vanishingCabinetState.getVanishingCabinet(pos);
				if (vanishingCabinetStorage.isPresent()) {
					if (!getStack(0).isEmpty()) {
						var stack = getStack(0);
						if (stack.getItem() instanceof SplitCabinetCoreItem) {
							var nbt = stack.getOrCreateNbt();
							if (nbt.contains("core_uuid", NbtElement.INT_ARRAY_TYPE) && vanishingCabinetState.getUuid(pos).isEmpty()) {
								var uuid = nbt.getUuid("core_uuid");
								vanishingCabinetState.setUuid(pos, uuid.toString());
							}
							
							var matchingCabinetPos = vanishingCabinetState.findMatchingCabinet(pos);
							if (portal != null) {
								if (matchingCabinetPos != null) {
									var matchingCabinet = vanishingCabinetState.getVanishingCabinet(matchingCabinetPos).get();
									var storage = vanishingCabinetStorage.get();
									var matchingCabinetVec = new Vec3d(matchingCabinetPos.getX() + 0.5, matchingCabinetPos.getY() + 1, matchingCabinetPos.getZ() + 0.5);
									portal.setDestination(matchingCabinetVec);
									portal.setDestinationDimension(RegistryKey.of(RegistryKeys.WORLD, matchingCabinet.getDimension()));
									float rotation = 0;
									if (storage.getDirection() == matchingCabinet.getDirection()) {
										rotation = 180;
									} else if (storage.getDirection() == matchingCabinet.getDirection().getOpposite()) {
										rotation = 0;
									} else if (storage.getDirection() == matchingCabinet.getDirection().rotateYClockwise()) {
										rotation = -90;
									} else if (storage.getDirection() == matchingCabinet.getDirection().rotateYCounterclockwise()) {
										rotation = 90;
									}
									portal.setRotation(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), rotation));
									setPortalVisible(true);
									markDirty();
								} else if (isPortalVisible()) {
									setPortalVisible(false);
									markDirty();
								}
							}
						}
					} else if (!vanishingCabinetState.getUuid(pos).isEmpty()) {
						vanishingCabinetState.setUuid(pos, "");
						setPortalVisible(false);
						markDirty();
					}
				}
			}
		}
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
		tag.putString("packet", "");
		sendPacket( w, BlockEntityUpdateS2CPacket.create( this, blockEntity -> tag ) );
	}
	
	private void sendPacket(ServerWorld world, BlockEntityUpdateS2CPacket packet) {
		world.getPlayers().forEach( player -> player.networkHandler.sendPacket( packet ) );
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.put("item", inventory.get(0).writeNbt(new NbtCompound()));
		if (world != null && (portal = getPortalOrCache()) != null) {
			nbt.put("portal", portal.writePortalDataToNbt());
		}
		super.writeNbt(nbt);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		inventory.set(0, ItemStack.fromNbt(nbt.getCompound("item")));
		if (world != null && (portal = getPortalOrCache()) != null) {
			portal.updatePortalFromNbt(nbt.getCompound("portal"));
		}
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
