package com.example.rshlnadmin.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.databinding.FragmentOrdersBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.rshlnadmin.R

class OrdersFragment : Fragment() {

    private lateinit var ordersViewModel: OrdersViewModel
    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ordersViewModel =
            ViewModelProvider(this).get(OrdersViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        ordersViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        (activity as MainActivity).supportActionBar?.title = "Orders"

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = (activity as MainActivity).activeFragment
                val ordersFragment = (activity as MainActivity).ordersFragment
                if (currentFragment == ordersFragment){
                    requireActivity().finish()
                }else{
                    requireActivity().supportFragmentManager.beginTransaction().hide(currentFragment).show(ordersFragment).commit()
                    val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
                    navView.selectedItemId = R.id.navigation_orders
                    (activity as MainActivity).supportActionBar?.title = "Orders"
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}