package com.example.familysafety


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.familysafety.SharedPref


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       val status= SharedPref.getBoolean(PrefConstants.IS_USER_LOGGED_IN)
        if(status){

            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


    }
}