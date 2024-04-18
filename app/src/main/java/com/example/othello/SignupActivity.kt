package com.example.othello

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupActivity : AppCompatActivity() {
    lateinit var viewModel: SignupActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        viewModel = ViewModelProvider(this).get(SignupActivityViewModel::class.java)
        val createBtn = findViewById<Button>(R.id.create_btn)
        val cancelBtn = findViewById<Button>(R.id.cancel_btn)
        val nicknamebox = findViewById<TextInputLayout>(R.id.nick)
        val emailbox = findViewById<TextInputLayout>(R.id.email)
        val passwordbox = findViewById<TextInputLayout>(R.id.password)
        val password2box = findViewById<TextInputLayout>(R.id.password2)
        val nicknameboxinput = findViewById<TextInputEditText>(R.id.nickinput)
        val emailboxinput = findViewById<TextInputEditText>(R.id.emailinput)
        val passwordboxinput = findViewById<TextInputEditText>(R.id.passwordinput)
        val password2boxinput = findViewById<TextInputEditText>(R.id.password2input)


        createBtn.setOnClickListener {
            currentFocus?.let {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(it.windowToken, 0)
            }
            Snackbar.make(
                createBtn,
                "Creating account...",
                Snackbar.LENGTH_LONG
            ).show()
            viewModel.createAcct(emailboxinput.text.toString(), passwordboxinput.text.toString(), nicknameboxinput.text.toString())
        }

        cancelBtn.setOnClickListener {
            finish()
        }

        viewModel.error.observe(this) {
            viewModel.error.value?.let { it1 ->
                Snackbar.make(
                    createBtn,
                    it1,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.status.observe(this) {
            if(viewModel.status.value == true){
                Snackbar.make(
                    createBtn,
                    "Account created",
                    Snackbar.LENGTH_LONG
                ).show()
                val toMain = Intent(this, MainActivity::class.java)
                toMain.putExtra("uid", viewModel.uid.value.toString())
                finish()
                startActivity(toMain)
            }
            else {
                Snackbar.make(
                    createBtn,
                    "Creation failed please recheck inputs",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewModel.dbstatus.observe(this) {
            if(viewModel.status.value == false){
                Snackbar.make(
                    createBtn,
                    "Error communicating with database",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }


        nicknameboxinput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(passwordboxinput.text.toString() != "" && emailboxinput.text.toString() != "" && password2boxinput.text.toString() != "") {
                    if(passwordboxinput.text.toString().length >= 6) {
                        if(passwordboxinput.text.toString() == password2boxinput.text.toString()) {
                            createBtn.isEnabled = true
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        emailboxinput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(passwordboxinput.text.toString() != "" && nicknameboxinput.text.toString() != "" && password2boxinput.text.toString() != "") {
                    if(passwordboxinput.text.toString().length >= 6) {
                        if(passwordboxinput.text.toString() == password2boxinput.text.toString()) {
                            createBtn.isEnabled = true
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        passwordboxinput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(nicknameboxinput.text.toString() != "" && emailboxinput.text.toString() != "" && password2boxinput.text.toString() != "") {
                    if(passwordboxinput.text.toString().length >= 6) {
                        if(passwordboxinput.text.toString() == password2boxinput.text.toString()) {
                            createBtn.isEnabled = true
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        password2boxinput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(passwordboxinput.text.toString() != "" && emailboxinput.text.toString() != "" && nicknameboxinput.text.toString() != "") {
                    if(passwordboxinput.text.toString().length >= 6) {
                        if(passwordboxinput.text.toString() == password2boxinput.text.toString()) {
                            createBtn.isEnabled = true
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })





    }


}