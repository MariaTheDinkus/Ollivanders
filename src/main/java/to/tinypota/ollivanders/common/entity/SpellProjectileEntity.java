package to.tinypota.ollivanders.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.registry.common.OllivandersEntityTypes;

public class SpellProjectileEntity extends PersistentProjectileEntity {

    public SpellProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public SpellProjectileEntity(LivingEntity owner, World world) {
        super(OllivandersEntityTypes.SPELL_PROJECTILE, owner, world);
        setVelocity(owner, owner.getPitch(), owner.getYaw(), 0.0F, 1F, 0F);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected float getDragInWater() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();

        if (!getWorld().isClient() && ) {
            if (age < 10 && getOwner() != null && getOwner().isSneaking()) {
                var owner = getOwner();
                setVelocity(owner, owner.getPitch(), owner.getYaw(), 0.0F, 1F, 0F);
                velocityModified = true;
            } else if (age > 400) {
                discard();
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

		var world = getWorld();

		if (!world.isClient()) {
            //TODO: Do the spell shit here

			discard();
		}
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        var world = getWorld();

        if (!world.isClient()) {
            //TODO: Do the spell shit here

            discard();
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}
