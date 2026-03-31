package io.github.jaknndiius.cusventory.event

import io.github.jaknndiius.cusventory.inventory.Cusventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent

class CusventoryChangeEvent: Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

        val holder = event.inventory.holder as? Cusventory ?: return

        // 바깥을 눌렀을 때 이벤트 핸들러 필요
        if(event.rawSlot < event.inventory.size) { // 상단(Cusventory) 클릭
            val slotInfo = holder.slot(event.slot)

            slotInfo.clickEvent?.invoke(event)

            slotInfo.clickSound?.let {
                event.whoClicked.playSound(it)
            }

            event.isCancelled = slotInfo.isLocked
        } else { // 하단(플레이어 인벤토리) 클릭
            val playerInventoryInfo = holder.playerInventory()

            event.isCancelled = playerInventoryInfo.isLocked

            playerInventoryInfo.clickEvent?.invoke(event)

        }

    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {

        val holder = event.inventory.holder as? Cusventory ?: return

        val size = holder.inventory.size

        event.isCancelled = event.rawSlots.any {
            it < size && holder.slot(it).isLocked
        }

    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder as? Cusventory ?: return

        holder.getCloseEvent()?.invoke(event)
    }

}