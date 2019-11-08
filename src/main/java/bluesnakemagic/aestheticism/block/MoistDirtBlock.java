package bluesnakemagic.aestheticism.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

public class MoistDirtBlock extends Block {

    public static final IntProperty MOISTURE = IntProperty.of("moisture", 1, 7);

    public MoistDirtBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MOISTURE);
    }

    public static boolean isWaterNearby(ViewableWorld world, BlockPos pos) {
        for (BlockPos p : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (world.getFluidState(p).matches(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.onScheduledTick(state, world, pos, rand);
        if (world.isClient) {
            return;
        }
        int moistness = state.get(MOISTURE);
        if (!isWaterNearby(world, pos) && !world.hasRain(pos.up())) {
            if (moistness > 1) {
                world.setBlockState(pos, state.with(MOISTURE, moistness - 1), 2);
            } else {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
            }
        } else if (moistness < 7) {
            world.setBlockState(pos, state.with(MOISTURE, 7), 2);
        }
    }
}
