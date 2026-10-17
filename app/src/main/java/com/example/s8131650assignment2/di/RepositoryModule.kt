
package com.example.s8131650assignment2.di

import com.example.s8131650assignment2.data.repository.AppRepository
import com.example.s8131650assignment2.data.repository.AppRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Connects the repository interface to its implementation.
 * Hilt uses this binding to inject AppRepository automatically.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        implementation: AppRepositoryImpl
    ): AppRepository
}
