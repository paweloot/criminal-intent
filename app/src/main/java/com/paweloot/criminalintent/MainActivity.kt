package com.paweloot.criminalintent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, CrimeListFragment.newInstance())
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, CrimeDetailsFragment.newInstance(crimeId))
            .addToBackStack(null)
            .commit()
    }
}
