package com.example.rshlnadmin

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rshlnadmin.adapters.ProductAdapter
import com.example.rshlnadmin.models.Product

@BindingAdapter("productListData")
fun bindProductRecyclerView(recyclerView: RecyclerView,data: List<Product>?){
    val adapter = recyclerView.adapter as ProductAdapter
    adapter.submitList(data)
}