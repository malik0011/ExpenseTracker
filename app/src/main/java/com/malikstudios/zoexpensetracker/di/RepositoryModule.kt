package com.malikstudios.zoexpensetracker.di

import com.malikstudios.zoexpensetracker.data.local.ExpenseDao
import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepository
import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule provides the ExpenseRepository implementation for dependency injection.
 * It is annotated with @Module and @InstallIn to specify that it should be installed
 * in the SingletonComponent, making the provided instances available throughout the app's lifecycle.
 */

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository =
        ExpenseRepositoryImpl(dao)
}