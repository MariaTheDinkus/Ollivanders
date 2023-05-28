package to.tinypota.ollivanders.registry.builder;

import net.minecraft.block.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.TallBlockItem;

public class WoodBlockStorage {
	private final String TRANSLATION_NAME;
	private final String NAME;
	private final SaplingBlock SAPLING;
	private final LeavesBlock LEAVES;
	private final PillarBlock LOG;
	private final PillarBlock STRIPPED_LOG;
	private final PillarBlock WOOD;
	private final PillarBlock STRIPPED_WOOD;
	private final Block PLANKS;
	private final StairsBlock STAIRS;
	private final SlabBlock SLAB;
	private final FenceBlock FENCE;
	private final FenceGateBlock FENCE_GATE;
	private final DoorBlock DOOR;
	private final TallBlockItem DOOR_ITEM;
	private final ButtonBlock BUTTON;
	private final TrapdoorBlock TRAPDOOR;
	private final PressurePlateBlock PRESSURE_PLATE;
	private final SignBlock SIGN;
	private final WallSignBlock WALL_SIGN;
	private final HangingSignBlock HANGING_SIGN;
	private final WallHangingSignBlock WALL_HANGING_SIGN;
	private final BlockFamily FAMILY;
	
	public WoodBlockStorage(String translationName, String name, SaplingBlock sapling, LeavesBlock leaves, PillarBlock log, PillarBlock strippedLog, PillarBlock wood, PillarBlock strippedWood, Block planks, StairsBlock stairs, SlabBlock slab, FenceBlock fence, FenceGateBlock fenceGate, DoorBlock door, TallBlockItem doorItem, ButtonBlock button, TrapdoorBlock trapdoor, PressurePlateBlock pressurePlate, SignBlock sign, WallSignBlock wallSign, HangingSignBlock hangingSign, WallHangingSignBlock wallHangingSign, BlockFamily family) {
		TRANSLATION_NAME = translationName;
		NAME = name;
		SAPLING = sapling;
		LEAVES = leaves;
		LOG = log;
		STRIPPED_LOG = strippedLog;
		WOOD = wood;
		STRIPPED_WOOD = strippedWood;
		PLANKS = planks;
		STAIRS = stairs;
		SLAB = slab;
		FENCE = fence;
		FENCE_GATE = fenceGate;
		DOOR = door;
		DOOR_ITEM = doorItem;
		BUTTON = button;
		TRAPDOOR = trapdoor;
		PRESSURE_PLATE = pressurePlate;
		SIGN = sign;
		WALL_SIGN = wallSign;
		HANGING_SIGN = hangingSign;
		WALL_HANGING_SIGN = wallHangingSign;
		FAMILY = family;
	}
	
	public String getTranslationName() {
		return TRANSLATION_NAME;
	}
	
	public String getName() {
		return NAME;
	}
	
	public SaplingBlock getSapling() {
		return SAPLING;
	}
	
	public LeavesBlock getLeaves() {
		return LEAVES;
	}
	
	public PillarBlock getLog() {
		return LOG;
	}
	
	public PillarBlock getStrippedLog() {
		return STRIPPED_LOG;
	}
	
	public PillarBlock getWood() {
		return WOOD;
	}
	
	public PillarBlock getStrippedWood() {
		return STRIPPED_WOOD;
	}
	
	public Block getPlanks() {
		return PLANKS;
	}
	
	public StairsBlock getStairs() {
		return STAIRS;
	}
	
	public SlabBlock getSlab() {
		return SLAB;
	}
	
	public FenceBlock getFence() {
		return FENCE;
	}
	
	public FenceGateBlock getFenceGate() {
		return FENCE_GATE;
	}
	
	public DoorBlock getDoor() {
		return DOOR;
	}
	
	public TallBlockItem getDoorItem() {
		return DOOR_ITEM;
	}
	
	public ButtonBlock getButton() {
		return BUTTON;
	}
	
	public TrapdoorBlock getTrapdoor() {
		return TRAPDOOR;
	}
	
	public PressurePlateBlock getPressurePlate() {
		return PRESSURE_PLATE;
	}
	
	public SignBlock getSign() {
		return SIGN;
	}
	
	public WallSignBlock getWallSign() {
		return WALL_SIGN;
	}
	
	public HangingSignBlock getHangingSign() {
		return HANGING_SIGN;
	}
	
	public WallHangingSignBlock getWallHangingSign() {
		return WALL_HANGING_SIGN;
	}
	
	public BlockFamily getFamily() {
		return FAMILY;
	}
}
