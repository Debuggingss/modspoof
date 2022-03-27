package dev.debuggings.modspoof.core

import dev.debuggings.modspoof.ModSpoof
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType

import java.io.File

class Config : Vigilant(File(ModSpoof.configLocation)) {

    @Property(
        type = PropertyType.PARAGRAPH,
        name = "Hidden Mods",
        category = "General",
        hidden = true
    )
    var hiddenMods = ""

    init {
        initialize()
    }
}
