package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.client.screen.VanishingCabinetGuiDescription;

public class OllivandersScreenHandlers {
	public static final ScreenHandlerType<VanishingCabinetGuiDescription> VANISHING_CABINET = register("vanishing_cabinet", new ScreenHandlerType<>((syncId, playerInventory) -> new VanishingCabinetGuiDescription(syncId, playerInventory, ScreenHandlerContext.EMPTY), FeatureFlags.VANILLA_FEATURES));
	
	public static void init() {
	
	}
	
	public static <H extends ScreenHandlerType<?>> H register(String name, H handler) {
		H result = Registry.register(Registries.SCREEN_HANDLER, Ollivanders.id(name), handler);
		return result;
	}
}
