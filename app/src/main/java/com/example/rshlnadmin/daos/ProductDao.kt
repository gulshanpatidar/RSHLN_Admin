package com.example.rshlnadmin.daos

import com.example.rshlnadmin.models.Product
import com.example.rshlnadmin.models.Review
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductDao {
    //get the instance of database
    private val db = FirebaseFirestore.getInstance()
    //get/set the collection
    val productsCollection = db.collection("products")

    fun addProduct(product: Product){
        //do this work in background thread
        GlobalScope.launch {
            //add the product to the database and get the id of product
            val documentId = productsCollection.add(product).await().id
            //put this is into the product
            product.productId = documentId
            //again update the product into the database
            productsCollection.document(documentId).set(product)
        }
    }

    fun deleteProduct(productId: String){
        GlobalScope.launch {
            productsCollection.document(productId).delete()
        }
    }

    fun getProductById(productId: String): Task<DocumentSnapshot> {
        return productsCollection.document(productId).get()
    }

    fun updateProduct(product: Product,newPrice: Float,availability: Boolean){
        //do this work in background thread
        GlobalScope.launch {
            //change the price
            product.productPrice = newPrice
            //change the availability
            product.availability = availability
            //update into the database
            productsCollection.document(product.productId).set(product)
        }
    }
}