package com.example.rshlnadmin.ui.update_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.daos.OrderDao
import com.example.rshlnadmin.databinding.FragmentUpdateOrderBinding
import com.example.rshlnadmin.models.Order
import com.example.rshlnadmin.ui.OrderStatus
import com.example.rshlnadmin.ui.order_details.OrderDetailsFragment

class UpdateOrderFragment(val previousFragment: OrderDetailsFragment,val order: Order) : Fragment() {

    private lateinit var binding: FragmentUpdateOrderBinding
    private lateinit var orderDao: OrderDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateOrderBinding.inflate(inflater)
        (activity as MainActivity).supportActionBar?.title = "Update Order Status"
        orderDao = OrderDao()

        if (order.orderStatus==OrderStatus.CANCEL_REQUESTED){
            binding.orderCancelled.visibility = View.VISIBLE
        }

        binding.updateStatusButton.setOnClickListener {
            val selectedStatusId = binding.chooseOrderStatus.checkedRadioButtonId
            val selectedStatus = container?.findViewById<RadioButton>(selectedStatusId)!!
            updateStatus(selectedStatus)
        }

        return binding.root
    }

    private fun updateStatus(selectedStatus: RadioButton) {

        when(selectedStatus){
            binding.orderApproved ->{
                order.orderStatus = OrderStatus.APPROVED
                orderDao.updateStatus(order)
                previousFragment.updateOrderStatus(OrderStatus.APPROVED)
            }
            binding.orderCancelled->{
                order.orderStatus = OrderStatus.CANCELLED
                orderDao.updateStatus(order)
                previousFragment.updateOrderStatus(OrderStatus.CANCELLED)
            }
            binding.orderRejected ->{
                order.orderStatus = OrderStatus.REJECTED
                orderDao.updateStatus(order)
                previousFragment.updateOrderStatus(OrderStatus.REJECTED)
            }
            binding.orderPacked->{
                order.orderStatus = OrderStatus.PACKED
                orderDao.updateStatus(order)
                previousFragment.updateOrderStatus(OrderStatus.PACKED)
            }
            binding.orderShipped->{
                order.orderStatus = OrderStatus.SHIPPED
                orderDao.updateStatus(order)
                previousFragment.updateOrderStatus(OrderStatus.SHIPPED)
            }
            binding.orderDelivered->{
                order.orderStatus = OrderStatus.DELIVERED
                order.isDelivered = true
                orderDao.updateStatus(order)
                previousFragment.updateOrderStatus(OrderStatus.DELIVERED)
            }
        }
        val currentFragment = this@UpdateOrderFragment
        requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).show(previousFragment).commit()
        (activity as MainActivity).supportActionBar?.title = "Order Details"
        Toast.makeText(requireContext(),"Order status updated successfully",Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val currentFragment = this@UpdateOrderFragment
                requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).show(previousFragment).commit()
                (activity as MainActivity).supportActionBar?.title = "Order Details"
            }
        })
    }

}