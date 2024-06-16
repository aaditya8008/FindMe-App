
package com.example.familysafety

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.familysafety.databinding.ActivityMainActivityBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainActivityBinding
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_CONTACTS
    )
    private val permissionCode = 78

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        initFirebase()

        // Check and request permissions
        if (isAllPermissionsGranted()) {
            if (isLocationEnabled(this)) {
                setUpLocationListener()
            } else {
                showGPSNotEnabledDialog()
            }
        } else {
            askForPermission()
        }

        setUpBottomNavigation()
    }

    private fun initFirebase() {
        // Initialize Firebase Authentication
        FirebaseAuth.getInstance()
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.guard -> GuardFragment.newInstance()
                R.id.home -> HomeFragement.newInstance()
                R.id.dashboard -> MapsFragment()
                R.id.profile -> ProfileFragment.newInstance()
                else -> throw IllegalArgumentException("Invalid menu item")
            }
            inflateFragment(fragment)
            true
        }
        binding.bottomNavigation.selectedItemId = R.id.home
    }

    private fun inflateFragment(newInstance: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, newInstance)
            .commit()
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showGPSNotEnabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable gps")
            .setMessage("Enable gps")
            .setCancelable(false)
            .setPositiveButton("Enable") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        Toast.makeText(this@MainActivity, "Location: ${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG).show()

                        updateUserLocation(location.latitude, location.longitude)
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun updateUserLocation(latitude: Double, longitude: Double) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()

        val locationData = hashMapOf(
            "lat" to latitude,
            "long" to longitude
        )

        db.collection("users").document(email).update(locationData as Map<String, Any>)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    private fun isAllPermissionsGranted(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }
}








