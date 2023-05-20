package to.tinypota.ollivanders.api.spell;

import net.minecraft.util.StringIdentifiable;

public enum SpellType implements StringIdentifiable {
	RAYCAST("raycast"),
	PROJECTILE("projectile"),
	SELF("self");
	
	private final String name;
	
	SpellType(String name) {
		this.name = name;
	}
	
	@Override
	public String asString() {
		return name;
	}
}
