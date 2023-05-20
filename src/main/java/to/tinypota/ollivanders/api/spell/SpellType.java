package to.tinypota.ollivanders.api.spell;

import net.minecraft.util.StringIdentifiable;

public enum SpellType implements StringIdentifiable {
	/*
	 * The different spell types. Raycast spells do *not* have access to block entities for some reason.
	 * TODO: Hopefully figure out why raycast spells can't get block entities.
	 */
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
