package com.example.focusfriend

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager= supportFragmentManager
        val fragmentTransition= fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frameLayout, fragment)
        fragmentTransition.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val bottom=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        replaceFragment(note())
        bottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.note -> replaceFragment(note())
                R.id.timer -> replaceFragment(timer())
                R.id.stopWatch -> replaceFragment(stopWatch())
                R.id.pomodoro -> replaceFragment(pomodoro())
                else ->{

                }
            }
            true
        }

    }
}