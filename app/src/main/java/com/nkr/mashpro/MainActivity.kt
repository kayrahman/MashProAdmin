package com.nkr.mashpro

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nkr.mashpro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener
{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // actionBar?.setLogo(R.drawable.mashpro_logo_copy)
      //  supportActionBar?.setLogo(R.drawable.mashpro_logo_copy)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        lifecycleScope.launchWhenResumed {
            navController.addOnDestinationChangedListener(
                this@MainActivity
            )
        }

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.authenticationFragment, R.id.userTypeFragment -> {
                binding.navView.visibility = View.GONE
                if (supportActionBar?.isShowing!!) {
                    supportActionBar?.hide()
                }

            }

            R.id.navigation_home -> {
                binding.navView.visibility = View.VISIBLE
                if (!supportActionBar?.isShowing!!) {
                    supportActionBar?.show()
                }

            }
        }

        }
}