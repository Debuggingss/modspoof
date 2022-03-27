package dev.debuggings.modspoof.mixins;

import dev.debuggings.modspoof.ModSpoof;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public class MixinModList extends FMLHandshakeMessage {

    @Shadow(remap = false) private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"), remap = false)
    private void removeMod(List<ModContainer> modlist, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isSingleplayer()) return;

        modTags.keySet().removeIf(key -> key.equals("modspoof"));

        for (Map.Entry<String, Boolean> mod : ModSpoof.Companion.getStates().entrySet()) {
            if (mod.getValue()) {
                modTags.keySet().removeIf(key -> key.equals(mod.getKey()));
                System.out.println("[ModSpoof] Removed " + mod.getKey() + " from mod list.");
            }
        }
    }
}
