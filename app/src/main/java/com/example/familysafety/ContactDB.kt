package com.example.familysafety

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class ContactDB :RoomDatabase(){
   abstract fun contactDao():ContactDao


   companion object {
       @Volatile
       private  var INSTANCE:ContactDB?=null
       fun getDB(context: Context):ContactDB{
          INSTANCE?.let {
              return it
          }
           return synchronized(ContactModel::class.java){ val instance=Room.databaseBuilder(context.applicationContext,ContactDB::class.java,"my_family_db").build()
               INSTANCE=instance
           instance
           }



       }
   }
}