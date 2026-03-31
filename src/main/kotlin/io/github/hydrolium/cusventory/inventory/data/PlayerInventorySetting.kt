package io.github.hydrolium.cusventory.inventory.data

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * 플레이어의 인벤토리 클릭 이벤트 핸들러와 잠금 여부 정보를 가지는 데이터 클래스입니다.
 *
 * @property clickEvent 클릭 이벤트 핸들러
 * @property isLocked 인벤토리 잠김 여부
 */
data class PlayerInventorySetting (
    var clickEvent: ((InventoryClickEvent) -> Unit)? = null,
    var isLocked: Boolean = true
)