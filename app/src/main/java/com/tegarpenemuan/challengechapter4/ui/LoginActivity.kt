package com.tegarpenemuan.challengechapter4.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tegarpenemuan.challengechapter4.databinding.ActivityLoginBinding
import com.tegarpenemuan.challengechapter4.helper.Constant
import com.tegarpenemuan.challengechapter4.utils.showCustomToast


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authPreferences = this.getSharedPreferences(Constant.Auth.PREF_AUTH, MODE_PRIVATE)

        if (authPreferences.getBoolean(Constant.Auth.KEY.LOGIN,false)) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etUsername.text.isEmpty()) {
                binding.etUsername.requestFocus()
                Toast(this).showCustomToast("Email tidak boleh kosong", this)
            } else if (binding.etPassword.text.isEmpty()) {
                binding.etPassword.requestFocus()
                Toast(this).showCustomToast("Password tidak boleh kosong", this)
            } else if (binding.etPassword.text.length < 8) {
                binding.etPassword.requestFocus()
                Toast(this).showCustomToast("Password harus lebih dari 8 karakter", this)
            } else if (!binding.etPassword.text.matches(Regex("(?=.*[a-z])(?=.*[A-Z]).+"))) {
                binding.etPassword.requestFocus()
                Toast(this).showCustomToast(
                    "Password harus mengandung upper case dan lowercase",
                    this
                )
            } else {
                val usernamePreferences =
                    authPreferences.getString(Constant.Auth.KEY.USERNAME, "")
                val passwordPreferences =
                    authPreferences.getString(Constant.Auth.KEY.PASSWORD, "")
                if (binding.etUsername.text.toString() == usernamePreferences && binding.etPassword.text.toString() == passwordPreferences) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast(this).showCustomToast("Anda Berhasil Login", this)
                    finish()


                    val isLogin = authPreferences.edit()
                    isLogin.putBoolean(Constant.Auth.KEY.LOGIN, true)
                    isLogin.apply()
                    finish()
                } else {
                    Toast(this).showCustomToast("Anda Belum Memiliki Akun", this)
                }
            }
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Apakah Anda ingin keluar dari aplikasi ?")
            .setCancelable(false)
            .setPositiveButton("Iya",
                DialogInterface.OnClickListener { dialog, id -> super.onBackPressed() })
            .setNegativeButton("Tidak", null)
            .show()
    }
}