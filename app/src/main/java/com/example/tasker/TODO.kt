package com.example.tasker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController

open class TODO() : ViewModel() {
    var Task by mutableStateOf("")
    private val Tasker = MyTasksRepoFIle()
    fun Save(context: Context){
        Tasker.AddAToDoItem(ToDoItemsStoreType(Task), context)
    }
    fun Reject(){

    }

    fun Edit(context: Context, Id : Long){
        Tasker.UpdateToDoItem(ToDoItemsStoreType(Task), Id ,context)
    }
    fun AccessElements(context : Context) = Tasker.GetArrayOfTODO(context)
}

class AddClassVIewModel() : ViewModel(){
    val List = MyTasksRepoFIle()
    fun Edit(context: Context, Id : Long){

    }
    fun Delete(context: Context, Id: Long){
        var Deleter by mutableStateOf(List.GetArrayOfTODO(context))
        List.DeleteToDoItem(Id, context)
        Deleter = List.GetArrayOfTODO(context)
    }

    fun Elements(context : Context) = List.GetArrayOfTODO(context)
}