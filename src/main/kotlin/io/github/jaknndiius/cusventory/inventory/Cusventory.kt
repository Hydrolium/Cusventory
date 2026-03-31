package io.github.jaknndiius.cusventory.inventory

import io.github.jaknndiius.cusventory.inventory.data.CusventoryInfo
import io.github.jaknndiius.cusventory.inventory.data.InventorySize
import io.github.jaknndiius.cusventory.inventory.data.PlayerInventorySetting
import io.github.jaknndiius.cusventory.inventory.data.SlotInfo
import io.github.jaknndiius.cusventory.inventory.data.SlotSetting
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.collections.forEach

/**
 * 인벤토리의 아이템, 클릭 이벤트, 잠금 여부 등을 설정 및 관리하는 커스텀 인벤토리 홀더입니다.
 */
open class Cusventory(
    name: String,
    inventorySize: InventorySize
): InventoryHolder, CusventoryInfo {

    override val line = inventorySize.line
    override val size = inventorySize.size

    private val inventory: Inventory = Bukkit.createInventory(this, size, Component.text(name))

    override fun getInventory() = inventory

    private val slotSettings = mutableMapOf<Int, SlotSetting>()

    private var playerInventorySetting = PlayerInventorySetting()

    private var closeEvent: ((InventoryCloseEvent) -> Unit)? = null

    private val dataKey = NamespacedKey("cusventory", "cusventory_inventory_item")

    // [fillEmpty] 함수에서 사용할 빈 아이템
    private val emptyItem = ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
        .modifyMeta {
            itemModel = Material.AIR.key
            isHideTooltip = true
            persistentDataContainer.set(dataKey, PersistentDataType.BYTE, 1)
        }

    private var cusventoryPager: CusventoryPager? = null

    /**
     * 페이지 메니저를 설정합니다.
     *
     * **[CusventoryPager.createCusventoryPage] 로 페이지 생성 시 자동으로 설정됩니다.**
     * 
     * @param cusventoryPager 설정할 [CusventoryPager].
     */
    fun attachPager(cusventoryPager: CusventoryPager) {
        this.cusventoryPager = cusventoryPager
    }

    /**
     * 설정된 페이지 메니저를 반환합니다.
     *
     * @return 이 인벤토리의 [CusventoryPager].
     */
    fun getPager() = cusventoryPager

    /**
     * 인벤토리를 닫을 때 호출되는 이벤트 핸들러를 설정합니다.
     *
     * @param closeEvent 인벤토리 닫기 이벤트 핸들러.
     */
    fun setCloseEvent(closeEvent: (InventoryCloseEvent) -> Unit) {
        this.closeEvent = closeEvent
    }

    /**
     * 인벤토리를 닫을 때 호출되는 이벤트 핸들러를 반환합니다.
     *
     * @return 인벤토리 닫기 이벤트 핸들러.
     */
    fun getCloseEvent() : ((InventoryCloseEvent) -> Unit)? = closeEvent

    override fun slot(slotIdx: Int): SlotInfo {
        val slotSetting = slotSettings[slotIdx] ?: SlotSetting()

        return SlotInfo(
            this,
            slotIdx,
            inventory.getItem(slotIdx)?.modifyMeta {
                persistentDataContainer.remove(dataKey)
            }?.clone(),
            slotSetting.clickEvent,
            slotSetting.isLocked,
            slotSetting.clickSound
        )
    }

    override fun getLockedSlots() = slotSettings.filterValues { it.isLocked }.keys
    override fun getUnLockedSlots() = slotSettings.filterValues { !it.isLocked }.keys

    /**
     * [slotIdx] 번째 슬롯의 아이템과 클릭이벤트, 잠금여부를 설정합니다.
     *
     * 이 함수는 람다 블록 안에서
     * [SlotInfo.item]과 [SlotInfo.clickEvent], [SlotInfo.isLocked]를 직관적으로 설정할 수 있게 합니다.
     *
     * [block] 내에서 설정되지 않은 [SlotInfo.item], [SlotInfo.clickEvent], [SlotInfo.isLocked]는 기존 값을 유지합니다.
     *
     * 유효한 인벤토리 범위(0 until [size])를 벗어난 경우,
     * 함수는 아무 작업도 수행하지 않고 즉시 종료됩니다.
     *
     * @param slotIdx 설정할 슬롯의 인덱스.
     * @param block 슬롯을 변경하는 블록.
     */
    fun slot(slotIdx: Int, block: SlotInfo.() -> Unit) {
        if(slotIdx !in 0 until size) return

        val slotInfo = slot(slotIdx).apply(block)

        inventory.setItem(slotIdx, slotInfo.item?.clone())

        slotSettings[slotIdx] = slotInfo.extract()
    }

    /**
     * 여러 슬롯의 아이템과 클릭이벤트, 잠금여부를 설정합니다.
     *
     * 이 함수는 람다 블록 안에서 [SlotInfo] 객체의 컨텍스트를 제공하여,
     * [SlotInfo.item]과 [SlotInfo.clickEvent], [SlotInfo.isLocked]를 직관적으로 설정할 수 있게 합니다.
     *
     * [block] 내에서 설정되지 않은 [SlotInfo.item], [SlotInfo.clickEvent], [SlotInfo.isLocked]는 기존 값을 유지합니다.
     *
     * 유효한 인벤토리 범위(0 until [size])를 벗어난 슬롯 번호의 경우, 아무 작업도 하지 않습니다.
     *
     * @param slotIdxs 설정할 슬롯의 인덱스 컬렉션([IntRange], [List] 등).
     * @param block 슬롯을 변경하는 블록.
     */
    fun slot(slotIdxs: Iterable<Int>, block: SlotInfo.(Int) -> Unit) {
        for(slotIdx in slotIdxs) {
            if(slotIdx !in 0 until size) continue

            val slotInfo = slot(slotIdx).apply { block(slotIdx) }

            inventory.setItem(slotIdx, slotInfo.item?.clone())

            slotSettings[slotIdx] = slotInfo.extract()
        }
    }

    /**
     * 인벤토리 테두리의 슬롯의 아이템을 설정하고,
     * 모든 클릭 이벤트를 제거한 뒤 잠금니다.
     *
     * @param frameItem 테두리에 배치할 아이템.
     */
    fun frame(frameItem: ItemStack) {
        frame {
            item = frameItem
            clickEvent = null
            isLocked = true
        }
    }

    /**
     * 인벤토리 테두리의 슬롯의 아이템과 클릭이벤트, 잠금여부를 설정합니다.
     *
     * 이 함수는 람다 블록 안에서 [SlotInfo] 객체의 컨텍스트를 제공하여,
     * [SlotInfo.item]과 [SlotInfo.clickEvent], [SlotInfo.isLocked]를 직관적으로 설정할 수 있게 합니다.
     *
     * [block] 내에서 설정되지 않은 [SlotInfo.item], [SlotInfo.clickEvent], [SlotInfo.isLocked]는 기존 값을 유지합니다.
     *
     * @param block 슬롯을 변경하는 블록.
     */
    fun frame(block: SlotInfo.(Int) -> Unit) {
        for (slotIdx in 0 until size) {
            val row = slotIdx / 9
            val col = slotIdx % 9

            if(row == 0 || row == (line-1) || col == 0 || col == 8) {

                val slotInfo = slot(slotIdx).apply { block(slotIdx) }

                inventory.setItem(slotIdx, slotInfo.item?.clone())

                slotSettings[slotIdx] = slotInfo.extract()
            }
        }
    }

    override fun playerInventory() = playerInventorySetting

    /**
     * 플레이어 인벤토리에 대한 클릭 이벤트, 잠금여부를 설정합니다.
     *
     * 이 함수는 람다 블록 안에서 [PlayerInventorySetting] 객체의 컨텍스트를 제공하여,
     * [PlayerInventorySetting.clickEvent], [PlayerInventorySetting.isLocked]를 직관적으로 설정할 수 있게 합니다.
     *
     * [block] 내에서 설정되지 않은 [PlayerInventorySetting.clickEvent], [PlayerInventorySetting.isLocked]는 기존 값을 유지합니다.
     *
     * @param block 플레이어 인벤토리 설정을 변경하는 블록.
     */
    fun playerInventory(block: PlayerInventorySetting.() -> Unit) {
        playerInventorySetting = playerInventorySetting.apply(block)
    }

    /**
     * 잠겨있고 비어있는 슬롯을 보이지 않는 아이템으로 모두 채우며,
     * 잠겨있고 비어있지 않은 슬롯의 아이템을 내부적으로 다른 아이템으로 변경합니다.
     *
     * * 모든 인벤토리를 채워
     * shift + left-click 등의 동작으로 인벤토리가 의도치 않게 채워지는 상황과
     * * double left-click 등의 동작으로 잠겨있는 슬롯의 아이템을 의도치 않게 플레이어가 얻는 상황을 방지합니다.
     */
    fun freeze() {

        for (slotIdx in getLockedSlots()) {

            val item = inventory.getItem(slotIdx)

            if(item == null) {
                inventory.setItem(slotIdx, emptyItem.clone())
            } else {
                inventory.setItem(slotIdx, item.clone().modifyMeta {
                    persistentDataContainer.set(dataKey, PersistentDataType.BYTE, 2)
                })
            }
        }
    }

    /**
     * 잠금 해제된 모든 슬롯의 아이템을 플레이어 인벤토리에 넣습니다.
     * 플리이어 인벤토리가 가득차면 플레이어의 위치에 아이템을 떨어뜨립니다.
     *
     * @param player 아이템을 줄 플레이어.
     */
    fun giveUnlockedItem(player: Player) {

        for (slotIdx in getUnLockedSlots()) {

            val item = inventory.getItem(slotIdx)?.modifyMeta {
                persistentDataContainer.remove(dataKey)
            }?.clone() ?: continue

            val remaining = player.inventory.addItem(item)

            remaining.values.forEach {
                player.world.dropItem(player.location, it)
            }
        }
    }

    /**
     * 플레이어에게 [Cusventory] 인벤토리를 엽니다.
     *
     * @param player 대상 플레이어.
     */
    fun open(player: Player) {
        player.openInventory(inventory)
    }

    /**
     * 플레이어에게 [Cusventory] 인벤토리를 엽니다.
     *
     * @param player 대상 플레이어.
     */
    fun open(player: HumanEntity) {
        player.openInventory(inventory)
    }
}