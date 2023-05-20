package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;

public class OllivandersPlayerState {
	private String suitedWand = "";
	private String suitedCore = "";
	private String currentSpell = "empty";
	private HashMap<Spell, Double> skillLevels = new HashMap<>();
	
	/*
	 * Writes this OllivandersPlayerState to NBT.
	 */
	public NbtCompound writeToNbt(NbtCompound nbt) {
		nbt.putString("suitedWand", suitedWand);
		nbt.putString("suitedCore", suitedCore);
		nbt.putString("currentSpell", currentSpell);
		var skillLevels = new NbtCompound();
		getSkillLevels().forEach((spell, level) -> {
			skillLevels.putDouble(OllivandersRegistries.SPELL.getId(spell).toString(), level);
		});
		nbt.put("skillLevels", skillLevels);
		
		return nbt;
	}
	
	public HashMap<Spell, Double> getSkillLevels() {
		return skillLevels;
	}
	
	public void setSkillLevels(HashMap<Spell, Double> skillLevels) {
		this.skillLevels = skillLevels;
	}
	
	public void addSkillLevel(Spell spell, double amount) {
		var currentLevel = skillLevels.getOrDefault(spell, 0.0);
		skillLevels.put(spell, currentLevel + amount);
	}
	
	public void subtractSkillLevel(Spell spell, double amount) {
		var currentLevel = skillLevels.getOrDefault(spell, 0.0);
		var newLevel = currentLevel - amount;
		if (newLevel < 0) {
			newLevel = 0;
		}
		skillLevels.put(spell, newLevel);
	}
	
	public double getSkillLevel(Spell spell) {
		return skillLevels.getOrDefault(spell, 0.0);
	}
	
	public String getSuitedWand() {
		return suitedWand;
	}
	
	public void setSuitedWand(String suitedWand) {
		this.suitedWand = suitedWand;
	}
	
	String getSuitedCore() {
		return suitedCore;
	}
	
	void setSuitedCore(String suitedCore) {
		this.suitedCore = suitedCore;
	}
	
	String getCurrentSpell() {
		return currentSpell;
	}
	
	void setCurrentSpell(String currentSpell) {
		this.currentSpell = currentSpell;
	}
}