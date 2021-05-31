package com.example.rshlnadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.rshlnadmin.R
import com.example.rshlnadmin.models.Order
import com.example.rshlnadmin.ui.OrderStatus

class OrderAdapter(
    private val clickListener: IOrderAdapter,
    private val orders: ArrayList<Order>
): RecyclerView.Adapter<OrderAdapter.ViewHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order,context)
        holder.itemOrder.setOnClickListener {
            clickListener.onOrderClicked(order)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemOrder: CardView = itemView.findViewById(R.id.item_order_view)
        val productsName: TextView = itemView.findViewById(R.id.products_name_item_order)
        val orderStatus: TextView = itemView.findViewById(R.id.order_status_item_order)

        fun bind(order: Order, context: Context){
            val cartItems = order.cart.items
            var name = cartItems[0].productName
            for(i in 1..(cartItems.size-1)){
                name += ", " + cartItems[i].productName
            }
            productsName.text = name
            val status = order.orderStatus
            var statusString = ""
            when(status){
                OrderStatus.PLACED ->{
                    statusString = "Order Placed"
                }
                OrderStatus.APPROVED->{
                    statusString = "Order Approved"
                }
                OrderStatus.CANCELLED->{
                    statusString = "Order Cancelled"
                    val color = context.resources.getColor(R.color.design_default_color_error)
                    orderStatus.setTextColor(color)
                }
                OrderStatus.DELIVERED->{
                    statusString = "Order Delivered"
                }
                OrderStatus.PACKED->{
                    statusString = "Order Packed"
                }
                OrderStatus.REJECTED->{
                    statusString = "Order Rejected"
                }
                OrderStatus.RETURNED->{
                    statusString = "Order Returned"
                }
                OrderStatus.SHIPPED->{
                    statusString = "Order Shipped"
                }
                OrderStatus.CANCEL_REQUESTED->{
                    statusString = "Cancellation Requested"
                }
            }
            orderStatus.text = statusString
        }
    }
}

interface IOrderAdapter{
    fun onOrderClicked(order: Order)
}