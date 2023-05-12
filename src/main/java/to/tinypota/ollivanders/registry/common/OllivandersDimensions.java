package to.tinypota.ollivanders.registry.common;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class OllivandersDimensions {
    public static final Map<RegistryKey<DimensionOptions>, Supplier<DimensionOptions>> DIMENSIONS = new HashMap<>();

    public static void init() {

    }
}
