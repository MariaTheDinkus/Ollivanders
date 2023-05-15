package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;

public class OllivandersDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		final var pack = dataGenerator.createPack();
		
		pack.addProvider(OllivandersLangGenerator::new);
		pack.addProvider(OllivandersModelGenerator::new);
	}
	
	@Override
	public @Nullable String getEffectiveModId() {
		return Ollivanders.ID;
	}
}