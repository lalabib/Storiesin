package com.project.lalabib.storiesin.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.project.lalabib.storiesin.R
import com.project.lalabib.storiesin.data.Result
import com.project.lalabib.storiesin.databinding.ActivitySignupBinding
import com.project.lalabib.storiesin.ui.login.LoginActivity
import com.project.lalabib.storiesin.utils.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var factory: ViewModelFactory
    private val signupViewModel: SignupViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        playAnimation()
        binding.tvLogin.setOnClickListener { moveToLogin() }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction() {
        binding.apply {
            btnSignup.setOnClickListener {
                val name = binding.edtName.text.toString()
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                when {
                    name.isEmpty() -> {
                        binding.tfName.error = getString(R.string.cant_empty)
                    }
                    email.isEmpty() -> {
                        binding.tfEmail.error = getString(R.string.cant_empty)
                    }
                    password.isEmpty() -> {
                        binding.tfPassword.error = getString(R.string.cant_empty)
                    }
                    else -> {
                        setupData()
                    }
                }
            }
        }
    }

    private fun setupData() {
        signupViewModel.postRegister(
            binding.edtName.text.toString(),
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString()
        ).observe(this@SignupActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@SignupActivity,
                            result.data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        moveToLogin()
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@SignupActivity,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun moveToLogin() {
        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivSignup, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvSignupMessage, View.ALPHA, 1f).setDuration(500)
        val nameTV = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameEdt = ObjectAnimator.ofFloat(binding.tfName, View.ALPHA, 1f).setDuration(500)
        val emailTV = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdt = ObjectAnimator.ofFloat(binding.tfEmail, View.ALPHA, 1f).setDuration(500)
        val passTV = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passEdt = ObjectAnimator.ofFloat(binding.tfPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTV,
                nameEdt,
                emailTV,
                emailEdt,
                passTV,
                passEdt,
                signup,
                login
            )
            startDelay = 500
        }.start()
    }
}