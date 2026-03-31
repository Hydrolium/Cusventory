package io.github.hydrolium.cusventory.inventory.data

import net.kyori.adventure.sound.Sound
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * 슬롯 정보를 가지는 클래스입니다.
 *
 * @property cusventoryInfo 인벤토리 정보(크기, 아이템, 잠금여부 등).
 * @property slotIdx 슬롯 인덱스 번호
 * @property item 아이템 정보
 * @property clickEvent 클릭 이벤트 핸들러
 * @property isLocked 인벤토리 잠김 여부
 */
class SlotInfo(
    val cusventoryInfo: CusventoryInfo,
    val slotIdx: Int,
    var item: ItemStack?,
    var clickEvent: ((InventoryClickEvent) -> Unit)?,
    var isLocked: Boolean,
    var clickSound: Sound?
) {
    fun extract() = SlotSetting(clickEvent, isLocked, clickSound)
}