package com.kilafyan.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilafyan.shoppinglist.data.ShopListRepositoryImpl
import com.kilafyan.shoppinglist.domain.DeleteShopItemUseCase
import com.kilafyan.shoppinglist.domain.EditShopItemUseCase
import com.kilafyan.shoppinglist.domain.GetShopListUseCase
import com.kilafyan.shoppinglist.domain.ShopItem

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()



    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val item = shopItem.copy(enable = !shopItem.enable)
        editShopItemUseCase.editShopItem(item)
    }



}