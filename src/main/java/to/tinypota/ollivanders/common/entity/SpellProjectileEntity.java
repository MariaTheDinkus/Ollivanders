package to.tinypota.ollivanders.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import to.tinypota.ollivanders.common.spell.PowerLevel;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.registry.common.OllivandersEntityTypes;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

public class SpellProjectileEntity extends PersistentProjectileEntity {
	private Spell spell = Spell.EMPTY;
	
	public SpellProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public SpellProjectileEntity(Spell spell, LivingEntity owner, World world) {
		super(OllivandersEntityTypes.SPELL_PROJECTILE, owner, world);
		this.spell = spell;
		setVelocity(owner, owner.getPitch(), owner.getYaw(), 0.0F, 1F, 0F);
	}
	
	public boolean hasSpell() {
		return spell != Spell.EMPTY;
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
	public boolean isNoClip() {
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		// This is a recreation of when colliding methods are called in the parent class. This is required for my entity to be no-clip, but still collide. This is so a spell can return ActionResult.PASS and pass through blocks.
		Vec3d posWithVelocity;
		var velocityPos = getVelocity();
		var pos = getPos();
		posWithVelocity = pos.add(velocityPos);
		HitResult hitResult = getWorld().raycast(new RaycastContext(pos, posWithVelocity, RaycastContext.ShapeType.COLLIDER, spell.shouldHitWater() ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, this));
		if (hitResult.getType() != HitResult.Type.MISS) {
			posWithVelocity = hitResult.getPos();
		}
		while (!isRemoved()) {
			var entityHitResult = getEntityCollision(pos, posWithVelocity);
			if (entityHitResult != null) {
				hitResult = entityHitResult;
			}
			if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
				var hitEntity = ((EntityHitResult) hitResult).getEntity();
				var owner = getOwner();
				if (hitEntity instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).shouldDamagePlayer((PlayerEntity) hitEntity)) {
					hitResult = null;
					entityHitResult = null;
				}
			}
			if (hitResult != null) {
				onCollision(hitResult);
				velocityDirty = true;
			}
			if (entityHitResult == null || getPierceLevel() <= 0) {
				break;
			}
			hitResult = null;
		}
		
		if (!getWorld().isClient()) {
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
		var world = getWorld();
		
		if (!world.isClient()) {
			var result = spell.onHitBlock(PowerLevel.NORMAL, world, blockHitResult, this);
			if (result == ActionResult.SUCCESS || result == ActionResult.FAIL) {
				discard();
			}
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		var world = getWorld();
		
		if (!world.isClient()) {
			var result = spell.onHitEntity(PowerLevel.NORMAL, world, entityHitResult, this);
			if (result == ActionResult.SUCCESS || result == ActionResult.FAIL) {
				discard();
			}
		}
	}
	
	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (spell != null) {
			nbt.putString("spell", OllivandersRegistries.SPELL.getId(spell).toString());
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("spell", NbtElement.STRING_TYPE)) {
			spell = OllivandersRegistries.SPELL.get(new Identifier(nbt.getString("spell")));
		}
	}
}
