package com.example.rshlnadmin.daos

import com.example.rshlnadmin.models.Order
import com.example.rshlnadmin.ui.OrderStatus
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderDao {
    //get the instance of the database
    private val db = FirebaseFirestore.getInstance()
    //get the collection
    val ordersCollection = db.collection("orders")

    fun getOrderById(orderId: String): Task<DocumentSnapshot>{
        return ordersCollection.document(orderId).get()
    }

    fun updateStatus(order: Order){
        GlobalScope.launch {
            ordersCollection.document(order.orderId).set(order)
        }
    }
}