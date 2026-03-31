package io.github.jaknndiius.cusventory

import io.github.jaknndiius.cusventory.command.CusventoryCommand
import io.github.jaknndiius.cusventory.event.CusventoryChangeEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin

class CusventoryPlugin : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(CusventoryChangeEvent(), this)

        this.lifecycleManager.registerEventHandler (
            LifecycleEvents.COMMANDS
        ) { commands ->
            commands.registrar().register(CusventoryCommand.build())
        }

        logger.info("Cusventory 플러그인이 활성화 되었습니다.")

    }

    override fun onDisable() { }
}
