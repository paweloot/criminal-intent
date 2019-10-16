package com.paweloot.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, CrimeFragment())
                .commit()
        }
    }
}
