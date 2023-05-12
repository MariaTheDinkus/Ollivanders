package to.tinypota.ollivanders.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;

public class CustomFireBlock extends FireBlock {

    public CustomFireBlock(Block.Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(AGE, 0).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false));
    }

}
