package to.tinypota.ollivanders.registry.common;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.spell.*;

public class OllivandersSpells {
    public static final Spell LUMOS = register("lumos", new EffectSpell("lumos", new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * 30), new Spell.Settings().type(SpellType.SELF)));
    public static final Spell NOX = register("nox", new ClearEffectSpell("nox", StatusEffects.NIGHT_VISION, new Spell.Settings().type(SpellType.SELF)));
    //public static final Spell RAY = register("ray", new TestRaycastSpell("ray", new Spell.Settings().type(SpellType.RAYCAST)));

    public static void init() {

    }

    public static Spell getCurrentSpell(PlayerEntity player) {
        return OllivandersRegistries.getSpellByName(player.getDataTracker().get(OllivandersTrackedData.CURRENT_SPELL));
    }

    public static void setCurrentSpell(PlayerEntity player, Spell spell) {
        player.getDataTracker().set(OllivandersTrackedData.CURRENT_SPELL, spell.getCastName());
    }

    public static void setCurrentSpell(PlayerEntity player, String name) {
        player.getDataTracker().set(OllivandersTrackedData.CURRENT_SPELL, name);
    }

    public static void emptyCurrentSpell(PlayerEntity player) {
        player.getDataTracker().set(OllivandersTrackedData.CURRENT_SPELL, Spell.EMPTY.getCastName());
    }

    public static <S extends Spell> S register(String name, S spell) {
        S result = Registry.register(OllivandersRegistries.SPELL, Ollivanders.id(name), spell);
        OllivandersRegistries.addSpellName(spell.getCastName());
        return result;
    }
}
