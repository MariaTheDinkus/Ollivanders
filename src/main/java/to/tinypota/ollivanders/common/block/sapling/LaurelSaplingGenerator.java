/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 */
package to.tinypota.ollivanders.common.block.sapling;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import to.tinypota.ollivanders.registry.common.OllivandersFeatures;

public class LaurelSaplingGenerator
extends SaplingGenerator {
    @Override
    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return OllivandersFeatures.LAUREL_TREE;
    }
}

