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

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val rePassword = binding.rePassword.text.toString().trim()
            if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password) && TextUtils.isEmpty(
                    rePassword
                )
            ) {
                Toast.makeText(this, "Hãy nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show()
            } else if (!password.equals(rePassword)) {
                Toast.makeText(this, "2 trường mật khẩu phải khớp nhau!", Toast.LENGTH_SHORT).show()
            } else {
                val passHashed = BCrypt.withDefaults().hashToString(12, password.toCharArray())
                val user = User(username, passHashed)
                FirebaseDatabase.getInstance().getReference("hash/login/$username/").setValue(user)
                    .addOnSuccessListener {
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