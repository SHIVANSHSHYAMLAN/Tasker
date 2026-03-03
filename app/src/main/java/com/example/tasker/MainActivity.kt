package com.example.tasker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasker.ui.theme.TaskerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskerTheme {
                val navController = rememberNavController()
                val TODO : TODO = viewModel()


                NavHost(
                    navController = navController,
                    startDestination = AllDestinations.Home.route
                ){
                    composable(AllDestinations.Home.route){ToDoListUI(navController)}
                    composable(AllDestinations.AddItems.route) { AddItemsForTODO(TODO, navController)}
                    composable(route = "${AllDestinations.EditItems.route}/{ItemId}",
                        arguments = listOf(navArgument("ItemId") { type = NavType.LongType})) {backStackEntry->
                        val itemId = backStackEntry.arguments?.getLong("ItemId") ?: 0L
                        EditItemsForTODO(navController = navController, Id = itemId)
                    }
                }
            }
        }
    }
}

sealed class AllDestinations(val route : String){
    object Home : AllDestinations("Home")
    object AddItems : AllDestinations("AddItems")

    object EditItems : AllDestinations("EditItems"){
        fun createRoute(itemId : Long) = "EditItems/$itemId"
    }
}