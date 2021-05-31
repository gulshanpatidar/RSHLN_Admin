package com.example.rshlnadmin.ui.order_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.R
import com.example.rshlnadmin.adapters.SummaryProductAdapter
import com.example.rshlnadmin.databinding.FragmentOrderDetailsBinding
import com.example.rshlnadmin.models.CartItemOffline
import com.example.rshlnadmin.models.Order
import com.example.rshlnadmin.ui.OrderStatus
import com.example.rshlnadmin.ui.orders.OrdersFragment
import com.example.rshlnadmin.ui.update_order.UpdateOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrderDetailsFragment(
    val previousFragment: OrdersFragment,
    val order: Order,
    val cartItemsOffline: ArrayList<CartItemOffline>
) : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var adapter: SummaryProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater)

        (activity as MainActivity).supportActionBar?.title = "Order Details"

        setupRecyclerView()

        binding.updateStatusOrderButton.setOnClickListener {
            updateOrder()
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val currentFragment = this@OrderDetailsFragment
                requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).show(previousFragment).commit()
                (activity as MainActivity).supportActionBar?.title = "Orders"
                val navView = (requireActivity()).findViewById<BottomNavigationView>(R.id.nav_view)
                navView.visibility = View.VISIBLE
            }
        })
    }

    private fun setupRecyclerView() {
        val cart = order.cart
        adapter = SummaryProductAdapter(cartItemsOffline)
        binding.productsRecyclerViewOrder.adapter = adapter
        binding.orderIdOrderDetails.text = "ORDER ID - " + order.orderId
        binding.userIdOrderDetails.text = "USER ID - " + order.userId
        binding.orderedOnOrder.text = "Ordered " + order.orderDate
        binding.orderStatusOrder.text = "Order status - " + order.orderStatus.toString()
        binding.userNameAddressOrder.text = order.address.userName
        binding.phoneNumberAddressOrder.text = order.address.mobileNumber
        binding.houseAndStreetAddressOrder.text =
            order.address.houseNumber + ", " + order.address.streetName
        binding.cityPincodeAddressOrder.text =
            order.address.pincodeOfAddress.toString() + ", " + order.address.city
        binding.totalAmountOrder.text = "₹" + cart.price
    }

    fun updateOrderStatus(status: OrderStatus){
        binding.orderStatusOrder.text = "Order status - " + status.toString()
    }

    private fun updateOrder() {

//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Are you sure you want to cancel order?")
//        //setup the input
//        val input = EditText(requireContext())
//        //specify some of the things
//        input.setHint("Enter the reason")
//        input.inputType = InputType.TYPE_CLASS_TEXT
//        builder.setView(input)
//
//        //setup the buttons
//        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
//            val reason = input.text.toString()
//            order.orderStatus = OrderStatus.CANCELLED
//            OrderDao().updateStatus(order)
//            binding.orderStatusOrder.text = "Order status - " + order.orderStatus.toString()
//        })
//
//        builder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, which ->
//            //do nothing for now
//        })
//
//        builder.show()

        val currentFragment = this
        val updateFragment = UpdateOrderFragment(currentFragment,order)
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,updateFragment,getString(R.string.title_update_order_fragment)).hide(currentFragment).commit()
    }
}