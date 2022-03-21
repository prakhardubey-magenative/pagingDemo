package com.example.shopifydemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopifydemomultipass.MainActivity
import com.example.shopifydemomultipass.databinding.ProductItemBinding
import com.shopify.buy3.Storefront
import javax.inject.Inject


class PagingAdapter @Inject constructor(): PagingDataAdapter<Storefront.ProductEdge,PagingAdapter.DogsViewHolder>(Diff()) {


    override fun onBindViewHolder(holder: DogsViewHolder, position: Int) {
        val product = getItem(position)
        if(product!= null){
            holder.binds(product)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsViewHolder  =
        DogsViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    class DogsViewHolder(private val binding:ProductItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun binds(products: Storefront.ProductEdge){

                Glide.with(MainActivity.context).load(products.node.images.edges.get(0).node.originalSrc)
                    .into(binding!!.productImg);

        }



    }

    class Diff : DiffUtil.ItemCallback<Storefront.ProductEdge>(){
        override fun areItemsTheSame(
            oldItem: Storefront.ProductEdge,
            newItem: Storefront.ProductEdge
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: Storefront.ProductEdge,
            newItem: Storefront.ProductEdge
        ): Boolean {
            TODO("Not yet implemented")
        }
//        override fun areItemsTheSame(oldItem: Storefront.ProductEdge, newItem: Storefront.ProductEdge): Boolean  =
//            oldItem.url == newItem.url
//
//        override fun areContentsTheSame(oldItem: Storefront.ProductEdge, newItem: Storefront.ProductEdge): Boolean =
//            oldItem == newItem
    }
}