package to.tinypota.ollivanders.common.spell;

import net.minecraft.util.StringIdentifiable;

public enum PowerLevel implements StringIdentifiable {
	NORMAL("normal", 1),
	MEDIUM("medium", 2),
	HIGH("high", 3),
	HIGHER("higher", 4),
	MAXIMUM("maximum", 5);
	
	String name;
	int numerical;
	
	PowerLevel(String name, int numerical) {
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
