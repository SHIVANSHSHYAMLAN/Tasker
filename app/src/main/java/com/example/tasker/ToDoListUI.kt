package com.example.tasker

import android.R.attr.onClick
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot.Companion.global
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun ToDoListUI(navController: NavController, TODO: TODO = TODO()){
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            LargeFloatingActionButton(onClick = {navController.navigate(AllDestinations.AddItems.route)}) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "This is to add items"
                )
            }
        }
    ) {innerPadding->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()){
             var lister by  remember { mutableStateOf(TODO.AccessElements(context)) }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(1),
                verticalItemSpacing = 9.dp
            ) {
                items(lister){Elements ->
                    MyTasks(Elements.name, Elements.Id, navController = navController)
                }
            }
        }
    }
}
 @SuppressLint("SuspiciousIndentation")
 @Composable
 fun MyTasks(TaskName : String, Id : Long, AddTaskViewModel : AddClassVIewModel = AddClassVIewModel(), navController: NavController){
     val context = LocalContext.current
     val DarkThemeBrushBackground = remember {
         Brush.linearGradient(
             listOf(
                 Color(0x80FF84BF), // Hot Pink, 50% opacity
                 Color(0x92FF5DAD), // Deep Pink, ~60% opacity
                 Color(0x6FFFB6C1), // Light Pink, ~40% opacity
                 Color(0x77FF85A1)  // Soft Rose, ~30% opacity
             )
         )
     }

     val BrightThemeBrushBackground = remember {
         Brush.linearGradient(
             listOf(
                 Color(0xA3FF7D7D),
                 Color(0xB2FF4A49)
             )
         )
     }
     val BoxModifierDarkTheme = Modifier
         .clip(RoundedCornerShape(40.dp))
         .background(DarkThemeBrushBackground)
         .requiredHeightIn(min = 80.dp)
         .wrapContentSize(Alignment.CenterStart)


     val BoxModifierBrightTheme = Modifier
         .clip(RoundedCornerShape(40.dp))
         .background(BrightThemeBrushBackground)
         .requiredHeightIn(min = 80.dp)
         .wrapContentSize(Alignment.CenterStart)

     val TextStyling = TextStyle(
         color = Color.White,
         fontWeight = FontWeight.Bold,
         fontSize = 19.sp,
         textAlign = TextAlign.Center
     )

     val TextModifier = Modifier
         .padding(start = 20.dp)


     val identifyTheSystemTheme = isSystemInDarkTheme()


         Box(modifier = if(identifyTheSystemTheme) BoxModifierDarkTheme else BoxModifierBrightTheme) {
             Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                 Text(TaskName, style = TextStyling, modifier = TextModifier)
                 Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                     IconButton(onClick = {}) {
                         Icon(
                             Icons.Default.Notifications,
                             contentDescription = "Add or remove notification for it for the time you want",
                             tint = Color.White
                         )
                     }

                     IconButton(onClick = {
                         navController.navigate(AllDestinations.EditItems.createRoute(Id))
                     }) {
                         Icon(
                             Icons.Default.Edit,
                             contentDescription = "Edit the to-do item",
                             tint = Color.White
                         )
                     }

                     IconButton(onClick = {
                         AddTaskViewModel.Delete(context, Id)
                     }) {
                         Icon(
                             Icons.Default.Delete,
                             contentDescription = "Delete the to-do list item you have selected",
                             tint = Color.White
                         )
                     }
                 }
             }
         }
 }


@Composable
fun AddItemsForTODO(TODO : TODO = TODO(), navController: NavController){
    Column(modifier = Modifier.fillMaxSize().background(Color.Cyan), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        val context = LocalContext.current
        var showError by remember { mutableStateOf(false) }
        var TaskSaver by remember { mutableStateOf(TODO.Task) }
        TextField(
            value = TaskSaver,
            onValueChange = {
                TaskSaver = it
                showError = false
                            },
            label = {Text("Please enter the task")},
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Blue,
                focusedLabelColor = Color.White,
                focusedContainerColor = Color.LightGray,
                cursorColor = Color.Blue
            ),
            modifier = Modifier.size(height = 120.dp, width = 280.dp)
        )
        if(showError){
            Text("Please enter something in the task field", color = Color.Red)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                if(TaskSaver.isNotBlank()){
                    TODO.Task = TaskSaver
                    TODO.Save(context)
                    TODO.Task = ""
                    navController.popBackStack()
                }
                else{
                    showError = true
                }
            }) {
                Text("Save")
            }
            Button(onClick = {
                TODO.Task = ""
                TODO.Reject()
                navController.popBackStack()
            }) {
                Text("Reject")
            }
        }
    }
}


@Composable
fun EditItemsForTODO(TODO : TODO = TODO(), navController: NavController, Id : Long){
    Column(modifier = Modifier.fillMaxSize().background(Color.Cyan), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        val context = LocalContext.current
        var showError by remember { mutableStateOf(false) }
        var TaskSaver by remember { mutableStateOf(TODO.Task) }
        TextField(
            value = TaskSaver,
            onValueChange = {
                TaskSaver = it
                showError = false
            },
            label = {Text("Please enter the task")},
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Blue,
                focusedLabelColor = Color.White,
                focusedContainerColor = Color.LightGray,
                cursorColor = Color.Blue
            ),
            modifier = Modifier.size(height = 120.dp, width = 280.dp)
        )
        if(showError){
            Text("Please enter something in the task field", color = Color.Red)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                if(TaskSaver.isNotBlank()){
                    TODO.Task = TaskSaver
                    TODO.Edit(context, Id)
                    TODO.Task = ""
                    navController.popBackStack()
                }
                else{
                    showError = true
                }
            }) {
                Text("Save")
            }
            Button(onClick = {
                TODO.Task = ""
                TODO.Reject()
                navController.popBackStack()
            }) {
                Text("Reject")
            }
        }
    }
}