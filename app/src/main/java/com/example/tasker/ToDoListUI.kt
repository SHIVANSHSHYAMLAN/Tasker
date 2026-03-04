package com.example.tasker

import android.Manifest
import android.R.attr.onClick
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.widget.Button
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import kotlinx.coroutines.delay


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
 @OptIn(ExperimentalMaterial3Api::class)
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
         .fillMaxWidth()
         .padding(start = 4.dp)


     val identifyTheSystemTheme = isSystemInDarkTheme()

     var NotifyOr by remember { mutableStateOf(false) }
     var SelectedTime : TimePickerState? by remember { mutableStateOf(null) }

     var isVisible by remember { mutableStateOf(true) } // controls visibility

     AnimatedVisibility(
         visible = isVisible,
         exit = slideOutHorizontally(
             targetOffsetX = { -it }, // slide out to the left
             animationSpec = tween(durationMillis = 500)
         ) + fadeOut(animationSpec = tween(500))
     ) {

         Box(modifier = if (identifyTheSystemTheme) BoxModifierDarkTheme else BoxModifierBrightTheme) {
             Column() {
                 Row(
                     modifier = Modifier.fillMaxSize(),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.Start
                 ) {
                     Text(TaskName, style = TextStyling, modifier = TextModifier.weight(1f))
                     Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                         IconButton(onClick = {
                             NotifyOr = true
                         }) {
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
                             isVisible = false

                         }) {
                             Icon(
                                 Icons.Default.Delete,
                                 contentDescription = "Delete the to-do list item you have selected",
                                 tint = Color.White
                             )
                         }
                     }

                 }

                 if (NotifyOr) {
                     Notify(
                         onConfirm = { time ->
                         SelectedTime = time
                         CreateNotificationChannelTODO(context, TaskName, Id.toString())
                         NotifyOr = false
                     }, onDismiss = { NotifyOr = false }, modifier = Modifier
                             .fillMaxWidth()
                             .align(Alignment.CenterHorizontally)
                     )
                 }
             }
         }
     }
 }


@Composable
fun AddItemsForTODO(TODO : TODO = TODO(), navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
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
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notify(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier
){
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        TimeInput(
            state = timePickerState
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Button(onClick = { onConfirm(timePickerState) }){
                Text("Confirm")
            }
            Button(onClick = onDismiss){
                Text("Dismiss")
            }
        }
    }
}

fun CreateNotificationChannelTODO(context: Context, Description: String, Id : String){
    val Channel_Deatails = Notifications(Id)
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(Channel_Deatails.Channel_Id, Channel_Deatails.Channel_Name,
            NotificationManager.IMPORTANCE_DEFAULT).apply{
            description = "This is an task notification feature"
        }
        val notificationManager : NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, Channel_Deatails.Channel_Id)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Tasker")
        .setContentText(Description)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)){
        if(ActivityCompat.checkSelfPermission(
            context,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
        ){
            return@with}
        notify(Channel_Deatails.Channel_Id.toInt(), builder.build())
    }

}