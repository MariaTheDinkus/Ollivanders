package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.entity.FlooFireBlockEntity;

public class OllivandersBlockEntityTypes {
	public static final BlockEntityType<FlooFireBlockEntity> FLOO_FIRE = register("floo_fire", FabricBlockEntityTypeBuilder.create(FlooFireBlockEntity::new, OllivandersBlocks.FLOO_FIRE).build());
	
	public static void init() {
	
	}
	
	public static <B extends BlockEntityType<?>> B register(String name, B blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Ollivanders.id(name), blockEntityType);
	}
}
