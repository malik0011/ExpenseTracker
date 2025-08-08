package com.malikstudios.zoexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.malikstudios.zoexpensetracker.data.model.ExpenseEntity

/*
 * AppDatabase is the main database class for the ZoExpenseTracker app.
 */
@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}