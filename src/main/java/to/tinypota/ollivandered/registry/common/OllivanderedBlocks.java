package to.tinypota.ollivandered.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivandered.Ollivandered;

public class OllivanderedBlocks {
    public static final Block TEST_BLOCK = register("test_block", new Block(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK)), new Item.Settings());

    public static void init() {

    }

    public static <B extends Block> B register(String name, B block) {
        return Registry.register(Registries.BLOCK, Ollivandered.id(name), block);
    }

    public static <B extends Block> B register(String name, B block, Item.Settings itemSettings) {
        BlockItem blockItem = new BlockItem(block, itemSettings);
        Registry.register(Registries.BLOCK, Ollivandered.id(name), block);
        Registry.register(Registries.ITEM, Ollivandered.id(name), blockItem);
        OllivanderedItemGroups.addToDefault(blockItem);
        return block;
    }

    private static PistonBlock createPistonBlock(boolean sticky) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> !state.get(PistonBlock.EXTENDED);
        return new PistonBlock(sticky, AbstractBlock.Settings.copy(Blocks.PISTON).strength(1.5f).solidBlock(($, $$, $$$) -> false).suffocates(contextPredicate).blockVision(contextPredicate));
    }
}
