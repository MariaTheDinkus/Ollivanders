/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 */
package to.tinypota.ollivanders.common.world.feature.foliageplacer;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.common.OllivandersFoliagePlacerTypes;

public class RedwoodFoliagePlacer
extends FoliagePlacer {
    public static final Codec<RedwoodFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> RedwoodFoliagePlacer.fillFoliagePlacerFields(instance).and(IntProvider.createValidatingCodec(0, 24).fieldOf("height").forGetter(placer -> placer.height)).and(IntProvider.createValidatingCodec(0, 6).fieldOf("rings").forGetter(placer -> placer.rings)).apply(instance, RedwoodFoliagePlacer::new));
    private final IntProvider height;
	private final IntProvider rings;

    public RedwoodFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height, IntProvider rings) {
        super(radius, offset);
        this.height = height;
        this.rings = rings;
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return OllivandersFoliagePlacerTypes.REDWOOD_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, FoliagePlacer.TreeNode treeNode, int foliageHeight, int radius, int offset) {
    	for (int times = 0; times < rings.get(random); times++) {
        	int i = 0;
        	for (int j = offset; j >= offset - foliageHeight; --j) {
				var hei = i;
				if (times != 0 && times != rings.get(random) - 1) {
					hei = i + 1;
				}
				this.generateSquare(world, placer, random, config, treeNode.getCenter().down(times * 3), hei, j, treeNode.isGiantTrunk());
				if (j == offset - foliageHeight + 1) {
					--i;
					continue;
				}
				if (i >= radius + treeNode.getFoliageRadius()) continue;
				++i;
			}
		}
    }

    @Override
    public int getRandomRadius(Random random, int baseHeight) {
        return super.getRandomRadius(random, baseHeight) + random.nextInt(Math.max(baseHeight + 1, 1));
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return this.height.get(random);
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx == radius && dz == radius && radius > 0;
    }
}

