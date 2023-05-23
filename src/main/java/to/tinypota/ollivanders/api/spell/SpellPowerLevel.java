package to.tinypota.ollivanders.api.spell;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

public enum SpellPowerLevel implements StringIdentifiable {
	NORMAL("normal", 0),
	MEDIUM("medium", 1),
	HIGH("high", 2),
	HIGHER("higher", 3),
	MAXIMUM("maximum", 4);
	
	String name;
	int id;
	private static final SpellPowerLevel[] VALUES;
	
	SpellPowerLevel(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumerical() {
		return id + 1;
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
