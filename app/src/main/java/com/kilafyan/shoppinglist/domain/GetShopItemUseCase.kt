package com.kilafyan.shoppinglist.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopItem(shopItemId: Long) : ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}