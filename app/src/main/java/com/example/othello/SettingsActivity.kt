//Lucas Myers
package com.example.othello

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {
    lateinit var viewModel: SettingsActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(this).get(SettingsActivityViewModel::class.java)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val setSensBtn = findViewById<Button>(R.id.setSens)
        val deleteBtn = findViewById<Button>(R.id.deletebtn)
        val sensInput = findViewById<TextInputEditText>(R.id.sensInput)

        backBtn.setOnClickListener {
            if(viewModel.sens.value!! > 200 || viewModel.sens.value!! < 0){
                Snackbar.make(
                    backBtn,
                    "Sensitivity must be between 1 and 200",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else{
                val toMain = Intent(this, MainActivity::class.java)
                toMain.putExtra("sens", viewModel.sens.value)
                setResult(1, toMain)
                finish()
            }
        }

        setSensBtn.setOnClickListener {
            currentFocus?.let {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(it.windowToken, 0)
            }
            if(sensInput.text.toString().toFloat() < 200 || sensInput.text.toString().toFloat() > 1) {
                viewModel.setSens(sensInput.text.toString().toFloat())
                Snackbar.make(
                    backBtn,
                    "Sensitivity set to ${sensInput.text.toString().toFloat()}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else {
                Snackbar.make(
                    backBtn,
                    "Sensitivity must be between 1 and 100",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        deleteBtn.setOnClickListener {
            val deleteHandler = Handler(Looper.getMainLooper())
            deleteHandler.postDelayed({
                viewModel.deleteUser()
                val toMain = Intent(this, MainActivity::class.java)
                setResult(2, toMain)
                finish()
            }, 5000)
            val msg = "Your account and all data will be deleted in 5 seconds"
            Snackbar.make(deleteBtn, msg, 5000)
                .setAction("Undo") {
                    deleteHandler.removeCallbacksAndMessages(null)
                }
                .show()
        }

    }

    override fun onResume() {
        super.onResume()
        val sens = intent.getFloatExtra("sens", 50.0f)
        val uid = intent.getStringExtra("uid")
        viewModel.setSens(sens)
        viewModel.setUid(uid!!)
    }

}