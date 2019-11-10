package bluesnakemagic.aestheticism.block;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import bluesnakemagic.aestheticism.AestheticismBlocks;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class MuddyDirtBlock extends Block {

    public static final IntProperty MOISTURE = IntProperty.of("moisture", 1, 7);

    /** Array of [search_index] -> 0x_MM_ZZ_YY_XX where MM is the moistness, ZZ, YY, and XX are co-ordinate offsets from
     * 0,0,0. */
    private static final int[] MOISTNESS_OFFSETS;

    static {
        IntList positions = new IntArrayList();
        Set<BlockPos> implicit = new HashSet<>();
        implicit.add(BlockPos.ORIGIN);
        for (Direction dir : Direction.values()) {
            implicit.add(BlockPos.ORIGIN.offset(dir));
        }
        for (BlockPos p : BlockPos.iterate(-4, 0, -4, 4, 1, 4)) {
            if (implicit.contains(p)) {
                continue;
            }
            double distance = 1.8 * (Math.sqrt(p.getSquaredDistance(0, 0, 0, false)) - 1);
            if (p.getY() < 0) {
                distance++;
                distance *= 2;
            }
            int value = 7 - (int) distance;
            if (value > 0) {
                int packed = 0;
                packed |= p.getX() & 0xFF;
                packed |= (p.getY() & 0xFF) << 8;
                packed |= (p.getZ() & 0xFF) << 16;
                packed |= value << 24;
                positions.add(packed);
            }
        }
        positions.sort(Comparator.comparingDouble(packed -> {
            int x = (byte) (int) packed;
            int y = (byte) (packed >> 8);
            int z = (byte) (packed >> 16);
            return BlockPos.ORIGIN.getSquaredDistance(x, y, z, false);
        }));
        MOISTNESS_OFFSETS = positions.toIntArray();
    }

    public MuddyDirtBlock(Block.Settings settings) {
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

    @Override
    public ItemStack getPickStack(BlockView view, BlockPos pos, BlockState state) {
        return Blocks.DIRT.getPickStack(view, pos, state);
    }

    public static int getMoistness(World world, BlockPos pos) {

        int fromNeighbours = 0;

        for (Direction dir : Direction.values()) {
            BlockPos offsetPos = pos.offset(dir);
            BlockState state = world.getBlockState(offsetPos);
            FluidState fluidState = state.getFluidState();
            if ((!fluidState.isEmpty() && fluidState.matches(FluidTags.WATER)) || world.hasRain(offsetPos.up())) {
                return 7;
            }
            Block block = state.getBlock();
            if (block == AestheticismBlocks.MUDDY_DIRT || block == AestheticismBlocks.MUDDY_GRASS) {
                int f = state.get(MOISTURE);
                if (dir == Direction.UP) {
                    f -= 1;
                } else if (dir == Direction.DOWN) {
                    f -= 3;
                } else {
                    f -= 2;
                }
                fromNeighbours = Math.max(fromNeighbours, f);
            }
        }

        if (fromNeighbours == 0) {
            // Don't bother checking if none of the neighbours are even slightly moist
            // This helps ensure that water spreads somewhat normally.
            return 0;
        }

        for (int packed : MOISTNESS_OFFSETS) {
            int dx = (byte) packed;
            int dy = (byte) (packed >> 8);
            int dz = (byte) (packed >> 16);
            BlockPos offsetPos = pos.add(dx, dy, dz);
            int moistness = packed >>> 24;
            if (fromNeighbours >= moistness) {
                return fromNeighbours;
            }

            if (hasWaterSource(world, offsetPos)) {
                return moistness;
            }
        }
        return 0;
    }

    private static boolean hasWaterSource(World world, BlockPos offsetPos) {
        FluidState fluidState = world.getFluidState(offsetPos);
        return (!fluidState.isEmpty() && fluidState.matches(FluidTags.WATER)) || world.hasRain(offsetPos.up());
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
        if (moistness > max && world.random.nextInt(4) == 0) {
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
