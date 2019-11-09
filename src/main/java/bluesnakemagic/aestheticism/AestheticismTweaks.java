package bluesnakemagic.aestheticism;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

import bluesnakemagic.aestheticism.mixin.impl.HoeItemAccessor;

public class AestheticismTweaks {
    public static void load() {
        putTillingBlocks();
    }

    private static void putTillingBlocks() {
        HoeItemAccessor acc = (HoeItemAccessor) Items.DIAMOND_HOE;
        Map<Block, BlockState> map = acc.getTILLED_BLOCKS();

        map.put(AestheticismBlocks.MUDDY_GRASS, Blocks.FARMLAND.getDefaultState());
        map.put(AestheticismBlocks.MUDDY_DIRT, Blocks.FARMLAND.getDefaultState());
    }
}
