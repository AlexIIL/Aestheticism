package bluesnakemagic.aestheticism.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MoistGrassBlock extends GrassBlock {

    public MoistGrassBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MoistDirtBlock.MOISTURE);
    }

    @Override
    public Identifier getDropTableId() {
        return Blocks.GRASS_BLOCK.getDropTableId();
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.onScheduledTick(state, world, pos, rand);
        if (world.isClient) {
            return;
        }
        state = world.getBlockState(pos);
        if (state.getBlock() != this) {
            return;
        }
        BlockState dryState = Blocks.GRASS_BLOCK.getDefaultState().with(SnowyBlock.SNOWY, state.get(SnowyBlock.SNOWY));
        MoistDirtBlock.processTick(dryState, state, world, pos);
    }
}
