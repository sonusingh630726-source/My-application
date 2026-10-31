package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a financial transaction in the Room Database.
 * Designed for BCA students learning modern Android database persistence.
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val amount: Double,
    val type: String, // "Income" or "Expense"
    val category: String, // "Food", "Travel", "Shopping", "Education", "Salary", "Other"
    val date: String // Date in format YYYY-MM-DD
)
