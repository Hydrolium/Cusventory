package io.github.jaknndiius.cusventory.inventory

/**
 * [Cusventory]들을 페이지 번호로 묶어 관리합니다.
 *
 * [CusventoryPager.createCusventoryPage]로 묶은 페이지들은
 * [Cusventory.getPager]를 통해 이전/다음/원하는 페이지의 [Cusventory]를 확인할 수 있습니다.
 */
class CusventoryPager(
    private val cusventories: List<Cusventory>,
    private val index: Int
) {

    companion object {

        /**
         * 페이지 정보가 저장된 페이지 메니저를 각 인벤토리에 저장하고,
         * 원하는 페이지의 인벤토리를 반환합니다.(기본값: 첫번째 페이지)
         *
         * @param cusventories 페이지 인벤토리 리스트. 리스트 순서대로 페이지가 저장됨.
         * @param returnPageIndex 반환될 페이지 인덱스.
         **
         * @return [returnPageIndex] 번째 페이지의 인벤토리
         *
         * @throws IllegalArgumentException [cusventories]가 비어있을 경우.
         * @throws IndexOutOfBoundsException [returnPageIndex] 가 [cusventories]의 범위에서 벗어날 경우.
         */
        fun createCusventoryPage(cusventories: List<Cusventory>, returnPageIndex: Int = 0): Cusventory {
            if(cusventories.isEmpty()) throw IllegalArgumentException("최소 1개의 페이지가 필요합니다.")
            if(returnPageIndex !in 0..cusventories.lastIndex) throw IndexOutOfBoundsException("시작 인덱스($returnPageIndex)가 범위를 벗어납니다.")

            cusventories.forEachIndexed { idx, cusventory ->
                cusventory.apply {
                    attachPager(CusventoryPager(cusventories, idx))
                }
            }
            return cusventories[returnPageIndex]
        }
    }

    /**
     * 현재 페이지의 인벤토리를 반환합니다.
     *
     * @return 현재 페이지의 인벤토리.
     */
    fun getCurrentCusventory() = cusventories[index]

    /**
     * 페이지 크기를 반환합니다.
     *
     * @return 페이지 크기.
     */
    fun getPageSize() = cusventories.size

    /**
     * 페이지 번호를 반환합니다.
     *
     * @return 페이지 볺.
     */
    fun getPageNum() = index

    /**
     * 현재 페이지에서 이전의 페이지가 있는지 확인합니다.
     *
     * @return 이전 페이지가 있으면 `true`, 없으면 `false`.
     */
    private fun hasPrevious() = index > 0

    /**
     * 현재 페이지에서 다음의 페이지가 있는지 확인합니다.
     *
     * @return 다음 페이지가 있으면 `true`, 없으면 `false`.
     */
    private fun hasNext() = index < cusventories.lastIndex

    /**
     * 해당 인덱스의 페이지가 있는지 확인합니다.
     *
     * @param idx 확인할 페이지 인덱스.
     * @return [idx]번째 페이지가 있으면 `true`, 없으면 `false`.
     */
    private fun hasPage(idx: Int) = idx in 0..cusventories.lastIndex

    /**
     * 이전 페이지 존재 여부를 확인한 뒤 이전 페이지 정보가 담긴 페이지 매니저를 반환합니다.
     *
     * @return 이전 페이지가 있으면 이전 페이지 매니저, 없으면 자기 자신.
     */
    fun previous(): CusventoryPager = if(hasPrevious()) cusventories[index - 1].getPager() ?: this else this
//    fun previous(): CusventoryPager = if(hasPrevious()) CusventoryPager(cusventories, index - 1) else this

    /**
     * 다음 페이지 존재 여부를 확인한 뒤 다음 페이지 정보가 담긴 페이지 매니저를 반환합니다.
     *
     * @return 다음 페이지가 있으면 다음 페이지 매니저, 없으면 자기 자신.
     */
    fun next(): CusventoryPager = if(hasNext()) cusventories[index + 1].getPager() ?: this else this
//    fun next(): CusventoryPager = if(hasNext()) CusventoryPager(cusventories, index + 1) else this

    /**
     * 해당 인덱스의 페이지 존재 여부를 확인한 뒤 페이지 정보가 담긴 페이지 매니저를 반환합니다.
     *
     * @param idx 원하는 페이지 인덱스.
     * @return [idx] 번쨰 페이지가 있으면 해당 페이지 매니저, 없으면 자기 자신.
     */
    fun goto(idx: Int): CusventoryPager = if(hasPage(idx)) cusventories[idx].getPager() ?: this else this
//    fun goto(idx: Int): CusventoryPager = if(hasPage(idx)) CusventoryPager(cusventories, idx) else this

}