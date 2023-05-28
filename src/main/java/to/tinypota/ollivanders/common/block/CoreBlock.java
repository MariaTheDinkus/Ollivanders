package to.tinypota.ollivanders.common.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.common.block.entity.CoreBlockEntity;

import java.util.Collections;
import java.util.List;

public class CoreBlock extends BlockWithEntity {
	private Identifier core;
	private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	
	public CoreBlock(Identifier core, Block.Settings settings) {
		super(settings);
		this.core = core;
	}
	
	public Identifier getCoreId() {
		return core;
	}
	
	public Item getCore() {
		return Registries.ITEM.get(core);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		for (var i = 0; i < 3; ++i) {
			var d = pos.getX() + random.nextDouble();
			var e = pos.getY() + random.nextDouble() * 1D;
			var f = pos.getZ() + random.nextDouble();
			world.addParticle(ParticleTypes.GLOW, d, e, f, 0.0D, 0.5D, 0.0D);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BASE_SHAPE;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
		return Collections.singletonList(new ItemStack(getCore()));
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(getCore());
	}
	
	@Override
	protected void spawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state) {
	
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CoreBlockEntity(pos, state);
	}
}
