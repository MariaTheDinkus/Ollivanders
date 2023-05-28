package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.common.OllivandersFeatures;

public class OllivandersDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		final var pack = dataGenerator.createPack();
		
		pack.addProvider(OllivandersLangGenerator::new);
		pack.addProvider(OllivandersModelGenerator::new);
		pack.addProvider(OllivandersBlockTagGenerator::new);
		pack.addProvider(OllivandersItemTagGenerator::new);
		pack.addProvider(OllivandersRecipeGenerator::new);
		pack.addProvider(OllivandersLootTableGenerator::new);
	}
	
	@Override
	public @Nullable String getEffectiveModId() {
		return Ollivanders.ID;
	}
}