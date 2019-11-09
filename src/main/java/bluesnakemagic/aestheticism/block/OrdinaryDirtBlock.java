package bluesnakemagic.aestheticism.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import bluesnakemagic.aestheticism.AestheticismBlocks;

public class OrdinaryDirtBlock extends Block {
    public OrdinaryDirtBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.onScheduledTick(state, world, pos, rand);
        if (world.isClient) {
            return;
        }
        state = world.getBlockState(pos);
        if (state.getBlock() == AestheticismBlocks.ORDINARY_DIRT) {
            double max = MuddyDirtBlock.getMoistness(world, pos);
            if (max > 0) {
                BlockState newState = AestheticismBlocks.MUDDY_DIRT.getDefaultState();
                newState = newState.with(MuddyDirtBlock.MOISTURE, 1);
                world.setBlockState(pos, newState, 2);
            }
        }
    }
}
