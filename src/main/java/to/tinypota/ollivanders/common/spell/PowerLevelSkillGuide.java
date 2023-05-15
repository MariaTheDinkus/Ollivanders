package to.tinypota.ollivanders.common.spell;

public class PowerLevelSkillGuide {
	public static final PowerLevelSkillGuide EMPTY = new PowerLevelSkillGuide(0, 0, 0, 0);
	private final double mediumLimit, highLimit, higherLimit, maximumLimit;
	
	public PowerLevelSkillGuide(double mediumLimit, double highLimit, double higherLimit, double maximumLimit) {
		this.mediumLimit = mediumLimit;
		this.highLimit = highLimit;
		this.higherLimit = higherLimit;
		this.maximumLimit = maximumLimit;
	}
	
	public PowerLevel availableForSkill(double skillLevel) {
		if (skillLevel < mediumLimit) {
			return PowerLevel.NORMAL;
		} else if (skillLevel >= mediumLimit && skillLevel < highLimit) {
			return PowerLevel.MEDIUM;
		} else if (skillLevel >= highLimit && skillLevel < higherLimit) {
			return PowerLevel.HIGH;
		} else if (skillLevel >= higherLimit && skillLevel < maximumLimit) {
			return PowerLevel.HIGHER;
		} else if (skillLevel >= maximumLimit) {
			return PowerLevel.MAXIMUM;
		}
		return PowerLevel.NORMAL;
	}
}
