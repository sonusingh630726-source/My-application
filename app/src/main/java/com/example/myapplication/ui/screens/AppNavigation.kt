package com.example.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.viewmodel.TransactionViewModel

/**
 * Navigation setup for the Smart Expense Tracker app using Jetpack Navigation Compose.
 * Routes: "dashboard", "add_edit?transactionId={id}", "list"
 */
@Composable
fun AppNavigation(viewModel: TransactionViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToAddEdit = { id ->
                    if (id != null) {
                        navController.navigate("add_edit?transactionId=$id")
                    } else {
                        navController.navigate("add_edit")
                    }
                },
                onNavigateToList = {
                    navController.navigate("list")
                }
            )
        }

        composable(
            route = "add_edit?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: -1L
            AddEditTransactionScreen(
                viewModel = viewModel,
                transactionId = if (transactionId == -1L) null else transactionId,
                onBack = { navController.popBackStack() }
            )
        }

        composable("list") {
            TransactionListScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate("add_edit?transactionId=$id")
                }
            )
        }
    }
}
