package com.malikstudios.zoexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.malikstudios.zoexpensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/*
  * ExpenseDao interface defines the data access object for expense-related operations.
  * It provides methods to insert expenses and retrieve them by date or all expenses.
 */
@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY timestampMillis DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY timestampMillis DESC")
    fun getExpensesByDate(date: String): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amountInPaise) FROM expenses WHERE date = :date")
    fun getTotalForDate(date: String): Flow<Long>

}