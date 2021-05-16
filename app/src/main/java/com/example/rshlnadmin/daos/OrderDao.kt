package com.example.rshlnadmin.daos

import com.example.rshlnadmin.models.Order
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

    fun updateStatus(status: String,orderId: String){
        GlobalScope.launch {
            val order = getOrderById(orderId).await().toObject(Order::class.java)!!
            order.orderStatus = status
            ordersCollection.document(orderId).set(order)
        }
    }

    fun orderDelivered(orderId: String){
        GlobalScope.launch {
            val order = getOrderById(orderId).await().toObject(Order::class.java)!!
            order.isDelivered = true
            ordersCollection.document(orderId).set(order)
        }
    }
}