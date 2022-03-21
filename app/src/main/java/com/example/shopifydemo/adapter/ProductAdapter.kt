package com.example.shopifydemo.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopifydemomultipass.MainActivity.Companion.context
import com.example.shopifydemomultipass.R
import com.example.shopifydemomultipass.databinding.ProductItemBinding
import com.shopify.buy3.Storefront

class ProductAdapter(var products: MutableList<Storefront.ProductEdge>) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    private var layoutInflater: LayoutInflater? = null


    class MyViewHolder : RecyclerView.ViewHolder {
        var binding: ProductItemBinding? = null

        constructor(binding: ProductItemBinding) : super(binding.root) {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProductItemBinding>(
            layoutInflater!!,
            R.layout.product_item, parent, false
        )
        return MyViewHolder(binding)


    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding?.tvTitle?.text = products.get(position).node.title
        holder.binding?.tvPrice?.text =
            products.get(position).node.variants.edges.get(0).node.price.toString()
        if(products.get(position).node.images.edges.size!=0)
        {
            Glide.with(context).load(products.get(position).node.images.edges.get(0).node.originalSrc)
                .into(holder?.binding!!.productImg);
        }



    }

    override fun getItemCount(): Int {
        return products.size
    }

}