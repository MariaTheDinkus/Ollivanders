package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import to.tinypota.ollivanders.Ollivanders;

public class OllivandersBlocks {
    public static final Block TEST_BLOCK = register("test_block", new Block(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK)), new Item.Settings());
    public static final WoodType LAUREL_WOOD = WoodType.register(new WoodType("laurel", BlockSetType.register(new BlockSetType("laurel"))));
    public static final WoodType REDWOOD = WoodType.register(new WoodType("redwood", BlockSetType.register(new BlockSetType("redwood"))));

    public static void init() {
        registerWood("laurel", LAUREL_WOOD, MapColor.BROWN, MapColor.BROWN);
        registerWood("redwood", REDWOOD, MapColor.BROWN, MapColor.BROWN);
    }

    public static void registerWood(String name, WoodType woodType, MapColor color, MapColor topColor) {
        //register(name + "_sapling", new SaplingBlock(generator, Block.Settings.copy(Blocks.OAK_SAPLING)), new Item.Settings());
        register(name + "_leaves", new LeavesBlock(Block.Settings.copy(Blocks.OAK_LEAVES)), new Item.Settings());
        register(name + "_log", new PillarBlock(setLogSettings(Block.Settings.copy(Blocks.OAK_LOG), topColor, color)), new Item.Settings());
        register("stripped_" + name + "_log", new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_OAK_LOG)), new Item.Settings());
        register(name + "_wood", new PillarBlock(Block.Settings.copy(Blocks.OAK_WOOD)), new Item.Settings());
        register("stripped_" + name + "_wood", new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_OAK_WOOD)), new Item.Settings());
        Block planks = register(name + "_planks", new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)), new Item.Settings());
        register(name + "_stairs", new StairsBlock(planks.getDefaultState(), Block.Settings.copy(planks)), new Item.Settings());
        register(name  + "_slab", new SlabBlock(Block.Settings.copy(Blocks.OAK_SLAB)), new Item.Settings());
        register(name + "_fence", new FenceBlock(Block.Settings.copy(Blocks.OAK_FENCE)), new Item.Settings());
        register(name + "_fence_gate", new FenceGateBlock(Block.Settings.copy(Blocks.OAK_FENCE_GATE), woodType), new Item.Settings());
        Block door = register(name + "_door", new DoorBlock(Block.Settings.copy(Blocks.OAK_DOOR), woodType.setType()));

        OllivandersItems.register(name + "_door", new TallBlockItem(door, new Item.Settings()));

        register(name + "_button", new ButtonBlock(Block.Settings.copy(Blocks.OAK_BUTTON), woodType.setType(), 30, true), new Item.Settings());

        register(name + "_trapdoor", new TrapdoorBlock(Block.Settings.copy(Blocks.OAK_TRAPDOOR), woodType.setType()), new Item.Settings());
        register(name + "_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, Block.Settings.copy(Blocks.OAK_PRESSURE_PLATE), woodType.setType()), new Item.Settings());
    }

    private static AbstractBlock.Settings setLogSettings(Block.Settings settings, MapColor topMapColor, MapColor sideMapColor) {
        return settings.mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor).instrument(Instrument.BASS).strength(2.0f).sounds(BlockSoundGroup.WOOD).burnable();
    }

    public static <B extends Block> B register(String name, B block) {
        return Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
    }

    public static <B extends Block> B register(String name, B block, Item.Settings itemSettings) {
        BlockItem blockItem = new BlockItem(block, itemSettings);
        Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
        Registry.register(Registries.ITEM, Ollivanders.id(name), blockItem);
        OllivandersItemGroups.addToDefault(blockItem);
        return block;
    }

    private static PistonBlock createPistonBlock(boolean sticky) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> !state.get(PistonBlock.EXTENDED);
        return new PistonBlock(sticky, AbstractBlock.Settings.copy(Blocks.PISTON).strength(1.5f).solidBlock(($, $$, $$$) -> false).suffocates(contextPredicate).blockVision(contextPredicate));
    }
}
