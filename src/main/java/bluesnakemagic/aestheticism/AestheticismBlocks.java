package bluesnakemagic.aestheticism;

import net.fabricmc.fabric.api.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

import bluesnakemagic.aestheticism.block.MoistDirtBlock;
import bluesnakemagic.aestheticism.block.MoistGrassBlock;

public class AestheticismBlocks {

    public static final MoistDirtBlock MOIST_DIRT;
    public static final MoistGrassBlock MOIST_GRASS;

    static {
        MOIST_DIRT = new MoistDirtBlock(FabricBlockSettings.copy(Blocks.DIRT).ticksRandomly().build());
        MOIST_GRASS = new MoistGrassBlock(FabricBlockSettings.copy(Blocks.GRASS_BLOCK).build());
    }

    public static void load() {
        register(MOIST_DIRT, "moist_dirt");
        register(MOIST_GRASS, "moist_grass");
    }

    private static void register(Block b, String path) {
        Registry.register(Registry.BLOCK, Aestheticism.id(path), b);
    }
}
