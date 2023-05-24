package to.tinypota.ollivanders.common.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.api.spell.SpellType;
import to.tinypota.ollivanders.api.wand.WandMatchLevel;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.registry.common.OllivandersSpells;

import java.util.Objects;

public class Spell {
	public static final Spell EMPTY = OllivandersSpells.register("empty", new Spell("empty", new Spell.Settings().type(SpellType.SELF)));
	private final String castName;
	private final SpellType type;
	private final boolean hitsWater;
	private final boolean customCastPercents;
	
	public Spell(String castName, Settings settings) {
		this.castName = castName;
		type = settings.type;
		hitsWater = settings.hitsWater;
		customCastPercents = settings.customCastPercents;
	}
	
	public boolean hasCustomCastPercents() {
		return customCastPercents;
	}
	
	/*
	 * A method which you can override in order to give custom cast subtraction percentages for each power level. Useful for spells with less than the 5 power levels available.
	 */
	public double getCustomCastPercents(SpellPowerLevel powerLevel) {
		return 1;
	}
	
	public boolean isEmpty() {
		return this == EMPTY;
	}
	
	public SpellPowerLevel getMaximumPowerLevel() {
		return SpellPowerLevel.MAXIMUM;
	}
	
	public SpellPowerLevel getAvailablePowerLevel(WandMatchLevel wandMatchLevel, double skillLevel) {
		SpellPowerLevel level = SpellPowerLevel.NORMAL;
		for (SpellPowerLevel powerLevel : SpellPowerLevel.values()) {
			if (powerLevel.getNumerical() <= getMaximumPowerLevel().getNumerical() && (SpellHelper.getCastPercentage(this, wandMatchLevel, powerLevel) + (skillLevel / 100 / 10)) > 0) {
				level = powerLevel;
			}
		}
		
		return level;
	}
	
	/*
	 * Called whenever a block is hit by a raycast or projectile entity. May or may not include water, depending on the spell.
	 * If it is shot with a projectile entity, you may return ActionResult.PASS to have the spell pass through the block.
	 */
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity, SpellProjectileEntity projectileEntity) {
		return onHitBlock(powerLevel, world, hitResult, playerEntity);
	}
	
	/*
	 * Called whenever a block is hit by a raycast or projectile entity. May or may not include water, depending on the spell.
	 * If it is shot with a projectile entity, you may return ActionResult.PASS to have the spell pass through the block.
	 */
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity) {
		return onHitBlock(powerLevel, world, hitResult);
	}
	
	/*
	 * Called whenever a block is hit by a raycast or projectile entity. May or may not include water, depending on the spell.
	 * If it is shot with a projectile entity, you may return ActionResult.PASS to have the spell pass through the block.
	 */
	protected ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult) {
		return ActionResult.PASS;
	}
	
	/*
	 * Called whenever an entity is hit by a raycast or projectile entity.
	 * If it is shot with a projectile entity, you may return ActionResult.PASS to have the spell pass through the entity.
	 */
	public ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult, PlayerEntity playerEntity, SpellProjectileEntity projectileEntity) {
		return onHitEntity(powerLevel, world, hitResult, playerEntity);
	}
	
	/*
	 * Called whenever an entity is hit by a raycast or projectile entity.
	 * If it is shot with a projectile entity, you may return ActionResult.PASS to have the spell pass through the entity.
	 */
	public ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult, PlayerEntity playerEntity) {
		return onHitEntity(powerLevel, world, hitResult);
	}
	
	/*
	 * Called whenever an entity is hit by a raycast or projectile entity.
	 * If it is shot with a projectile entity, you may return ActionResult.PASS to have the spell pass through the entity.
	 */
	protected ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult) {
		return ActionResult.PASS;
	}
	
	public ActionResult onSelfCast(SpellPowerLevel powerLevel, PlayerEntity playerEntity) {
		return ActionResult.PASS;
	}
	
	public String getCastName() {
		return castName;
	}
	
	public SpellType getType() {
		return type;
	}
	
	public boolean shouldHitWater() {
		return hitsWater;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Spell spell = (Spell) o;
		return hitsWater == spell.hitsWater && Objects.equals(castName, spell.castName) && type == spell.type;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(castName, type, hitsWater);
	}
	
	public static class Settings {
		SpellType type;
		boolean hitsWater = false;
		boolean customCastPercents = false;
		
		public Spell.Settings type(SpellType type) {
			this.type = type;
			return this;
		}
		
		public Spell.Settings hitsWater() {
			hitsWater = true;
			return this;
		}
		
		public Spell.Settings hitsWater(boolean hitsWater) {
			this.hitsWater = hitsWater;
			return this;
		}
		
		public Spell.Settings customCastPercents() {
			customCastPercents = true;
			return this;
		}
		
		public Spell.Settings customCastPercents(boolean customCastPercents) {
			this.customCastPercents = customCastPercents;
			return this;
		}
		
		public Spell.Settings of(Settings settings) {
			type = settings.type;
			hitsWater = settings.hitsWater;
			customCastPercents = settings.customCastPercents;
			return this;
		}
	}
}
