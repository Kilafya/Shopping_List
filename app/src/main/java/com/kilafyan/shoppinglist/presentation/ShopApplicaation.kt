package com.kilafyan.shoppinglist.presentation

import android.app.Application
import com.kilafyan.shoppinglist.di.DaggerApplicationComponent

class ShopApplicaation: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}