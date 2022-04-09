package com.tegarpenemuan.challengechapter4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tegarpenemuan.challengechapter4.databinding.ActivityRegisterBinding
import com.tegarpenemuan.challengechapter4.helper.Constant
import com.tegarpenemuan.challengechapter4.utils.showCustomToast

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authPreferences = this.getSharedPreferences(Constant.Auth.PREF_AUTH, MODE_PRIVATE)

//        binding.btnRegister.setOnClickListener {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }

        binding.btnRegister.setOnClickListener {
            if (binding.etUsername.text.isEmpty()) {
                binding.etUsername.requestFocus()
                Toast(this).showCustomToast("Username Tidak Boleh Kosong", this)
            } else if (binding.etEmail.text.isEmpty()) {
                binding.etEmail.requestFocus()
                Toast(this).showCustomToast("Email Tidak Boleh Kosong", this)
            } else if (!binding.etEmail.text.matches(Regex("^[a-zA-Z0-9_.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$"))) {
                binding.etEmail.requestFocus()
                Toast(this).showCustomToast("Email Tidak Valid", this)
            } else if (binding.etPassword.text.isEmpty()) {
                binding.etPassword.requestFocus()
                Toast(this).showCustomToast("Password Tidak Boleh Kosong", this)
            } else if (binding.etPassword.text.length < 8) {
                binding.etPassword.requestFocus()
                Toast(this).showCustomToast("Password harus lebih dari 8 karakter", this)
            } else if (!binding.etPassword.text.matches(Regex("(?=.*[a-z])(?=.*[A-Z]).+"))) {
                binding.etPassword.requestFocus()
                Toast(this).showCustomToast(
                    "Password harus mengandung upper case dan lowercase",
                    this
                )
            } else if (binding.etKonfirmasiPassword.text.isEmpty()) {
                binding.etKonfirmasiPassword.requestFocus()
                Toast(this).showCustomToast("Konfirmasi Password Tidak Boleh Kosong", this)
            } else if (!binding.etKonfirmasiPassword.text.toString().equals(binding.etPassword.text.toString())) {
                binding.etKonfirmasiPassword.requestFocus()
                Toast(this).showCustomToast("Konfirmasi Password Tidak Match", this)
            } else if (authPreferences.getString(
                    Constant.Auth.KEY.USERNAME, ""
                ) == binding.etUsername.text.toString()
                || authPreferences.getString(
                    Constant.Auth.KEY.EMAIL,
                    ""
                ) == binding.etEmail.text.toString()
                || authPreferences.getString(
                    Constant.Auth.KEY.PASSWORD,
                    ""
                ) == binding.etKonfirmasiPassword.text.toString()
            ) {
                Toast(this).showCustomToast("Akun Sudah Ada, Silahkan Login", this)
            } else {
                val editor = authPreferences.edit()
                editor.putString(
                    Constant.Auth.KEY.USERNAME,
                    binding.etUsername.text.toString()
                )
                editor.putString(
                    Constant.Auth.KEY.EMAIL,
                    binding.etEmail.text.toString()
                )
                editor.putString(
                    Constant.Auth.KEY.PASSWORD,
                    binding.etKonfirmasiPassword.text.toString()
                )
                editor.putBoolean(Constant.Auth.KEY.LOGIN, false)
                editor.apply()

                Toast(this).showCustomToast("Akun Telah Dibuat", this)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}