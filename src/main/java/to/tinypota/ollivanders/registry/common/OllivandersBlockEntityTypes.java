package to.tinypota.ollivanders.registry.common;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;

public class OllivandersBlockEntityTypes {
	public static void init() {
	
	}
	
	public static <B extends BlockEntityType<?>> B register(String name, B blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, Ollivanders.id(name), blockEntityType);
	}
}
