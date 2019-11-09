package bluesnakemagic.aestheticism;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

public class Aestheticism implements ModInitializer {

    public static final String MODID = "aestheticism";

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize() {
        AestheticismBlocks.load();
    }
}
