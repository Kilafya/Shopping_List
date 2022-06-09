package com.kilafyan.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kilafyan.shoppinglist.domain.ShopItem

@Entity(tableName = "shop_items")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val count: Double,
    val enable: Boolean
) {

}