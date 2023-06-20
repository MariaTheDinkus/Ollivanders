package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;

public class OllivandersPlayerState extends State {
	private String suitedWand = "";
	private String suitedCore = "";
	private String currentSpell = "empty";
	private SpellPowerLevel currentSpellPowerLevel = SpellPowerLevel.MAXIMUM;
	private HashMap<Spell, Double> skillLevels = new HashMap<>();
	private SpellPowerLevel powerLevel = SpellPowerLevel.NORMAL;
	
	public OllivandersPlayerState(OllivandersServerState serverState) {
		super(serverState);
	}
	
	/*
	 * Writes this OllivandersPlayerState to NBT.
	 */
	@Override
	public NbtCompound writeToNbt(NbtCompound nbt) {
		nbt.putString("suitedWand", suitedWand);
		nbt.putString("suitedCore", suitedCore);
		nbt.putString("currentSpell", currentSpell);
		nbt.putInt("currentSpellPowerLevel", currentSpellPowerLevel.getId());
		var skillLevels = new NbtCompound();
		this.skillLevels.forEach((spell, level) -> skillLevels.putDouble(OllivandersRegistries.SPELL.getId(spell).toString(), level));
		nbt.put("skillLevels", skillLevels);
		nbt.putInt("powerLevel", powerLevel.getId());
		
		return nbt;
	}
	
	@Override
	public void fromNbt(OllivandersServerState serverState, NbtCompound nbt) {
		suitedWand = nbt.getString("suitedWand");
		suitedCore = nbt.getString("suitedCore");
		currentSpell = nbt.getString("currentSpell");
		currentSpellPowerLevel = SpellPowerLevel.byId(nbt.getInt("currentSpellPowerLevel"));
		var skillLevels = nbt.getCompound("skillLevels");
		getSkillLevels().clear();
		for (var spellId : skillLevels.getKeys()) {
			var identifier = new Identifier(spellId);
			var spell = OllivandersRegistries.SPELL.get(identifier);
			double skillLevel = skillLevels.getDouble(spellId);
			getSkillLevels().put(spell, skillLevel);
		}
		powerLevel = SpellPowerLevel.byId(nbt.getInt("powerLevel"));
	}
	
	public SpellPowerLevel getCurrentSpellPowerLevel() {
		return currentSpellPowerLevel;
	}
	
	public void setCurrentSpellPowerLevel(SpellPowerLevel currentSpellPowerLevel) {
		this.currentSpellPowerLevel = currentSpellPowerLevel;
		if (powerLevel.getNumerical() > currentSpellPowerLevel.getNumerical()) {
			powerLevel = currentSpellPowerLevel;
		}
		markDirty();
	}
	
	public SpellPowerLevel getPowerLevel() {
		return powerLevel;
	}
	
	public void setPowerLevel(SpellPowerLevel powerLevel) {
		this.powerLevel = powerLevel;
		markDirty();
	}
	
	/**
	 * Increases the current power level of the spell.
	 *
	 * This method will increment the power level by one step (e.g., from NORMAL to MEDIUM),
	 * provided that the resulting power level does not exceed the currentSpellPowerLevel.
	 * This is to ensure that the power level of the spell does not go beyond what is currently allowed.
	 *
	 * The method uses the numerical value of the power level for comparison, where each power level
	 * is assigned a numerical value (NORMAL = 1, MEDIUM = 2, etc.).
	 *
	 * @return boolean - Returns true if the power level was successfully increased;
	 *                   returns false if the power level could not be increased because
	 *                   it would exceed the currentSpellPowerLevel.
	 */
	public boolean increasePowerLevel() {
		if (powerLevel.getNumerical() < currentSpellPowerLevel.getNumerical()) {
			powerLevel = SpellPowerLevel.values()[powerLevel.ordinal() + 1];
			markDirty();
			return true;
		}
		return false;
	}
	
	/**
	 * Decreases the current power level of the spell.
	 *
	 * This method will decrement the power level by one step (e.g., from MEDIUM to NORMAL),
	 * provided that the resulting power level does not fall below NORMAL. This is to ensure
	 * that the power level of the spell does not go below the minimum allowed value.
	 *
	 * The method uses the numerical value of the power level for comparison, where each power level
	 * is assigned a numerical value (NORMAL = 1, MEDIUM = 2, etc.).
	 */
	public void decreasePowerLevel() {
		if (powerLevel.getNumerical() > 1) {
			powerLevel = SpellPowerLevel.values()[powerLevel.ordinal() - 1];
			markDirty();
		}
	}
	
	public HashMap<Spell, Double> getSkillLevels() {
		return skillLevels;
	}
	
	public void setSkillLevels(HashMap<Spell, Double> skillLevels) {
		this.skillLevels = skillLevels;
		markDirty();
	}
	
	public void addSkillLevel(Spell spell, double amount) {
		var currentLevel = skillLevels.getOrDefault(spell, 0.0);
		skillLevels.put(spell, currentLevel + amount);
		markDirty();
	}
	
	public void subtractSkillLevel(Spell spell, double amount) {
		var currentLevel = skillLevels.getOrDefault(spell, 0.0);
		var newLevel = currentLevel - amount;
		if (newLevel < 0) {
			newLevel = 0;
		}
		skillLevels.put(spell, newLevel);
		markDirty();
	}
	
	public double getSkillLevel(Spell spell) {
		return skillLevels.getOrDefault(spell, 0.0);
	}
	
	public String getSuitedWand() {
		return suitedWand;
	}
	
	public void setSuitedWand(String suitedWand) {
		this.suitedWand = suitedWand;
		markDirty();
	}
	
	public String getSuitedCore() {
		return suitedCore;
	}
	
	public void setSuitedCore(String suitedCore) {
		this.suitedCore = suitedCore;
		markDirty();
	}
	
	public String getCurrentSpell() {
		return currentSpell;
	}
	
	public void setCurrentSpell(String currentSpell) {
		this.currentSpell = currentSpell;
		markDirty();
	}
}