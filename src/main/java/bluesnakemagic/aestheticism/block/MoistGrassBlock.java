package bluesnakemagic.aestheticism.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MoistGrassBlock extends SpreadableBlock {

    protected MoistGrassBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MoistDirtBlock.MOISTURE);
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.onScheduledTick(state, world, pos, rand);
    }
}
