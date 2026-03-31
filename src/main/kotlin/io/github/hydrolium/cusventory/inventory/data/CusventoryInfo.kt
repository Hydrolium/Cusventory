package io.github.hydrolium.cusventory.inventory.data

/**
 * Cusventory에 대한 정보가 담긴 **읽기 전용** 인터페이스입니다.
 *
 */
interface CusventoryInfo {

    /**
     * 인벤토리의 슬롯 갯수.
     */
    val size: Int

    /**
     * 인벤토리의 라인 갯수.
     */
    val line: Int

    /**
     * 해당 인벤토리의 슬롯의 정보가 담긴 객체를 반환합니다.
     *
     * @param slotIdx 원하는 슬롯 인덱스.
     *
     * @return 해당 슬롯의 정보가 담긴 [SlotInfo]객체.
     */
    fun slot(slotIdx: Int): SlotInfo

    /**
     * 잠겨 있는(isLocked = true) 모든 슬롯 인덱스의 집합을 반환합니다.
     *
     * @return 잠긴 슬롯 인덱스의 [Set].
     */
    fun getLockedSlots(): Set<Int>

    /**
     * 잠금 해제된(isLocked = false) 모든 슬롯 인덱스의 집합을 반환합니다.
     *
     * @return 잠금 해제된 슬롯 인덱스의 [Set].
     */
    fun getUnLockedSlots(): Set<Int>

    /**
     * 플레이어 인벤토리에 대한 클릭 이벤트 핸들러와 잠금 여부가 담긴 객체를 반환합니다.
     *
     * @return 플레이어 인벤토리 설정 정보가 담긴 [PlayerInventorySetting] 객체.
     */
    fun playerInventory(): PlayerInventorySetting
}