package com.imahdev.myappstory.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.imahdev.myappstory.databinding.ActivityLoginBinding
import com.imahdev.myappstory.view.ViewModelFactory
import com.imahdev.myappstory.view.main.MainActivity

class LoginActivity : AppCompatActivity() {


    private val loginVIewModel by viewModels<LoginVIewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {

        with(binding) {
            edLoginEmail.addTextChangedListener(btnEnabled)
            edRegisterPassword.addTextChangedListener(btnEnabled)
        }

        loginVIewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginVIewModel.loginResponse.observe(this) { response ->
            if (response.error) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            } else {
                val moveMainActivity = Intent(this, MainActivity::class.java)
                moveMainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                startActivity(moveMainActivity)
                finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            loginVIewModel.login(
                binding.edLoginEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            startDelay = 100
        }.start()

        val titleTv = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(150)
        val descTv = ObjectAnimator.ofFloat(binding.tvDescLogin, View.ALPHA, 1f).setDuration(150)
        val emailTv = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(150)
        val emailEdt = ObjectAnimator.ofFloat(binding.emailEdtTextLayout, View.ALPHA, 1f).setDuration(150)
        val passwordTv = ObjectAnimator.ofFloat(binding.tvSandi, View.ALPHA, 1f).setDuration(150)
        val passwordEdt = ObjectAnimator.ofFloat(binding.passwordEdtLayout, View.ALPHA, 1f).setDuration(150)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                titleTv,
                descTv,
                emailTv,
                emailEdt,
                passwordTv,
                passwordEdt,
                login,
            )
            start()
        }
    }


    private val btnEnabled = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val edtEmail = binding.edLoginEmail.text.toString().trim()
            val edtPass = binding.edRegisterPassword.text.toString().trim()

            binding.btnLogin.isEnabled =
              Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches() && edtPass.length >= 8
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }

    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}