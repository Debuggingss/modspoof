package dev.debuggings.modspoof.mixins;

import dev.debuggings.modspoof.ModSpoof;
import net.minecraftforge.fml.client.GuiSlotModList;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiSlotModList.class)
public class MixinGuiSlotModList {

    @Unique
    private String stringPrefix;

    @ModifyVariable(
        method = "drawSlot",
        at = @At("STORE"),
        ordinal = 0,
        remap = false
    )
    private ModContainer drawSlot(ModContainer mc) {
        switch (mc.getModId()) {
            case "FML":
            case "Forge":
            case "mcp":
                stringPrefix = "§2✔ §r";
                break;
            case "modspoof":
                stringPrefix = "§4✖ §r";
                break;
            default:
                if (ModSpoof.Companion.getStates().get(mc.getModId())) stringPrefix = "§c✖ §r";
                else stringPrefix = "§a✔ §r";
                break;
        }
        return mc;
    }

    @ModifyVariable(
        method = "drawSlot",
        at = @At("STORE"),
        ordinal = 0,
        remap = false
    )
    private String drawSlot2(String name) {
        return stringPrefix + name;
    }
}
