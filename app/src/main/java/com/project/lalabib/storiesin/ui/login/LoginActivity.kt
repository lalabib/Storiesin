package com.project.lalabib.storiesin.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.project.lalabib.storiesin.R
import com.project.lalabib.storiesin.data.model.UserModel
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.model.dataStore
import com.project.lalabib.storiesin.databinding.ActivityLoginBinding
import com.project.lalabib.storiesin.ui.main.MainActivity
import com.project.lalabib.storiesin.ui.signup.SignupActivity
import com.project.lalabib.storiesin.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        playAnimation()
        binding.tvSignup.setOnClickListener { moveToSignup() }
    }

    private fun setupViewModel() {
        val pref = UserPreference.getInstance(dataStore)
        loginViewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                when {
                    email.isEmpty() -> {
                        binding.tfEmail.error = getString(R.string.cant_empty)
                    }
                    password.isEmpty() -> {
                        binding.tfPassword.error = getString(R.string.cant_empty)
                    }
                    else -> {
                        showLoading()
                        postLogin()
                        showToast()
                        loginViewModel.login()
                        moveToMain()
                    }
                }
            }
        }
    }

    private fun moveToSignup() {
        startActivity(Intent(this, SignupActivity::class.java))
        finish()
    }

    private fun postLogin() {
        binding.apply {
            loginViewModel.postLogin(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }

        loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
            saveSession(
                UserModel(
                    response.loginResult?.name.toString(),
                    AUTH_KEY + (response.loginResult?.token.toString()),
                    true
                )
            )
        }
    }

    private fun saveSession(user: UserModel) {
        loginViewModel.saveUser(user)
    }

    private fun moveToMain() {
        loginViewModel.loginResponse.observe(this@LoginActivity) {
            if (!it.error) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showToast() {
        loginViewModel.txtMsg.observe(this@LoginActivity) {
            it.getContentIfNotHandled()?.let {  txtMsg ->
                Toast.makeText(this, txtMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        loginViewModel.loading.observe(this@LoginActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginMessage, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(500)
        val emailTV = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdt = ObjectAnimator.ofFloat(binding.tfEmail, View.ALPHA, 1f).setDuration(500)
        val passTV = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passEdt = ObjectAnimator.ofFloat(binding.tfPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, message, emailTV, emailEdt, passTV, passEdt, login, signup)
            startDelay = 500
        }.start()
    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }
}