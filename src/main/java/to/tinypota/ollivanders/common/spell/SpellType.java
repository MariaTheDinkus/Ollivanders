package to.tinypota.ollivanders.common.spell;

import net.minecraft.util.StringIdentifiable;

public enum SpellType implements StringIdentifiable {
	RAYCAST("raycast"),
	PROJECTILE("projectile"),
	SELF("self"),
	STATIONARY("stationary");
	
	private final String name;
	
	SpellType(String name) {
		this.name = name;
	}
	
	@Override
	public String asString() {
		return name;
	}
}
