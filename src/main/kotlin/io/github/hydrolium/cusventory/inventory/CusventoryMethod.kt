package io.github.hydrolium.cusventory.inventory

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun ItemStack.modifyMeta(metaEditor: ItemMeta.() -> Unit) =
    apply {
        editMeta { meta ->
            meta.metaEditor()
        }
    }
