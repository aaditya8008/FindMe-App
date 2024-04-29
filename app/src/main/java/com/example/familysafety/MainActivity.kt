package com.example.familysafety

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.familysafety.databinding.ActivityMainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //orit
       binding.bottomNavigation.setOnItemSelectedListener {menu->
            if(menu.itemId==R.id.guard) {
                inflateFragement(GuardFragment.newInstance())
            }
           else if(menu.itemId==R.id.home){
                inflateFragement(HomeFragement.newInstance())
           }
           else if(menu.itemId==R.id.dashboard){
                inflateFragement(DashBoardFragment.newInstance())
            }
           else if(menu.itemId==R.id.profile){
                inflateFragement(ProfileFragment.newInstance())
            }


            true
        }


    }



    private fun inflateFragement(newInstance: Fragment) {
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,newInstance)
        transaction.commit()
    }

}