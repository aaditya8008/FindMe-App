package com.example.familysafety

import android.app.appsearch.exceptions.AppSearchException
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.familysafety.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.oAuthCredential


class LoginActivity : AppCompatActivity() {
    private  val RC_SIGNIN=12
    lateinit var binding:ActivityLoginBinding
    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
       val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString(R.string.your_web_client_id))
           .requestEmail()
           .build()
         googleSignInClient = GoogleSignIn.getClient(this, gso)
binding.loginButton.setOnClickListener {
    signIn()
}

    }

    private fun signIn() {
       val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGNIN)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGNIN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
              val account=task.getResult(ApiException::class.java) !!

                firebaseAuthWithGoogle(account.idToken!!)
            }
            catch (e:AppSearchException){
                Log.d("Error 12","ApiFailed",e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth=FirebaseAuth.getInstance()
val credential=GoogleAuthProvider.getCredential(idToken,null)
 auth.signInWithCredential(credential).addOnCompleteListener(this){ task->
     if(task.isSuccessful){
Log.d("Fire12","SignInWithCredential:success")
         val user=auth.currentUser
         startActivity(Intent(this,MainActivity::class.java))
         Log.d("FIRE12","SignInWithCredential:success ${user?.displayName}",task.exception)

     }
     else{
         Log.w("TAG","SignInWithCredential:failue",task.exception)


     }

 }
    }
}