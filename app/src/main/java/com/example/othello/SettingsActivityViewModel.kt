//Lucas Myers
package com.example.othello

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivityViewModel: ViewModel() {

    private val _sens = MutableLiveData<Float>()
    private val _uid = MutableLiveData<String>()

    val sens: LiveData<Float> get() = _sens
    val uid: LiveData<String> get() = _uid

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun setSens(length: Float?) {
        _sens.value = length
    }

    fun setUid(uid: String) {
        _uid.value = uid
    }

    fun deleteUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.currentUser?.delete()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // User account deleted successfully
                            Log.d("auth", "User account deleted.")
                            db.collection("users").document(_uid.value.toString()).collection("statistics").get()
                                .addOnSuccessListener { statistics ->
                                    for (document in statistics) {
                                        document.reference.delete()
                                    }
                                    db.collection("users").document(_uid.value.toString()).get()
                                        .addOnSuccessListener { user ->
                                            user.reference.delete()
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.w("auth", "Error deleting user documents", task.exception)
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("auth", "Error deleting user documents", task.exception)
                                }
                        } else {
                            Log.w("auth", "Error deleting user account", task.exception)
                        }
                    }
            } catch (e: Exception) {
                print("Could not communicate with Auth")
            }
        }
    }

}