package io.github.jaknndiius.cusventory.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.github.jaknndiius.cusventory.inventory.Cusventory
import io.github.jaknndiius.cusventory.inventory.data.InventorySize
import io.github.jaknndiius.cusventory.inventory.CusventoryPager
import io.github.jaknndiius.cusventory.inventory.modifyMeta
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.sound.Sound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object CusventoryCommand {

    fun build(): LiteralCommandNode<CommandSourceStack> {
        return create().build()
    }

    private fun create(): LiteralArgumentBuilder<CommandSourceStack> {

        return Commands.literal("cusventory")
            .then(
                Commands.literal("test")
                    .requires { ctx -> ctx.sender is Player }
                    .executes { ctx ->

                    val sender = ctx.source.sender as Player

                    val cusventory0 = Cusventory("안녕ff", InventorySize.LINE_6).apply {
                        frame { slotIdx ->
                            item = ItemStack(Material.DIAMOND).modifyMeta {
                                setDisplayName("$slotIdx 슬롯의 테두리")
                            }
                        }
                        slot(9) {
                            item = ItemStack(Material.PINK_BED)
                            clickSound = Sound.sound(org.bukkit.Sound.UI_BUTTON_CLICK, Sound.Source.MASTER, 1.0f, 1.0f)
                            clickEvent = { event ->
                                val holder = event.inventory.holder as Cusventory

                                holder.apply {
                                    slot(10) {
                                        item = ItemStack(Material.DIAMOND)
                                    }
                                    slot(19) {
                                        item = item?.modifyMeta {
                                            setDisplayName("sss")
                                        }
                                    }
                                 }
                            }
                        }
                        slot(17) {
                            item = ItemStack(Material.PINK_BED).modifyMeta {
                                displayName(cusventoryInfo.slot(16).item?.displayName())
                            }
                            clickEvent = { event ->
                                val holder = event.inventory.holder as Cusventory
                                holder.getPager()?.next()?.getCurrentCusventory()?.open(event.whoClicked)
                            }
                        }
                        slot(19..26) { slotIdx ->
                            item = ItemStack(Material.BARRIER).modifyMeta {
                                setDisplayName("$slotIdx 슬롯의 아이템")
                            }
                        }
                        slot(11) {
                            clickEvent = { event ->
                                event.whoClicked.sendMessage("AAAFFFA")
                            }
                        }
                        slot(10..16) {
                            isLocked = false
                            clickSound = Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 1.0f, 1.0f)

                        }
                        playerInventory {
                            isLocked = false
                        }
                        setCloseEvent { event ->
                            val player = event.player as Player
                            val holder = event.inventory.holder as Cusventory

                            holder.giveUnlockedItem(player)
                        }
                        freeze()
                    }

                    val cusventory1 = Cusventory("안녕하세요", InventorySize.LINE_5).apply {
                        frame(ItemStack(Material.EMERALD))
                        slot(9) {
                            item = ItemStack(Material.PINK_BED)
                            clickEvent = { event ->
                                val holder = event.inventory.holder as Cusventory
                                holder.getPager()?.previous()?.getCurrentCusventory()?.open(event.whoClicked)
                            }
                        }
                        slot(17) {
                            item = ItemStack(Material.PINK_BED)
                            clickEvent = { event ->
                                val holder = event.inventory.holder as Cusventory
                                holder.getPager()?.next()?.getCurrentCusventory()?.open(event.whoClicked)
                            }
                        }
                        slot(10..16) {
                            isLocked = false
                        }
                        setCloseEvent { event ->
                            val player = event.player as Player
                            val holder = event.inventory.holder as Cusventory

                            holder.giveUnlockedItem(player)
                        }

                        freeze()
                    }

                    val cusventory2 = Cusventory("안녕하세요!!!", InventorySize.LINE_4).apply {
                        frame(ItemStack(Material.GOLD_INGOT))
                        slot(9) {
                            item = ItemStack(Material.PINK_BED)
                            clickEvent = { event ->
                                val holder = event.inventory.holder as Cusventory
                                holder.getPager()?.previous()?.getCurrentCusventory()?.open(event.whoClicked)
                            }
                        }
                        slot(17) {
                            item = ItemStack(Material.PINK_BED)
                            clickEvent = { event ->
                                val holder = event.inventory.holder as Cusventory
                                holder.getPager()?.next()?.getCurrentCusventory()?.open(event.whoClicked)
                            }
                        }
                        setCloseEvent { event ->
                            val player = event.player as Player
                            val holder = event.inventory.holder as Cusventory

                            holder.giveUnlockedItem(player)
                        }
                        slot(10..16) {
                            isLocked = false
                        }
                        freeze()
                    }

                    val cusventory = CusventoryPager.createCusventoryPage(listOf(cusventory0, cusventory1, cusventory2))

                    cusventory.open(sender)

                    Command.SINGLE_SUCCESS
                }
            )
    }
}