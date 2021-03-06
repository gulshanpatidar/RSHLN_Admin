package com.example.rshlnadmin.ui.products

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.models.Product
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.coroutineContext

enum class ProductStatus{LOADING,ERROR,DONE}

class ProductsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Products Fragment"
    }
    val text: LiveData<String> = _text

    private val _navigateToAddProduct = MutableLiveData<Boolean>()
    val navigateToAddProduct : LiveData<Boolean>
    get() = _navigateToAddProduct

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
    get() = _products

    private val _status = MutableLiveData<ProductStatus>()
    val status: LiveData<ProductStatus>
    get() = _status

    private var mProducts: ArrayList<Product> = ArrayList()

    init {
        _products.value = mProducts
        retreiveAllProducts()
    }

    fun addProduct(){
        _navigateToAddProduct.value = true
    }

    private fun retreiveAllProducts(){
        val productsCollection = FirebaseFirestore.getInstance().collection("products")
        viewModelScope.launch {
            try {
                val query = productsCollection.get().await()
                val documents = query.documents
                for (document in documents){
                    val product = document.toObject(Product::class.java)!!
                    _text.value = product.productName
                    mProducts.add(product)
                }
            }catch (e: Exception){
                Log.d("CHECKING",e.message.toString())
            }
            withContext(Dispatchers.Main){
                _products.value = mProducts
            }
        }
    }
}