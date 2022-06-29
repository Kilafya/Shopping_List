package com.kilafyan.shoppinglist.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kilafyan.shoppinglist.data.ShopListProvider
import com.kilafyan.shoppinglist.databinding.FragmentShopItemBinding
import com.kilafyan.shoppinglist.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment: Fragment() {

    private val component by lazy {
        (requireActivity().application as ShopApplicaation).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mViewModel: ShopItemViewModel

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Long = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
        binding.viewModel = mViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        launchRightMode()
        setupCloseListener()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        with(binding) {
            btnSave.setOnClickListener {
//                mViewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
                thread {
                    context?.contentResolver?.insert(
                        Uri.parse("content://com.kilafyan.shoppinglist/shop_items"),
                        ContentValues().apply {
                            put(ShopListProvider.COLUMN_ID, 0L)
                            put(ShopListProvider.COLUMN_NAME, etName.text?.toString())
                            put(ShopListProvider.COLUMN_COUNT, etCount.text?.toString()?.toDouble())
                            put(ShopListProvider.COLUMN_ENABLE, true)
                        }
                    )
                }
            }
        }
    }

    private fun launchEditMode() {
        with(binding) {
            mViewModel.getShopItem(shopItemId)
            btnSave.setOnClickListener {
//                mViewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
                thread {
                    context?.contentResolver?.update(
                        Uri.parse("content://com.kilafyan.shoppinglist/shop_items"),
                        ContentValues().apply {
                            put(ShopListProvider.COLUMN_ID, shopItemId)
                            put(ShopListProvider.COLUMN_NAME, etName.text?.toString())
                            put(ShopListProvider.COLUMN_COUNT, etCount.text?.toString()?.toDouble())
                            put(ShopListProvider.COLUMN_ENABLE, true)
                        },
                        null,
                        null
                    )
                }
            }
        }
    }

    private fun setupCloseListener() {
        mViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun parseParam() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode: $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getLong(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)

        }
    }

    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Long): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putLong(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}