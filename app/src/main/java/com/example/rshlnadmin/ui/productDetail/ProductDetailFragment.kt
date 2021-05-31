package com.example.rshlnadmin.ui.productDetail

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_detail,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.menu_delete_product){
            deleteProduct()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteProduct() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.product_delete_warning))
        builder.setPositiveButton("YES",{ dialog,which ->
            productDao.deleteProduct(productId)
            requireActivity().runOnUiThread{
                Toast.makeText(requireContext(),"Product deleted successfully",Toast.LENGTH_SHORT).show()
                exitThisFragment()
            }
        })

        builder.setNegativeButton("NO",{ dialog, which ->

        })

        builder.show()
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