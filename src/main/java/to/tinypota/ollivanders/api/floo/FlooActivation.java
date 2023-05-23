package to.tinypota.ollivanders.api.floo;

import net.minecraft.util.StringIdentifiable;

public enum FlooActivation implements StringIdentifiable {
	OFF("off"),
	ACTIVE("active"),
	LEFT("left"),
	ARRIVED("arrived");
	
	String name;
	
	FlooActivation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String asString() {
		return name;
	}
}
