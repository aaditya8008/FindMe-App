package com.example.familysafety

import android.content.Context
import android.content.SharedPreferences


object SharedPref {
private val Name="AppSharedPref"
    private val MODE=Context.MODE_PRIVATE
    lateinit var preferences:SharedPreferences
    fun init(context: Context){
        preferences=context.getSharedPreferences(Name,MODE)
    }
    fun putBoolean(key:String,value:Boolean){
     preferences.edit().putBoolean(key,value).commit()

    }

    fun getBoolean(key:String): Boolean {
        return preferences.getBoolean(key,false)
    }
    fun putString(key:String,S:String){
        preferences.edit().putString(key,S).commit()
    }
    fun getString(key:String,S:String): String? {
        return preferences.getString(key,S)
    }



}