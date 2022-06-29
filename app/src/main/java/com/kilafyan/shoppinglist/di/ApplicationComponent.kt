package com.kilafyan.shoppinglist.di

import android.app.Application
import android.content.ContentProvider
import com.kilafyan.shoppinglist.data.ShopListProvider
import com.kilafyan.shoppinglist.presentation.MainActivity
import com.kilafyan.shoppinglist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ShopItemFragment)

    fun inject(provider: ShopListProvider)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}