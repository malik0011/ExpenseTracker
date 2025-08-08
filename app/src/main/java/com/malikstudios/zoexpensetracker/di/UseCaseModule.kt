package com.malikstudios.zoexpensetracker.di

import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepository
import com.malikstudios.zoexpensetracker.domain.usecase.AddExpenseUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetExpensesByDateUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetTotalForDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * UseCaseModule provides the use cases for the application.
 * It is annotated with @Module and @InstallIn to specify that it should be installed
 * in the SingletonComponent, making the provided instances available throughout the app's lifecycle.
 */

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddExpenseUseCase(repo: ExpenseRepository) =
        AddExpenseUseCase(repo)

    @Provides
    fun provideGetExpensesByDateUseCase(repo: ExpenseRepository) =
        GetExpensesByDateUseCase(repo)

    @Provides
    fun provideGetTotalForDateUseCase(repo: ExpenseRepository) =
        GetTotalForDateUseCase(repo)
}