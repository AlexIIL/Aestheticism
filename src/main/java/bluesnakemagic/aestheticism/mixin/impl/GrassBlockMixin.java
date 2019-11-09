package bluesnakemagic.aestheticism.mixin.impl;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import bluesnakemagic.aestheticism.AestheticismBlocks;
import bluesnakemagic.aestheticism.block.MoistDirtBlock;

@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin extends SpreadableBlock {

    protected GrassBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.onScheduledTick(state, world, pos, rand);
        if (world.isClient) {
            return;
        }
        state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.GRASS_BLOCK) {
            double max = MoistDirtBlock.getMoistness(world, pos);
            if (max > 0 || world.hasRain(pos.up())) {
                BlockState newState = AestheticismBlocks.MOIST_GRASS.getDefaultState();
                newState = newState.with(MoistDirtBlock.MOISTURE, 1);
                world.setBlockState(pos, newState, 2);
            }
        }
    }
}
