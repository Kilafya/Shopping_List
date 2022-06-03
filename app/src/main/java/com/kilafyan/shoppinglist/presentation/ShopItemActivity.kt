package com.kilafyan.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.kilafyan.shoppinglist.R
import com.kilafyan.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

//    private lateinit var tilName: TextInputLayout
//    private lateinit var tilCount: TextInputLayout
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var btnSave: Button
//
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        initViews()
        launchRightMode()
//        setupChangedListener()
//        setupErrorListener()
//        setupCloseListener()
    }
//
    private fun launchRightMode() {
        val fragment =  when (screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            else -> throw RuntimeException("Unknown screen mode")
        }
    supportFragmentManager.beginTransaction()
        .add(R.id.shop_item_container, fragment)
        .commit()
    }
//
//    private fun launchAddMode() {
//        btnSave.setOnClickListener {
//            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//
//    private fun launchEditMode() {
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this) {
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
//        btnSave.setOnClickListener {
//            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//    private fun setupErrorListener() {
//        viewModel.errorInputName.observe(this) {
//            val message = if (it) {
//                getString(R.string.error_input_name)
//            } else {
//                null
//            }
//            tilName.error = message
//        }
//
//        viewModel.errorInputCount.observe(this) {
//            val message = if (it) {
//                getString(R.string.error_input_count)
//            } else {
//                null
//            }
//            tilCount.error = message
//        }
//    }
//
//    private fun setupChangedListener() {
//        etName.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputName()
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//        etCount.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputCount()
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }
//
//    private fun setupCloseListener() {
//        viewModel.shouldCloseScreen.observe(this) {
//            finish()
//        }
//    }
//
//    private fun initViews() {
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        btnSave = findViewById(R.id.btn_save)
//    }
//
    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getLongExtra(EXTRA_SHOP_ITEM_ID, -1)
        }
    }

    companion object {
        private const val EXTRA_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Long): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemId)
            return intent
        }
    }
}



