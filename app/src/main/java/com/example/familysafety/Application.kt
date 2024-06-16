package com.example.familysafety
import android.app.Application

 class Application:Application() {


     override fun onCreate() {
         super.onCreate()
         SharedPref.init(this)
     }

}