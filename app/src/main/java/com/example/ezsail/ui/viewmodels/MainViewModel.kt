package com.example.ezsail.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ezsail.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    // Dagger knows how to inject mainRepository although we don't specify that in the module
    val mainRepository: MainRepository
): ViewModel() {
}