package dev.debuggings.modspoof

import dev.debuggings.modspoof.commands.ModSpoofCommand
import dev.debuggings.modspoof.core.Config
import dev.debuggings.modspoof.utils.Utils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.GuiModList
import net.minecraftforge.fml.client.config.GuiCheckBox
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.lang.reflect.Field


@Mod(
    modid = ModSpoof.MOD_ID,
    name = ModSpoof.MOD_NAME,
    version = ModSpoof.VERSION
)
class ModSpoof {

    companion object {
        const val MOD_ID = "modspoof"
        const val MOD_NAME = "ModSpoof"
        const val VERSION = "1.0"
        const val configLocation = "./config/modspoof.toml"

        val blacklistedModids = arrayOf("Forge", "FML", "mcp", "modspoof")

        var config: Config? = null

        val mc: Minecraft = Minecraft.getMinecraft()
        var checkbox: GuiCheckBox? = null

        var states: HashMap<String, Boolean> = HashMap()

        val listWidth: Field = GuiModList::class.java.getDeclaredField("listWidth")
        val selectedMod: Field = GuiModList::class.java.getDeclaredField("selectedMod")
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        listWidth.isAccessible = true
        selectedMod.isAccessible = true

        MinecraftForge.EVENT_BUS.register(this)

        ModSpoofCommand().register()

        config = Config()
        config?.preload()

        val savedStates = Utils.loadConfig()
        Loader.instance().modList.forEach {
            states[it.modId] = savedStates.contains(it.modId)
        }
    }

    @SubscribeEvent
    fun initGui(event: InitGuiEvent.Post) {
        if (event.gui is GuiModList) {
            val gui = event.gui as GuiModList
            val width = listWidth.get(gui) as Int

            checkbox = GuiCheckBox(
                555,
                width + 15,
                gui.height - 23,
                "Hide Mod",
                false
            )

            checkbox?.visible = false

            event.buttonList.add(checkbox)
        }
    }

    @SubscribeEvent
    fun actionPerformed(event: GuiScreenEvent.ActionPerformedEvent.Post) {
        if (
            event.gui is GuiModList &&
            event.button is GuiCheckBox &&
            event.button.id == 555
        ) {
            val button = event.button as GuiCheckBox
            val gui = event.gui as GuiModList
            var selected = selectedMod.get(gui) ?: return
            selected = selected as ModContainer

            states[selected.modId] = button.isChecked
        }
    }

    @SubscribeEvent
    fun guiClick(event: GuiScreenEvent.MouseInputEvent.Post) {
        if (event.gui is GuiModList) {
            val gui = event.gui as GuiModList
            var selected = selectedMod.get(gui) ?: return
            selected = selected as ModContainer

            if (blacklistedModids.contains(selected.modId)) {
                checkbox?.visible = false
                return
            }

            checkbox?.visible = true
            checkbox?.setIsChecked(states[selected.modId] ?: false)
        }
    }

    @SubscribeEvent
    fun guiOpen(event: GuiOpenEvent) {
        if (event.gui is GuiMainMenu) Utils.saveConfig()
    }
}
