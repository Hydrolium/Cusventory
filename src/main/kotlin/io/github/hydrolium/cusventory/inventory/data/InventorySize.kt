package io.github.hydrolium.cusventory.inventory.data

/**
 * Bukkit/Minecraft 인벤토리의 크기를 줄(line) 단위로 정의합니다.
 *
 * 이 enum은 1줄(9칸)부터 6줄(54칸)까지의 표준 크기를 나타냅니다.
 * [line]과 [size] 프로퍼티를 통해 실제 줄 수와 총 슬롯 수를 쉽게 계산할 수 있습니다.
 */
enum class InventorySize {
    LINE_1,
    LINE_2,
    LINE_3,
    LINE_4,
    LINE_5,
    LINE_6;

    val line: Int
        get() = ordinal + 1

    val size: Int
        get() = line * 9
}