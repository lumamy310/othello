package com.example.othello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginActivityViewModel: ViewModel() {

    private val _uid = MutableLiveData<String>()
    private val _status = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()

    val uid: LiveData<String> get() = _uid
    val status: LiveData<Boolean> get() = _status
    val error: LiveData<String> get() = _error

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(inputEmail: String, inputPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _uid.postValue(auth.uid)
                            _status.postValue(true)
                        } else {
                            _status.postValue(false)
                        }
                    }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }

    }


}