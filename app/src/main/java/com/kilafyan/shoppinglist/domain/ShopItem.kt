package com.kilafyan.shoppinglist.domain

data class ShopItem(
    val name: String,
    val count: Double,
    val enable: Boolean,
    var id: Long = UNDEFINED_ID
) {

    companion object {

        const val UNDEFINED_ID = -1L
    }
}
