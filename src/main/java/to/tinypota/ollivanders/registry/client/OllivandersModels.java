package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.client.util.ModelIdentifier;
import to.tinypota.ollivanders.Ollivanders;

public class OllivandersModels {
	public static void init() {
		ModelLoadingPlugin.register(pluginContext -> {
			pluginContext.addModels(new ModelIdentifier(Ollivanders.id("pestle" ),""));
		});
	}
}
