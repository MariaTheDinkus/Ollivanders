package to.tinypota.ollivanders.common.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;

public class ClearEffectSpell extends Spell {
    private final StatusEffect statusEffect;

    public ClearEffectSpell(String castName, StatusEffect statusEffect, Settings settings) {
        super(castName, settings);
        this.statusEffect = statusEffect;
    }

    @Override
    public ActionResult onHitBlock(World world, BlockHitResult hitResult) {
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult onHitEntity(World world, EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity) {
            return ((LivingEntity) hitResult.getEntity()).removeStatusEffect(statusEffect) ? ActionResult.SUCCESS : ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult onSelfCast(LivingEntity entity) {
        return entity.removeStatusEffect(statusEffect) ? ActionResult.SUCCESS : ActionResult.FAIL;
    }
}
