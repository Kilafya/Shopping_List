package com.kilafyan.shoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.kilafyan.shoppinglist.domain.ShopItem
import com.kilafyan.shoppinglist.presentation.ShopApplicaation
import javax.inject.Inject

class ShopListProvider: ContentProvider() {

    private val component by lazy {
        (context as ShopApplicaation).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.kilafyan.shoppinglist", "shop_items", GET_SHOP_ITEMS_QUERY)
        addURI("com.kilafyan.shoppinglist", "shop_items/#", GET_SHOP_ITEM_BY_ID)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor()
            }
            else -> {
                null
            }
        }
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                if (values ==null) return null
                val id = values.getAsLong(COLUMN_ID)
                val name = values.getAsString(COLUMN_NAME)
                val count = values.getAsDouble(COLUMN_COUNT)
                val enable = values.getAsBoolean(COLUMN_ENABLE)
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enable = enable
                )
                shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
            }

        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                val id = selectionArgs?.get(0)?.toLong() ?: -1L
                return shopListDao.deleteShopItemSync(id)
            }
        }
        return 0
    }

    override fun update(uri: Uri,
                        values: ContentValues?,
                        selection: String?,
                        selectionArgs: Array<out String>?
    ): Int {
        when(uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                val id = values?.getAsLong(COLUMN_ID) ?: -1
                val name = values?.getAsString(COLUMN_NAME) ?: ""
                val count = values?.getAsDouble(COLUMN_COUNT) ?: 0.0
                val enable = values?.getAsBoolean(COLUMN_ENABLE) ?: true
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enable = enable
                )
                shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
                return 1
            }
        }
        return 0
    }

    companion object {

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_COUNT = "count"
        const val COLUMN_ENABLE = "enable"

        private const val GET_SHOP_ITEMS_QUERY = 100
        private const val GET_SHOP_ITEM_BY_ID = 101
    }
}