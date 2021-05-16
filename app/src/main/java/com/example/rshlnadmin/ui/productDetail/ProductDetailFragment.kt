package com.example.rshlnadmin.ui.productDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.R
import com.example.rshlnadmin.daos.ProductDao
import com.example.rshlnadmin.databinding.ProductDetailFragmentBinding
import com.example.rshlnadmin.models.Product
import com.example.rshlnadmin.ui.products.ProductsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailFragment(val productId: String) : Fragment() {

    private lateinit var binding: ProductDetailFragmentBinding
    private lateinit var productDao: ProductDao
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductDetailFragmentBinding.inflate(inflater)
        productDao = ProductDao()
        showProductDetails(productId)
        (activity as MainActivity).supportActionBar?.title = "Edit Product Details"
        //set click listener to update the product
        binding.updateProductButton.setOnClickListener {
            updateProduct()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val productsFragment = (activity as MainActivity).activeFragment
                val currentFragment = this@ProductDetailFragment
                requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).show(productsFragment).commit()
                (activity as MainActivity).supportActionBar?.title = "Products"
                val navView = (requireActivity()).findViewById<BottomNavigationView>(R.id.nav_view)
                navView.visibility = View.VISIBLE
            }
        })
    }

    private fun updateProduct() {
        val newPrice = binding.productPriceInDetail.text.toString().toFloat()
        val newAvailability = binding.isProductAvailableInDetail.isChecked
        productDao.updateProduct(product,newPrice,newAvailability)
        exitThisFragment()
    }

    private fun exitThisFragment() {
        val currentFragment = this@ProductDetailFragment
        val oldProductsFragment = (activity as MainActivity).activeFragment
        val newProductFragment = ProductsFragment()
        (activity as MainActivity).activeFragment = newProductFragment
        requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).remove(oldProductsFragment).add(R.id.nav_host_fragment_activity_main,newProductFragment,getString(R.string.products)).show(newProductFragment).commit()
        (activity as MainActivity).supportActionBar?.title = "Products"
    }

    private fun showProductDetails(productId: String) {
        productDao.getProductById(productId).addOnSuccessListener {
            product = it.toObject(Product::class.java)!!
            binding.productImageInDetail.load(product.productImage){
                transformations(RoundedCornersTransformation())
            }
            binding.productNameInDetail.text = product.productName
            binding.productDescriptionInDetail.text = product.description
            binding.productPriceInDetail.setText(product.productPrice.toString())
            binding.isProductAvailableInDetail.isChecked = product.availability
        }
    }

}