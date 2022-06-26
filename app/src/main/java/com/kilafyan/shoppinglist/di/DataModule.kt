package com.kilafyan.shoppinglist.di

import com.kilafyan.shoppinglist.data.ShopListRepositoryImpl
import com.kilafyan.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository
}