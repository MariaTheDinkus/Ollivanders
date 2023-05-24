package to.tinypota.ollivanders.api.wand;

import net.minecraft.util.StringIdentifiable;

public enum WandMatchLevel implements StringIdentifiable {
	PERFECT("perfect", 0.8, 1.0),
	AVERAGE("average", 0.6, 0.75),
	AWFUL("awful", 0.4, 0.5);
	
	String name;
	double defaultCastPercentage;
	double extraSkillGainPercentage;
	
	WandMatchLevel(String name, double defaultCastPercentage, double extraSkillGainPercentage) {
		this.name = name;
		this.defaultCastPercentage = defaultCastPercentage;
		this.extraSkillGainPercentage = extraSkillGainPercentage;
	}
	
	public double getDefaultCastPercentage() {
		return defaultCastPercentage;
	}
	
	public double getExtraSkillGainPercentage() {
		return extraSkillGainPercentage;
	}
	
	@Override
	public String asString() {
		return name;
	}
}
