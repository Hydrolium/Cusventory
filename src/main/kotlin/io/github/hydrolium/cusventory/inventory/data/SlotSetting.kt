package io.github.hydrolium.cusventory.inventory.data

import net.kyori.adventure.sound.Sound
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * 슬롯 정보 중 클릭 이벤트 핸들러와 잠금 여부 정보를 가지는 데이터 클래스입니다.
 *
 * @property clickEvent 클릭 이벤트 핸들러
 * @property isLocked 인벤토리 잠김 여부
 */
data class SlotSetting(
    val clickEvent: ((InventoryClickEvent) -> Unit)? = null,
    val isLocked: Boolean = true,
    val clickSound: Sound? = null
)