package to.tinypota.ollivanders.api.spell;

public class SpellPowerEvaluator {
	public static final SpellPowerEvaluator EMPTY = new SpellPowerEvaluator(0, 0, 0, 0);
	private final double mediumLimit, highLimit, higherLimit, maximumLimit;
	
	public SpellPowerEvaluator(double mediumLimit, double highLimit, double higherLimit, double maximumLimit) {
		this.mediumLimit = mediumLimit;
		this.highLimit = highLimit;
		this.higherLimit = higherLimit;
		this.maximumLimit = maximumLimit;
	}
	
	public SpellPowerLevel check(double skillLevel) {
		if (skillLevel < mediumLimit) {
			return SpellPowerLevel.NORMAL;
		} else if (skillLevel >= mediumLimit && skillLevel < highLimit) {
			return SpellPowerLevel.MEDIUM;
		} else if (skillLevel >= highLimit && skillLevel < higherLimit) {
			return SpellPowerLevel.HIGH;
		} else if (skillLevel >= higherLimit && skillLevel < maximumLimit) {
			return SpellPowerLevel.HIGHER;
		} else if (skillLevel >= maximumLimit) {
			return SpellPowerLevel.MAXIMUM;
		}
		return SpellPowerLevel.NORMAL;
	}
}
