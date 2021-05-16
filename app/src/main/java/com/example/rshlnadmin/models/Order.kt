package com.example.rshlnadmin.models

data class Order(var orderId: String, val products: ArrayList<String> =ArrayList(), val price: Float=0F, val userId: String="", var isDelivered: Boolean=false, var orderStatus: String="", val trackingId: String="", val courierPartner: String="", val date: String="", val paymentMethod: String = "", val paymentId: String="", val isPaid: Boolean = false, val address: Address = Address())
