package to.tinypota.ollivanders.api.spell;

import net.minecraft.util.StringIdentifiable;

public enum SpellPowerLevel implements StringIdentifiable {
	NORMAL("normal", 0, 0),
	MEDIUM("medium", 1, 0.12),
	HIGH("high", 2, 0.27),
	HIGHER("higher", 3, 0.48),
	MAXIMUM("maximum", 4, 0.75);
	
	String name;
	int id;
	double castPercentageSubtractor;
	private static final SpellPowerLevel[] VALUES;
	
	SpellPowerLevel(String name, int id, double castPercentageSubtractor) {
		this.name = name;
		this.id = id;
		this.castPercentageSubtractor = castPercentageSubtractor;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumerical() {
		return id + 1;
	}
	
	public double getCastPercentageSubtractor() {
		return castPercentageSubtractor;
	}
	
	public static SpellPowerLevel byId(int id) {
		switch(id) {
			case 0:
				return NORMAL;
			case 1:
				return MEDIUM;
			case 2:
				return HIGH;
			case 3:
				return HIGHER;
			case 4:
				return MAXIMUM;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String asString() {
		return name;
	}
	
	static {
		VALUES = SpellPowerLevel.values();
	}
}
