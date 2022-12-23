package com.example.nancost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.nancost.databinding.ActivityMainBinding
import com.example.nancost.model.Price
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.navhost) as NavHostFragment? ?: return

        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, appBarConfiguration)

        FirebaseDatabase.getInstance().getReference("price/")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val price = snapshot.getValue(Price:: class.java)
                        price?.newPrice?.let { newPrice ->
                            SharedPreUtils.putInt(AppConstant.Enum.NEW_PRICE, newPrice)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}