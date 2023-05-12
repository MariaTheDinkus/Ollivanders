package to.tinypota.ollivandered;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import to.tinypota.ollivandered.registry.common.*;

public class Ollivandered implements ModInitializer {
    public static final String ID = "ollivandered";

    public static Identifier id(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        OllivanderedItemGroups.init();
        OllivanderedItems.init();
        OllivanderedBlocks.init();
        OllivanderedBlockEntityTypes.init();
        OllivanderedEntityTypes.init();
        OllivanderedEvents.init();
        OllivanderedTrackedData.init();
        OllivanderedCommands.init();
        OllivanderedDimensions.init();
        OllivanderedEntityAttributes.init();
        OllivanderedStatusEffects.init();
    }
}
