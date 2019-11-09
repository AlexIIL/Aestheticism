package bluesnakemagic.aestheticism.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import net.fabricmc.fabric.api.block.FabricBlockSettings;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import bluesnakemagic.aestheticism.block.OrdinaryDirtBlock;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Redirect(
        method = "<clinit>",
        at = @At(value = "NEW", ordinal = 0),
        slice = @Slice(
            from = @At(value = "FIELD", target = "Lnet/minecraft/sound/BlockSoundGroup;GRASS", ordinal = 0),
            to = @At(value = "FIELD", target = "Lnet/minecraft/sound/BlockSoundGroup;GRAVEL", ordinal = 1)),
        allow = 1,
        require = 1)
    private static Block redirectDirt(Block.Settings settings) {
        return new OrdinaryDirtBlock(FabricBlockSettings.copyOf(settings).ticksRandomly().build());
    }
}
