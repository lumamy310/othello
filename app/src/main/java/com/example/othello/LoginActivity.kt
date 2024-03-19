package com.example.othello

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        val loginBtn = findViewById<Button>(R.id.login)
        val signUpBtn = findViewById<Button>(R.id.newacct)
        val author = findViewById<TextView>(R.id.nameText)
        val email = findViewById<TextInputLayout>(R.id.email)
        val pass = findViewById<TextInputLayout>(R.id.pass)
        val emailInput = findViewById<TextInputEditText>(R.id.emailinput)
        val passInput = findViewById<TextInputEditText>(R.id.passinput)

        loginBtn.setOnClickListener {
            currentFocus?.let {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(it.windowToken, 0)
            }
            Snackbar.make(
                author,
                "Logging in...",
                Snackbar.LENGTH_LONG
            ).show()
            viewModel.login(emailInput.text.toString(), passInput.text.toString())
        }

        signUpBtn.setOnClickListener {
            val tosignUp = Intent(this, SignupActivity::class.java)
            startActivity(tosignUp)
        }

        viewModel.status.observe(this) {
            if(viewModel.status.value == true){
                Snackbar.make(
                    author,
                    "Logged in",
                    Snackbar.LENGTH_LONG
                ).show()
                val toMain = Intent(this, MainActivity::class.java)
                toMain.putExtra("uid", viewModel.uid.value.toString())
                startActivity(toMain)
            }
            else {
                Snackbar.make(
                    author,
                    "Login failed please recheck username and password",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.error.observe(this) {
            viewModel.error.value?.let { it1 ->
                Snackbar.make(
                    author,
                    it1,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        passInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(emailInput.text.toString() != "") {
                    loginBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(passInput.text.toString() != "") {
                    loginBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })


    }

}