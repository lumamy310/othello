package com.example.othello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupActivityViewModel: ViewModel() {

    private val _uid = MutableLiveData<String>()
    private val _error = MutableLiveData<String>()
    private val _status = MutableLiveData<Boolean>()
    private val _dbstatus = MutableLiveData<Boolean>()

    val uid: LiveData<String> get() = _uid
    val error: LiveData<String> get() = _error
    val status: LiveData<Boolean> get() = _status
    val dbstatus: LiveData<Boolean> get() = _dbstatus

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun createAcct(email: String, password: String, nick: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = hashMapOf(
                                "nickname" to nick,
                                "email" to email,
                                "uid" to auth.uid
                            )
                            db.collection("users").document(auth.uid!!)
                                .set(user)
                                .addOnCompleteListener { task2 ->
                                if (task2.isSuccessful) {
                                    _uid.postValue(auth.uid)
                                    _status.postValue(true)
                                }
                                else {
                                    _dbstatus.postValue(false)
                                }
                            }
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

