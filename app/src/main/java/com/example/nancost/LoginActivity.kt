package com.example.nancost

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.nancost.databinding.ActivityLoginBinding
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.username.getText()
            val password = binding.password.getText()

            if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                binding.tvErrorLogin.setTextColor(R.color.red)
                binding.tvErrorLogin.text = getString(R.string.str_empty_field)
            }

            if (binding.tvSaveLogin.isChecked) {
                SharedPreUtils.putBoolean(AppConstant.UserLogin.HAS_LOGGED_IN, true)
            }

            Firebase.database.getReference("hash/login/$username")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val passHashed = snapshot.child("password").value.toString()
                            val result =
                                BCrypt.verifyer().verify(password.toCharArray(), passHashed)
                            if (result.verified) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                binding.tvErrorLogin.setTextColor(R.color.black)
                                binding.tvErrorLogin.text = getString(R.string.str_login_notice_msg)
                            } else {
                                binding.tvErrorLogin.setTextColor(R.color.red)
                                binding.tvErrorLogin.text = getString(R.string.str_invalid_username)
                            }
                        } else {
                            binding.tvErrorLogin.setTextColor(R.color.red)
                            binding.tvErrorLogin.text = getString(R.string.str_invalid_password)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}