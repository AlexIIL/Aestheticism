package bluesnakemagic.aestheticism.mixin.impl;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin extends SpreadableBlock {

    protected GrassBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        // TODO Auto-generated method stub
        throw new AbstractMethodError("// TODO: Implement this!");
    }
}
