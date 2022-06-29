package com.kilafyan.shoppinglist.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kilafyan.shoppinglist.R
import com.kilafyan.shoppinglist.data.ShopListProvider
import com.kilafyan.shoppinglist.databinding.ActivityMainBinding
import com.kilafyan.shoppinglist.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as ShopApplicaation).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
    private lateinit var mAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        viewModel.shopList.observe(this) {
            mAdapter.submitList(it)
        }

        binding.btnAddShopItem.setOnClickListener{
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
        thread {
            val cursor = contentResolver.query(
                Uri.parse("content://com.kilafyan.shoppinglist/shop_items"),
                null,
                null,
                null,
                null,
                null
            )
            while (cursor?.moveToNext() == true) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(ShopListProvider.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ShopListProvider.COLUMN_NAME))
                val count = cursor.getDouble(cursor.getColumnIndexOrThrow(ShopListProvider.COLUMN_COUNT))
                val enabled = cursor.getInt(cursor.getColumnIndexOrThrow(ShopListProvider.COLUMN_ENABLE)) > 0
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enable = enabled
                )
                Log.d("MainActivity", "Shop Item:\n$shopItem")
            }
            cursor?.close()
        }
    }

    private fun isOnePaneMode() = binding.shopItemContainer == null

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun setupRecyclerView() {
        mAdapter = ShopListAdapter()
        with(binding.rvShopList) {
            adapter = mAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.TYPE_ENABLE,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.TYPE_DISABLE,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }

        setupLongClickListener()
        setupClickListener()
        setupSwiperListener(binding.rvShopList)
    }

    private fun setupSwiperListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = mAdapter.currentList[viewHolder.adapterPosition]
//                viewModel.deleteShopItem(item)
                thread {
                    contentResolver.delete(
                        Uri.parse("content://com.kilafyan.shoppinglist/shop_items"),
                        null,
                        arrayOf(item.id.toString())
                    )
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        mAdapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        mAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }
}