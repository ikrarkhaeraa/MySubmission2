package com.example.mysubmission1.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.mysubmission1.databinding.ActivityRegisterBinding
import com.example.mysubmission1.main.ModelFactory
import com.example.mysubmission1.main.model.RegisterModel

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val actionBarTitle = "Register Page"
        private const val btnTitle = "Register"
    }

    private lateinit var factory: ModelFactory
    private val model: RegisterModel by viewModels { factory }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ModelFactory.getInstance(this)

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnRegister.text = btnTitle

        clickButton()

    }


    private fun clickButton() {
        binding.apply {
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            btnRegister.setOnClickListener {
                if (name.isEmpty()  && email.isEmpty() && password.isEmpty() && isValidEmail(email)) {
                    edtName.error = FIELD_REQUIRED
                    edtEmail.error = FIELD_IS_NOT_VALID
                    edtPassword.error = FIELD_REQUIRED
                } else {
                    showLoading(true)
                    uploadData()
                    moveToLogin()
                }
            }
        }
    }

    private fun uploadData() {
        binding.apply {
            model.postDataRegister(
                edtName.text.toString(),
                edtEmail.text.toString(),
                edtPassword.text.toString()
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

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun moveToLogin () {
        model.regis.observe(this@RegisterActivity) { response ->
            if (!response.error) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

}