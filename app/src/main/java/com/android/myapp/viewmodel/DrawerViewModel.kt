package com.android.myapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawerViewModel : ViewModel() {
    val _openDrawer = MutableLiveData<Boolean>()

    val openDrawer: LiveData<Boolean> get() = _openDrawer

    fun setOpendrawer (isOpen: Boolean){
        _openDrawer.value = isOpen
    }
}