package com.example.rshlnadmin.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.R
import com.example.rshlnadmin.adapters.IOrderAdapter
import com.example.rshlnadmin.adapters.OrderAdapter
import com.example.rshlnadmin.daos.OrderDao
import com.example.rshlnadmin.daos.ProductDao
import com.example.rshlnadmin.databinding.FragmentOrdersBinding
import com.example.rshlnadmin.models.CartItemOffline
import com.example.rshlnadmin.models.Order
import com.example.rshlnadmin.models.Product
import com.example.rshlnadmin.ui.order_details.OrderDetailsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrdersFragment : Fragment(), IOrderAdapter {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var orderDao: OrderDao
    private lateinit var productDao: ProductDao
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        orderDao = OrderDao()
        productDao = ProductDao()
        setupRecyclerView()

        (activity as MainActivity).supportActionBar?.title = "Orders"

        return binding.root
    }

    fun setupRecyclerView() {
        GlobalScope.launch {
            val orderCollection = FirebaseFirestore.getInstance().collection("orders")
            val query =
                orderCollection.orderBy("createdAt", Query.Direction.DESCENDING).get().await()
            val documents = query.documents
            val orders = ArrayList<Order>()
            for (document in documents) {
                val order = document.toObject(Order::class.java)!!
                orders.add(order)
            }
            withContext(Dispatchers.Main) {
                adapter = OrderAdapter(this@OrdersFragment, orders)
                binding.ordersRecyclerView.adapter = adapter
                binding.ordersFragmentProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentFragment = (activity as MainActivity).activeFragment
                    val ordersFragment = (activity as MainActivity).ordersFragment
                    if (currentFragment == ordersFragment) {
                        requireActivity().finish()
                    } else {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .hide(currentFragment).show(ordersFragment).commit()
                        val navView =
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
                        navView.selectedItemId = R.id.navigation_orders
                        (activity as MainActivity).supportActionBar?.title = "Orders"
                    }
                }
            })
    }

    override fun onOrderClicked(order: Order) {
        val items = order.cart.items
        GlobalScope.launch {
            val cartItemsOffline = ArrayList<CartItemOffline>()
            for (i in 0..(items.size - 1)) {
                val item = items[i]
                val product = productDao.getProductById(item.productId).await()
                    .toObject(Product::class.java)!!
                val cartItemOffline = CartItemOffline(item.productId, item.quantity, product)
                cartItemsOffline.add(cartItemOffline)
            }
            withContext(Dispatchers.Main) {
                val currentFragment = this@OrdersFragment
                val orderDetailsFragment = OrderDetailsFragment(currentFragment, order,cartItemsOffline)
                requireActivity().supportFragmentManager.beginTransaction().add(
                    R.id.nav_host_fragment_activity_main,
                    orderDetailsFragment,
                    getString(R.string.title_order_detail_fragment)
                ).hide(currentFragment).commit()
                val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
                navView.visibility = View.GONE
            }
        }
    }
}