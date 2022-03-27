package dev.debuggings.modspoof.commands

import dev.debuggings.modspoof.utils.Utils
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler

class ModSpoofCommand : Command("modspoof") {

    @DefaultHandler
    fun handle() {
        val mods = Utils.loadConfig().joinToString("§7, §3").dropLast(4)
        Utils.chat("§8[§3ModSpoof§8] §7Hidden Mods: §3$mods")
    }
}
