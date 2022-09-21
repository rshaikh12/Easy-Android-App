package com.example.easy.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
/**
 * @author Marius Funk
 */
class MainActivityViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return com.example.easy.ui.viewmodels.MainActivityViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
