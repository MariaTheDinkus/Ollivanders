package to.tinypota.ollivanders.api.spell;

import net.minecraft.util.StringIdentifiable;

public enum SpellPowerLevel implements StringIdentifiable {
	NORMAL("normal", 1),
	MEDIUM("medium", 2),
	HIGH("high", 3),
	HIGHER("higher", 4),
	MAXIMUM("maximum", 5);
	
	String name;
	int numerical;
	
	SpellPowerLevel(String name, int numerical) {
		this.name = name;
		this.numerical = numerical;
	}
	
	public int getNumerical() {
		return numerical;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String asString() {
		return name;
	}
}
