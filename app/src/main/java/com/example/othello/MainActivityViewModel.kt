package com.example.othello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    private val _uid = MutableLiveData<String>()

    val uid: LiveData<String> get() = _uid




    fun setUid(uid: String) {
        _uid.value = uid
    }

}