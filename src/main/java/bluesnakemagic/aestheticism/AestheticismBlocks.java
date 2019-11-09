package bluesnakemagic.aestheticism;

import net.fabricmc.fabric.api.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

import bluesnakemagic.aestheticism.block.MuddyDirtBlock;
import bluesnakemagic.aestheticism.block.MuddyGrassBlock;

public class AestheticismBlocks {

    public static final MuddyDirtBlock MUDDY_DIRT;
    public static final MuddyGrassBlock MUDDY_GRASS;

    static {
        MUDDY_DIRT = new MuddyDirtBlock(FabricBlockSettings.copy(Blocks.DIRT).ticksRandomly().build());
        MUDDY_GRASS = new MuddyGrassBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK).build());
    }

    public static void load() {
        register(MUDDY_DIRT, "muddy_dirt");
        register(MUDDY_GRASS, "muddy_grass");
    }

    private static void register(Block b, String path) {
        Registry.register(Registry.BLOCK, Aestheticism.id(path), b);
    }
}
