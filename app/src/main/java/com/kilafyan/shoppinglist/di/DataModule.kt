package com.kilafyan.shoppinglist.di

import android.app.Application
import com.kilafyan.shoppinglist.data.AppDataBase
import com.kilafyan.shoppinglist.data.ShopListDao
import com.kilafyan.shoppinglist.data.ShopListRepositoryImpl
import com.kilafyan.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDataBase.getInstance(application).shopListDao()
        }
    }
}