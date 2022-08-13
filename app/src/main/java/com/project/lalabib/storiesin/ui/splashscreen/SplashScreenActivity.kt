package com.project.lalabib.storiesin.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewPropertyAnimator
import com.project.lalabib.storiesin.databinding.ActivitySplashscreenBinding
import com.project.lalabib.storiesin.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val time: Long = 2500
    private val alpha0 = 0f
    private val alpha1 = 1f
    private var propertyAnim: ViewPropertyAnimator? = null

    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSplash.alpha = alpha0
        propertyAnim = binding.ivSplash.animate().setDuration(time).alpha(alpha1).withEndAction {
            Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    override fun onDestroy() {
        propertyAnim?.cancel()
        super.onDestroy()
    }
}