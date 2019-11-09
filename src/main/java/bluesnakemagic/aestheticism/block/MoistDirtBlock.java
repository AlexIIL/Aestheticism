package bluesnakemagic.aestheticism.block;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

public class MoistDirtBlock extends Block {

    public static final IntProperty MOISTURE = IntProperty.of("moisture", 1, 7);

    /** Array of [search_index][x=0, y=1, z=2, moistness=3] */
    private static final int[][] MOISTNESS_OFFSETS;

    static {
        List<BlockPos> positions = new ArrayList<>();
        for (BlockPos p : BlockPos.iterate(-4, -1, -4, 4, 1, 4)) {
            if (BlockPos.ORIGIN.equals(p)) {
                continue;
            }
            double distance = 1.8 * (Math.sqrt(p.getSquaredDistance(0, 0, 0, false)) - 1);
            int value = 7 - (int) distance;
            if (value > 0) {
                positions.add(p.toImmutable());
            }
        }
        positions.remove(BlockPos.ORIGIN);
        positions.sort(Comparator.comparingDouble(p -> p.getSquaredDistance(0, 0, 0, false)));
        MOISTNESS_OFFSETS = new int[positions.size()][4];
        for (int i = 0; i < positions.size(); i++) {
            BlockPos pos = positions.get(i);
            MOISTNESS_OFFSETS[i][0] = pos.getX();
            MOISTNESS_OFFSETS[i][1] = pos.getY();
            MOISTNESS_OFFSETS[i][2] = pos.getZ();
            double distance = 1.8 * (Math.sqrt(pos.getSquaredDistance(0, 0, 0, false)) - 1);
            MOISTNESS_OFFSETS[i][3] = 7 - (int) distance;
        }
    }

    public MoistDirtBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MOISTURE);
    }

    @Override
    public Identifier getDropTableId() {
        return Blocks.DIRT.getDropTableId();
    }

    public static int getMoistness(ViewableWorld world, BlockPos pos) {
        for (int[] arr : MOISTNESS_OFFSETS) {
            int dx = arr[0];
            int dy = arr[1];
            int dz = arr[2];
            if (world.getFluidState(pos.add(dx, dy, dz)).matches(FluidTags.WATER)) {
                return arr[3];
            }
        }
        return 0;
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.onScheduledTick(state, world, pos, rand);
        processTick(Blocks.DIRT.getDefaultState(), state, world, pos);
    }

    public static void processTick(BlockState dryState, BlockState state, World world, BlockPos pos) {
        if (world.isClient) {
            return;
        }
        int moistness = state.get(MOISTURE);
        int max = getMoistness(world, pos);
        if (max <= 0 && !world.hasRain(pos.up())) {
            if (moistness > 1) {
                world.setBlockState(pos, state.with(MOISTURE, moistness - 1), 2);
            } else {
                world.setBlockState(pos, dryState, 2);
            }
        } else if (moistness < max) {
            world.setBlockState(pos, state.with(MOISTURE, moistness + 1), 2);
        }
    }
}
