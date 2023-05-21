package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;

public class OllivandersEntityTypes {
	public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE = register("spell_projectile", FabricEntityTypeBuilder
			.create()
			.<SpellProjectileEntity>entityFactory(SpellProjectileEntity::new)
			.dimensions(EntityDimensions.changing(0.1F, 0.1F))
			.fireImmune()
			.spawnableFarFromPlayer()
			.trackRangeChunks(4)
			.trackedUpdateRate(20)
			.build()
	);
	
	public static void init() {
	
	}
	
	public static <E extends EntityType<?>> E register(String name, E entityType) {
		return Registry.register(Registries.ENTITY_TYPE, Ollivanders.id(name), entityType);
	}
}
