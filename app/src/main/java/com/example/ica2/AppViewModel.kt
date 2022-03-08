package com.example.ica2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val locations: MutableLiveData<List<Location>> by lazy {
        loadLocations()
    }

    private fun loadLocations(): MutableLiveData<List<Location>> {
        val currentLocations = listOf(Location("Bahamas"), Location("NUS High School Boarding"))
        return MutableLiveData(currentLocations)
    }
}