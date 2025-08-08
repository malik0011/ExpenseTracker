package com.malikstudios.zoexpensetracker.di

import android.content.Context
import androidx.room.Room
import com.malikstudios.zoexpensetracker.data.local.AppDatabase
import com.malikstudios.zoexpensetracker.data.local.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DatabaseModule provides the Room database and DAO for dependency injection.
 * It is annotated with @Module and @InstallIn to specify that it should be installed
 * in the SingletonComponent, making the provided instances available throughout the app's lifecycle.
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "zo_expense_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()
}