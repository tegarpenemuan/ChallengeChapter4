package com.tegarpenemuan.challengechapter4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.tegarpenemuan.challengechapter4.databinding.ActivitySplashBinding
import com.tegarpenemuan.challengechapter4.helper.Constant

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        val authPreferences = this.getSharedPreferences(Constant.Auth.PREF_AUTH, MODE_PRIVATE)

        binding.pbSplash.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            if (!authPreferences.contains(Constant.Auth.KEY.USERNAME)) {
                binding.pbSplash.visibility = View.GONE
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (!authPreferences.getBoolean(Constant.Auth.KEY.LOGIN,false)) {
                    binding.pbSplash.visibility = View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.pbSplash.visibility = View.GONE
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}