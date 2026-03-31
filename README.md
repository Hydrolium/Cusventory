# Cusventory

## 개요
**Cusventory**는 PaperMC(Minecraft 1.21.8) 기반의 커스텀 인벤토리 관련 라이브러리입니다.  
기존에 복잡한 Inventory 생성 로직을 간편하게 설정하도록 설계되었습니다.

## 주요 기능
- 직관적인 슬롯 설정
- 인벤토리 Frame 설정, 인벤토리 프리징, 아이템 반환 등의 편의성 함수 제공
- 인벤토리를 페이지 별로 묶고 이전/다음 페이지 이동을 쉽게 구현 가능 
- 인벤토리 생성 코드에 인벤토리 클릭 이벤트 추가 가능

## 환경
- **Language**: Kotlin(2.2.0-Beta2)
- **Platform**: Paper API(1.21.8-R0.1-SNAPSHOT)
- **Build Tool**: Gradle(Kotlin DSL)
- **Java Version**: 21

## 구조
- `io.github.hydrolium.cusventory.inventory`: 핵심 인벤토리 클래스 및 페이징
- `io.github.hydrolium.cusventory.inventory.data`: 슬롯 정보, 인벤토리 크기, 인벤토리 설정값 등의 데이터 클래스
- `io.github.hydrolium.cusventory.event`: 인벤토리 이벤토 핸들링
- `io.github.hydrolium.cusventory.command`: 인벤토리 테스트 및 예시 커맨드

## 사용 예시
```kotlin
// Cusventory 생성
val cusventory = Cusventory("인벤토리 이름", InventorySize.LINE_6).apply {
    // 테두리 설정
    frame { slotIdx ->
        item = ItemStack(Material.DIAMOND).modifyMeta {
            setDisplayName("$slotIdx 슬롯의 테두리")
        }
    }
    // 단일 슬롯 설정
    slot(9) {
        item = ItemStack(Material.PINK_BED)
        clickEvent = { event ->
            event.whoClicked.sendMessage("인벤토리 클릭됨!")
        }
    }
    // 다중 슬롯 설정
    slot(10..16) {
        item = ItemStack(Material.DIAMOND)
        isLocked = false // 아이템 꺼내기 허용
        clickSound = Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 1.0f, 1.0f)
    }
    // 플레이어 인벤토리 설정
    playerInventory {
        isLocked = false
    }
    // 인벤토리 닫기 이벤트 설정
    setCloseEvent { event ->
        val player = event.player as Player
        val holder = event.inventory.holder as Cusventory

        holder.giveUnlockedItem(player) // isLocked = false인 슬롯의 아이템을 플레이어에게 주기
    }
    
    freeze() // 인벤토리 보호 활성화
}

// 인벤토리 오픈
cusventory.open(player)
```