package com.example.search_image.di

import com.example.search_image.common.utils.NetworkUtil
import com.example.search_image.common.utils.NetworkUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkUtilModule {

    @Binds
    @Singleton
    abstract fun buildNetworkUtil(
        networkUtilImpl: NetworkUtilsImpl,
    ): NetworkUtil

}