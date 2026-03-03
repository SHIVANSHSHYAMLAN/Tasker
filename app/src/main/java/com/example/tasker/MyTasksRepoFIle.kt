package com.example.tasker

import android.content.Context
import androidx.compose.ui.platform.LocalContext

class MyTasksRepoFIle(){
    public fun AddAToDoItem(AddItem : ToDoItemsStoreType, context: Context){
        val Adder = ToDoDataBase(context)
        Adder.AddTasks(AddItem)
    }

    public fun DeleteToDoItem(id : Long, context: Context){
        val Deleter = ToDoDataBase(context)
        Deleter.DeleteTask(id)
    }

    public fun UpdateToDoItem(UpadteItem : ToDoItemsStoreType, id: Long, context: Context){
        val Updater = ToDoDataBase(context)
        Updater.UpdateTask(UpadteItem, id)
    }

    public fun GetArrayOfTODO(context : Context) : Array<ToDoItemsStoreReturnType>{
        val Searcher = ToDoDataBase(context)
        val SearcherArray = Searcher.SearchTasks()
        return SearcherArray
    }
}