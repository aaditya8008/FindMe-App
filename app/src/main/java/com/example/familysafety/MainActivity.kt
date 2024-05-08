package com.example.familysafety

import android.Manifest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

import com.example.familysafety.databinding.ActivityMainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainActivityBinding
    val permissions= arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_CONTACTS
    )
    val permissionCode=78
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askforPermission()
        binding=ActivityMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //orit
       binding.bottomNavigation.setOnItemSelectedListener {menu->
            when (menu.itemId) {
                R.id.guard -> {
                    inflateFragement(GuardFragment.newInstance())
                }
                R.id.home -> {
                    inflateFragement(HomeFragement.newInstance())
                }
                R.id.dashboard -> {
                    inflateFragement(MapsFragment())
                }
                R.id.profile -> {
                    inflateFragement(ProfileFragment.newInstance())
                }
            }
            true
        }
        binding.bottomNavigation.selectedItemId=R.id.home


    }

    private fun askforPermission() {
        ActivityCompat.requestPermissions(this,permissions,permissionCode)
    }


    private fun inflateFragement(newInstance: Fragment) {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,newInstance)
        transaction.commit()
    }


}

//
//signInRequest = BeginSignInRequest.builder()
//.setGoogleIdTokenRequestOptions(
//BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//.setSupported(true)
//// Your server's client ID, not your Android client ID.
//.setServerClientId(getString(59253384547-1vll7giv65v6g1vs4kjh1ah7qpu7qd04.apps.googleusercontent.com))
//// Only show accounts previously used to sign in.
//.setFilterByAuthorizedAccounts(true)
//.build())
//.build()