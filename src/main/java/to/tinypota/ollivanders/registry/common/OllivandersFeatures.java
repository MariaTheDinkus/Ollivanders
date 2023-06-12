package to.tinypota.ollivanders.registry.common;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.world.feature.RandomCoreFeature;

public class OllivandersFeatures {
	public static final RegistryKey<ConfiguredFeature<?, ?>> LAUREL_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Ollivanders.id("laurel"));
	public static final RegistryKey<ConfiguredFeature<?, ?>> REDWOOD_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Ollivanders.id("redwood"));
	public static final RegistryKey<Biome> LAUREL_FOREST = RegistryKey.of(RegistryKeys.BIOME, Ollivanders.id("laurel_forest"));
	public static final RegistryKey<Biome> REDWOOD_FOREST = RegistryKey.of(RegistryKeys.BIOME, Ollivanders.id("redwood_forest"));
	
	public static final RandomCoreFeature PHOENIX_FEATHER = register("phoenix_feather", new RandomCoreFeature(OllivandersBlocks.PHOENIX_FEATHER_BLOCK, DefaultFeatureConfig.CODEC));
	public static final RandomCoreFeature THESTRAL_TAIL_HAIR = register("thestral_tail_hair", new RandomCoreFeature(OllivandersBlocks.THESTRAL_TAIL_HAIR_BLOCK, DefaultFeatureConfig.CODEC));
	public static final RandomCoreFeature UNICORN_TAIL_HAIR = register("unicorn_tail_hair", new RandomCoreFeature(OllivandersBlocks.UNICORN_TAIL_HAIR_BLOCK, DefaultFeatureConfig.CODEC));
	
	public static void init() {
		// Only run below code when a fabric-api.datagen property is not set, so that the datagen does not fail.
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && System.getProperty("fabric-api.datagen") != null)
			return;
		
		BiomePlacement.replaceOverworld(BiomeKeys.FOREST, RegistryKey.of(RegistryKeys.BIOME, Ollivanders.id("redwood_forest")), 0.35);
		BiomePlacement.replaceOverworld(BiomeKeys.BIRCH_FOREST, RegistryKey.of(RegistryKeys.BIOME, Ollivanders.id("laurel_forest")), 0.35);
		
		BiomeModifications.addFeature(biomeSelectionContext -> BiomeSelectors.foundInOverworld().test(biomeSelectionContext), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, Ollivanders.id("phoenix_feather")));
		BiomeModifications.addFeature(biomeSelectionContext -> biomeSelectionContext.hasTag(BiomeTags.IS_FOREST), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, Ollivanders.id("thestral_tail_hair")));
		BiomeModifications.addFeature(biomeSelectionContext -> biomeSelectionContext.hasTag(BiomeTags.IS_FOREST), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, Ollivanders.id("unicorn_tail_hair")));
	}
	
	public static <F extends Feature<?>> F register(String name, F feature) {
		return Registry.register(Registries.FEATURE, Ollivanders.id(name), feature);
	}
}
