package com.example.rshlnadmin.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.databinding.FragmentProductsBinding
import com.example.rshlnadmin.ui.AddProductFragment
import com.example.rshlnadmin.R
import com.example.rshlnadmin.adapters.IProductAdapter
import com.example.rshlnadmin.adapters.ProductAdapter
import com.example.rshlnadmin.ui.productDetail.ProductDetailFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsFragment : Fragment(), IProductAdapter {

    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var binding: FragmentProductsBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater)

        productsViewModel =
            ViewModelProvider(this).get(ProductsViewModel::class.java)
        binding.viewModel = productsViewModel
        adapter = ProductAdapter(this)
        binding.productsRecyclerView.adapter = adapter

        productsViewModel.products.observe(viewLifecycleOwner,{
            adapter.notifyDataSetChanged()
            binding.productsRecyclerView.smoothScrollToPosition(it.size)
        })

        productsViewModel.navigateToAddProduct.observe(viewLifecycleOwner, {
            //navigate to the add product fragment
            val addProductsFragment = AddProductFragment()
            val currentFragment = this
            requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,addProductsFragment,getString(R.string.title_add_product_fragment)).hide(currentFragment).commit()
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            navView.visibility = View.GONE
            it.and(false)
        })
        val root: View = binding.root
        return root
    }

    override fun onProductClicked(productId: String) {
        //navigate to the product detail fragment
        val currentFragment = this
        val productDetailFragment = ProductDetailFragment(productId)
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main,productDetailFragment,getString(R.string.title_product_detail_fragment)).hide(currentFragment).commit()
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.visibility = View.GONE
    }
}