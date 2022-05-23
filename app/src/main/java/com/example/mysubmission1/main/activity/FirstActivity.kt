package com.example.mysubmission1.main.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mysubmission1.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story App"

        chooseButton()
        playAnimation()
    }

    private fun chooseButton() {
        binding.apply {
            btnLogin.setOnClickListener {
                val intentToLogin = Intent(this@FirstActivity, LoginActivity::class.java)
                startActivity(intentToLogin)
            }
            btnRegister.setOnClickListener {
                val intentToRegister = Intent(this@FirstActivity, RegisterActivity::class.java)
                startActivity(intentToRegister)
            }
        }
    }

    private fun playAnimation() {

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val regis = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(500)


        val together = AnimatorSet().apply {
            playTogether(login, regis)
        }


        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }

}