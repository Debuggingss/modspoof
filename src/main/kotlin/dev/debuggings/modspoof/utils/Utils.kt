package dev.debuggings.modspoof.utils

import dev.debuggings.modspoof.ModSpoof
import net.minecraft.util.ChatComponentText

object Utils {

    fun loadConfig(): List<String> {
        return ModSpoof.config?.hiddenMods?.split("\n") ?: emptyList()
    }

    fun saveConfig() {
        var modstring = ""

        for ((modid, hidden) in ModSpoof.states) {
            if (hidden) modstring += "$modid\n"
        }

        ModSpoof.config?.hiddenMods = modstring

        ModSpoof.config?.markDirty()
        ModSpoof.config?.writeData()

        println("[ModSpoof] Saved Config.")
    }

    fun chat(msg: String) {
        ModSpoof.mc.thePlayer.addChatMessage(ChatComponentText(msg))
    }
}
