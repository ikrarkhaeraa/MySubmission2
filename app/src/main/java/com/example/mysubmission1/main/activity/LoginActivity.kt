package com.example.mysubmission1.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.mysubmission1.databinding.ActivityLoginBinding
import com.example.mysubmission1.main.ModelFactory
import com.example.mysubmission1.main.model.LoginModel
import com.example.mysubmission1.main.model.UserSession

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val actionBarTitle = "Login Page"
        private const val btnTitle = "Login"
    }

    private lateinit var factory: ModelFactory
    private val model: LoginModel by viewModels { factory }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ModelFactory.getInstance(this)

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnLogin.text = btnTitle

        clickButton()

    }

    private fun clickButton() {
        binding.apply {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            btnLogin.setOnClickListener {
                if (email.isEmpty() && password.isEmpty() && isValidEmail(email)) {
                    edtEmail.error = FIELD_IS_NOT_VALID
                    edtPassword.error = FIELD_REQUIRED
                } else {
                    showLoading(true)
                    uploadData()
                    model.userLogin()
                    moveToStory()
                }
            }
        }
    }

    private fun uploadData() {
        binding.apply {
            model.uploadLoginData(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }
        model.login.observe(this@LoginActivity) { response ->
            saveUserSession(
                UserSession(
                    response.loginResult.name,
                    "Bearer " + (response.loginResult.token),
                    true
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun saveUserSession(session: UserSession){
        model.saveUserSession(session)
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun moveToStory () {
        model.login.observe(this@LoginActivity) { response ->
            if (!response.error) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}