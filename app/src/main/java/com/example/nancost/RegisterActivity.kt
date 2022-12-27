package com.example.nancost

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.nancost.databinding.ActivityRegisterBinding
import com.example.nancost.model.User
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.username.getText().trim()
            val password = binding.password.getText()
            val rePassword = binding.rePassword.getText()
            if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password) && TextUtils.isEmpty(rePassword)
            ) {
                binding.tvErrorRegister.setTextColor(R.color.red)
                binding.tvErrorRegister.text = getString(R.string.str_register_notice_msg)
            } else if (!password.equals(rePassword)) {
                binding.tvErrorRegister.setTextColor(R.color.red)
                binding.tvErrorRegister.text = getString(R.string.str_match_password)
            } else {
                val passHashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())
                val user = User(
                    username = username,
                    password = passHashed,
                    userUid = UUID.randomUUID().toString()
                )
                FirebaseDatabase.getInstance().getReference("hash/login/$username/").setValue(user)
                    .addOnSuccessListener {
                        binding.tvErrorRegister.setTextColor(R.color.black)
                        binding.tvErrorRegister.text = getString(R.string.str_register_notice_msg)
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show()
                    }
            }

        }
    }
}