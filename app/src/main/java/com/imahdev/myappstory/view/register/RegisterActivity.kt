package com.imahdev.myappstory.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.imahdev.myappstory.databinding.ActivityRegisterBinding
import com.imahdev.myappstory.view.ViewModelFactory
import com.imahdev.myappstory.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
            edRegisterEmail.addTextChangedListener(btnEnabled)
            edRegisterName.addTextChangedListener(btnEnabled)
            edRegisterPassword.addTextChangedListener(btnEnabled)
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerViewModel.registerResponse.observe(this) { response ->
            if (response.error) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.btnSignup.setOnClickListener {
            registerViewModel.register(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.tvTitleRegister, View.ALPHA, 1f).setDuration(150)
        val  nameTv= ObjectAnimator.ofFloat(binding.tvNameRegister, View.ALPHA, 1f).setDuration(150)
        val nameEdt = ObjectAnimator.ofFloat(binding.nameEdtLayout, View.ALPHA, 1f).setDuration(150)
        val emailTv = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(150)
        val emailEdt = ObjectAnimator.ofFloat(binding.emailEdtTextLayout, View.ALPHA, 1f).setDuration(150)
        val passwordTv = ObjectAnimator.ofFloat(binding.tvSandi, View.ALPHA, 1f).setDuration(150)
        val passwordEdt = ObjectAnimator.ofFloat(binding.passwordEdtLayout, View.ALPHA, 1f).setDuration(150)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTv,
                nameEdt,
                emailTv,
                emailEdt,
                passwordTv,
                passwordEdt,
                register,
            )
            start()
        }

    }

    private val btnEnabled = object :TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val edtName = binding.edRegisterName.text.toString().trim()
            val edtEmail = binding.edRegisterEmail.text.toString().trim()
            val edtPass = binding.edRegisterPassword.text.toString().trim()

            binding.btnSignup.isEnabled =
                edtName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches() && edtPass.length >= 8
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}