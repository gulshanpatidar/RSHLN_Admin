package com.example.rshlnadmin

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rshlnadmin.databinding.ActivityMainBinding
import com.example.rshlnadmin.ui.notifications.NotificationsFragment
import com.example.rshlnadmin.ui.orders.OrdersFragment
import com.example.rshlnadmin.ui.products.ProductsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val ordersFragment = OrdersFragment()
    val productsFragment = ProductsFragment()
    val notificationsFragment = NotificationsFragment()
    private val fragmentManager = supportFragmentManager
    var activeFragment: Fragment = ordersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        fragmentManager.beginTransaction().apply {
            add(R.id.nav_host_fragment_activity_main,notificationsFragment,getString(R.string.title_notifications)).hide(notificationsFragment)
            add(R.id.nav_host_fragment_activity_main,productsFragment,getString(R.string.products)).hide(productsFragment)
            add(R.id.nav_host_fragment_activity_main,ordersFragment,getString(R.string.orders))
        }.commit()

        navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_orders->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(ordersFragment).commit()
                    activeFragment = ordersFragment
                    supportActionBar?.title = "Orders"
                    true
                }
                R.id.navigation_products->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(productsFragment).commit()
                    activeFragment = productsFragment
                    supportActionBar?.title = "Products"
                    true
                }
                R.id.navigation_notifications->{
                    fragmentManager.beginTransaction().hide(activeFragment).show(notificationsFragment).commit()
                    activeFragment = notificationsFragment
                    supportActionBar?.title = "Notifications"
                    true
                }
                else -> false
            }
        }

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_orders, R.id.navigation_products, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)


    }
}