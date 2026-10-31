package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.TransactionEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing transaction state and communicating with the Room Database.
 */
class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).transactionDao()

    // All transactions flow exposed as StateFlow
    val allTransactions: StateFlow<List<TransactionEntity>> = dao.getAllTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Total income flow
    val totalIncome: StateFlow<Double> = dao.getTotalIncome()
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // Total expense flow
    val totalExpense: StateFlow<Double> = dao.getTotalExpense()
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // Calculated balance (Income - Expense)
    val balance: StateFlow<Double> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Add a new transaction
    fun addTransaction(title: String, amount: Double, type: String, category: String, date: String) {
        viewModelScope.launch {
            val transaction = TransactionEntity(
                title = title,
                amount = amount,
                type = type,
                category = category,
                date = date
            )
            dao.insertTransaction(transaction)
        }
    }

    // Update an existing transaction
    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            dao.updateTransaction(transaction)
        }
    }

    // Delete a transaction
    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
        }
    }

    // Get a single transaction by ID for editing
    suspend fun getTransactionById(id: Long): TransactionEntity? {
        return dao.getTransactionById(id)
    }
}
