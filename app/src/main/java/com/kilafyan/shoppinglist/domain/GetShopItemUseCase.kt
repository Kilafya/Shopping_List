package com.kilafyan.shoppinglist.domain

import javax.inject.Inject

class GetShopItemUseCase @Inject constructor(
    private val shopListRepository: ShopListRepository
    ) {

    suspend fun getShopItem(shopItemId: Long) : ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}