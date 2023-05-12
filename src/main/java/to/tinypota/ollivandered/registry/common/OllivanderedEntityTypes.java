package to.tinypota.ollivandered.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivandered.Ollivandered;
import to.tinypota.ollivandered.common.entity.SpellProjectileEntity;

public class OllivanderedEntityTypes {
    public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE = register("slime_bullet", FabricEntityTypeBuilder
            .create()
            .<SpellProjectileEntity>entityFactory(SpellProjectileEntity::new)
            .dimensions(EntityDimensions.changing(0.5F, 0.5F))
            .fireImmune()
            .spawnableFarFromPlayer()
            .trackRangeBlocks(32)
            .trackedUpdateRate(10)
            .build()
    );

    public static void init() {

    }

    public static <E extends EntityType<?>> E register(String name, E entityType) {
        return Registry.register(Registries.ENTITY_TYPE, Ollivandered.id(name), entityType);
    }
}
