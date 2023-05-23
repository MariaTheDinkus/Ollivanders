package to.tinypota.ollivanders.api.spell;

import java.util.EnumMap;
import java.util.Map;

public class SpellPowerStorage<T> {
	private final Map<SpellPowerLevel, T> powerLevelMap;
	
	public SpellPowerStorage(T normalValue, T mediumValue, T highValue, T higherValue, T maximumValue) {
		powerLevelMap = new EnumMap<>(SpellPowerLevel.class);
		powerLevelMap.put(SpellPowerLevel.NORMAL, normalValue);
		powerLevelMap.put(SpellPowerLevel.MEDIUM, mediumValue);
		powerLevelMap.put(SpellPowerLevel.HIGH, highValue);
		powerLevelMap.put(SpellPowerLevel.HIGHER, higherValue);
		powerLevelMap.put(SpellPowerLevel.MAXIMUM, maximumValue);
	}
	
	public T getValue(SpellPowerLevel powerLevel) {
		return powerLevelMap.get(powerLevel);
	}
}
