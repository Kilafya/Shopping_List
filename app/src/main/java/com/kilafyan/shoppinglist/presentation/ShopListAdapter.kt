package com.kilafyan.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.kilafyan.shoppinglist.R
import com.kilafyan.shoppinglist.databinding.ItemShopDisableBinding
import com.kilafyan.shoppinglist.databinding.ItemShopEnableBinding
import com.kilafyan.shoppinglist.domain.ShopItem

class ShopListAdapter: ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            TYPE_ENABLE -> R.layout.item_shop_enable
            TYPE_DISABLE -> R.layout.item_shop_disable
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val binding =  DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        with(holder.binding) {
            when (this) {
                is ItemShopEnableBinding -> {
                    this.shopItem = shopItem
                }
                is ItemShopDisableBinding -> {
                    this.shopItem = shopItem
                }
            }
            root.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(shopItem)
                true
            }
            root.setOnClickListener {
                onShopItemClickListener?.invoke(shopItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enable) TYPE_ENABLE else TYPE_DISABLE
    }

    companion object {
        const val TYPE_ENABLE = 1
        const val TYPE_DISABLE = 2

        const val MAX_POOL_SIZE = 20
    }
}