package bluesnakemagic.aestheticism;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

public class AestheticismClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockColorProvider mapper = AestheticismClient::getGrassColour;
        ColorProviderRegistry.BLOCK.register(mapper, AestheticismBlocks.MUDDY_GRASS);
    }

    private static int getGrassColour(BlockState state, ExtendedBlockView view, BlockPos pos, int tint) {
        if (view != null && pos != null) {
            return BiomeColors.getGrassColor(view, pos);
        } else {
            return GrassColors.getColor(0.5D, 1.0D);
        }
    }
}
