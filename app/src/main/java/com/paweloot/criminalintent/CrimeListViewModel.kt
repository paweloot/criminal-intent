package com.paweloot.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {

    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 100) {
            crimes.add(
                Crime(
                    title = "Crime #$i",
                    isSolved = i % 2 == 0
                )
            )
        }
    }
}