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
import com.project.lalabib.storiesin.databinding.ActivitySignupBinding
import com.project.lalabib.storiesin.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
        binding.tvLogin.setOnClickListener { moveToLogin() }
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
                        showLoading()
                        signupViewModel.postRegister(name, email, password)
                        showToast()
                        moveActivity()
                    }
                }
            }
        }
    }

    private fun moveToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun moveActivity() {
        signupViewModel.registerResponse.observe(this@SignupActivity) {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun showToast() {
        signupViewModel.txtMsg.observe(this@SignupActivity) {
            it.getContentIfNotHandled()?.let {  txtMsg ->
                Toast.makeText(this, txtMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        signupViewModel.loading.observe(this@SignupActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
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
            playSequentially(title, nameTV, nameEdt, emailTV, emailEdt, passTV, passEdt, signup, login)
            startDelay = 500
        }.start()
    }
}