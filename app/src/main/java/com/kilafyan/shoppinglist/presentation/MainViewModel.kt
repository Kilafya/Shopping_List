package com.kilafyan.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilafyan.shoppinglist.domain.DeleteShopItemUseCase
import com.kilafyan.shoppinglist.domain.EditShopItemUseCase
import com.kilafyan.shoppinglist.domain.GetShopListUseCase
import com.kilafyan.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
): ViewModel() {

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        viewModelScope.launch {
            val item = shopItem.copy(enable = !shopItem.enable)
            editShopItemUseCase.editShopItem(item)
        }
    }
}