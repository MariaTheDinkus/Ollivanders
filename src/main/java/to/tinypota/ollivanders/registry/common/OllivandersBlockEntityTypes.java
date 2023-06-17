package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.entity.*;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;

public class OllivandersBlockEntityTypes {
	public static final BlockEntityType<FlooFireBlockEntity> FLOO_FIRE = register("floo_fire", FabricBlockEntityTypeBuilder.create(FlooFireBlockEntity::new, OllivandersBlocks.FLOO_FIRE).build());
	public static final BlockEntityType<CoreBlockEntity> CORE = register("core", FabricBlockEntityTypeBuilder.create(CoreBlockEntity::new, OllivandersBlocks.PHOENIX_FEATHER_BLOCK, OllivandersBlocks.THESTRAL_TAIL_HAIR_BLOCK, OllivandersBlocks.UNICORN_TAIL_HAIR_BLOCK).build());
	public static final BlockEntityType<LatheBlockEntity> LATHE = register("lathe", FabricBlockEntityTypeBuilder.create(LatheBlockEntity::new, OllivandersBlocks.LATHE).build());
	public static final BlockEntityType<VanishingCabinetBlockEntity> VANISHING_CABINET = register("vanishing_cabinet", FabricBlockEntityTypeBuilder.create(VanishingCabinetBlockEntity::new, OllivandersBlocks.VANISHING_CABINET).build());
	public static BlockEntityType<SignBlockEntity> SIGN;
	public static BlockEntityType<OllivandersHangingSignBlockEntity> HANGING_SIGN;
	
	public static void init() {
		var signBuilder = FabricBlockEntityTypeBuilder.create(SignBlockEntity::new);
		var hangingSignBuilder = FabricBlockEntityTypeBuilder.create(OllivandersHangingSignBlockEntity::new);
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			signBuilder.addBlocks(storage.getSign(), storage.getWallSign());
			hangingSignBuilder.addBlocks(storage.getHangingSign(), storage.getWallHangingSign());
		}
		
		SIGN = register("sign", signBuilder.build());
		HANGING_SIGN = register("hanging_sign", hangingSignBuilder.build());
	}
	
	public static <B extends BlockEntityType<?>> B register(String name, B blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Ollivanders.id(name), blockEntityType);
	}
}
