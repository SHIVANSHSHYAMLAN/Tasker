package com.example.tasker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.jar.Attributes

const val DataBase_Name = "TODo"
const val Version = 1
const val Table_Name = "MyTasks"
const val Ids = "Id"
const val Task_Name = "Name"

class ToDoDataBase(context: Context) : SQLiteOpenHelper(context, DataBase_Name, null, Version){
    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = """
            CREATE TABLE $Table_Name(
            $Ids INTEGER PRIMARY KEY AUTOINCREMENT,
            $Task_Name TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(create_table)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }

    public fun AddTasks(item : ToDoItemsStoreType){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Task_Name, item.name)
        }
        db.insert(Table_Name, null, values)
        db.close()
    }

    public fun UpdateTask(task : ToDoItemsStoreType, id : Long){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Task_Name, task.name)
        }
        db.update(Table_Name, values, "$Ids = ?", arrayOf(id.toString()))
        db.close()
    }
    public fun SearchTasks() : Array<ToDoItemsStoreReturnType>{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Table_Name}", null)
        val AllTasks = mutableListOf<ToDoItemsStoreReturnType>()
        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(Ids))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task_Name))
            AllTasks.add(ToDoItemsStoreReturnType(name, id))
        }
        cursor.close()
        db.close()
        return AllTasks.toTypedArray()
    }

    public fun DeleteTask(id : Long){
        val db = this.writableDatabase
        db.delete(Table_Name, "$Ids = ?", arrayOf(id.toString()) )
        db.close()
    }
}


