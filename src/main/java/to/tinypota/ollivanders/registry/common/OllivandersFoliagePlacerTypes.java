package to.tinypota.ollivanders.registry.common;

import net.minecraft.world.gen.foliage.FoliagePlacerType;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.world.feature.foliageplacer.RedwoodFoliagePlacer;
import to.tinypota.ollivanders.mixin.common.FoliagePlacerTypeInvoker;

public class OllivandersFoliagePlacerTypes {
	public static final FoliagePlacerType<RedwoodFoliagePlacer> REDWOOD_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister(Ollivanders.id("redwood_foliage_placer").toString(), RedwoodFoliagePlacer.CODEC);
	
	public static void init() {
	
	}
}
