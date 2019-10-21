package com.paweloot.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {

    val crimeListLiveData = CrimeRepository.get().getCrimes()
}