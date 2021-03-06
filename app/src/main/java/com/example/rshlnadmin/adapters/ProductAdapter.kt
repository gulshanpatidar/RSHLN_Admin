package com.example.rshlnadmin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.rshlnadmin.R
import com.example.rshlnadmin.databinding.ItemProductBinding
import com.example.rshlnadmin.models.Product

class ProductAdapter(private val clickListener: IProductAdapter): ListAdapter<Product,ProductAdapter.ViewHolder?>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.productItem.setOnClickListener{
            clickListener.onProductClicked(product.productId)
        }
    }

    class ViewHolder(private var binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root){
        val productImage: ImageView = binding.productImageInProductFragment
        val productName: TextView = binding.productNameInProductFragment
        val productDescription: TextView = binding.productDescriptionInProductFragment
        val productPrice: TextView = binding.productPriceInProductFragment
        val availabilityLabel: TextView = binding.availabilityLabel
        val productItem: LinearLayout = binding.productItem

        @SuppressLint("ResourceAsColor")
        fun bind(product: Product){
            productImage.load(product.productImage){
                transformations(RoundedCornersTransformation())
            }
            productName.text = product.productName
            productDescription.text = product.description
            val price = product.productPrice.toString()
            if (product.availability){
                productPrice.text = "Price - ???$price"
            }else{
                availabilityLabel.visibility = View.VISIBLE
                productPrice.visibility = View.GONE
                availabilityLabel.text = "Currently Not Available"
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}

interface IProductAdapter{
    fun onProductClicked(productId: String)
}