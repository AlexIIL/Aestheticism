package bluesnakemagic.aestheticism.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MuddyGrassBlock extends GrassBlock {

    public MuddyGrassBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MuddyDirtBlock.MOISTURE);
    }

    @Override
    public Identifier getDropTableId() {
        return Blocks.GRASS_BLOCK.getDropTableId();
    }

    @Override
    public ItemStack getPickStack(BlockView view, BlockPos pos, BlockState state) {
        return Blocks.GRASS_BLOCK.getPickStack(view, pos, state);
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
        MuddyDirtBlock.processTick(dryState, state, world, pos);
    }
}
