package com.example.shopifydemomultipass

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.shopifydemo.ProductVIewModel
import com.example.shopifydemo.adapter.PagingAdapter
import com.example.shopifydemo.adapter.ProductAdapter
import com.example.shopifydemomultipass.databinding.ActivityMainBinding
import com.shopify.buy3.Storefront
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

     var pageadapter= PagingAdapter()
    private var productcursor: String? = null
    private var productlist: ProductVIewModel? = null
    lateinit var product_adapter: ProductAdapter
    private var recyclertlist: RecyclerView? = null
    private var binding: ActivityMainBinding? = null

    private var products: MutableList<Storefront.ProductEdge>? = null

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = recyclerView.layoutManager!!.childCount
            val totalItemCount = products?.size
            var firstVisibleItemPosition = 0
            if (recyclerView.layoutManager is LinearLayoutManager) {
                firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            } else if (recyclerView.layoutManager is GridLayoutManager) {
                firstVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
            if (!recyclerView.canScrollVertically(1)) {
                if (products?.size != 49) {
                    productlist!!.number = 10
                    productlist!!.cursor = productcursor!!

                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        context = this.applicationContext
        recyclertlist = setLayout(binding!!.productrecyclerview, "vertical")
        productlist = ViewModelProvider(this).get(ProductVIewModel::class.java)
        productlist?.context = this


        productlist?.Response()




        productlist!!.edgesdata.observe(

            this,
            Observer<MutableList<Storefront.ProductEdge>> { this.productData(it) })
        recyclertlist?.addOnScrollListener(recyclerViewOnScrollListener)
        recyclertlist?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pageadapter
        }
        lifecycleScope.launchWhenStarted {
            productlist!!.getallproducts?.collectLatest { response->
//                binding.apply {
//                    progressBar.isVisible=false
//                    recyclerview.isVisible=true
//                }
                pageadapter.submitData(response)
            }
        }


    }


    private fun productData(products: MutableList<Storefront.ProductEdge>) {
        val Edgesdata = products
//        product_adapter = ProductAdapter(Edgesdata)

//        recyclertlist?.adapter = product_adapter
        if (this.products == null) {
            this.products = products
        } else {
            this.products!!.addAll(products)
            product_adapter.notifyDataSetChanged()
        }
        productcursor = this.products!![this.products!!.size - 1].cursor


    }

    fun setLayout(view: RecyclerView, orientation: String): RecyclerView {
        view.setHasFixedSize(true)
        view.isNestedScrollingEnabled = false

        val manager = LinearLayoutManager(this)
        when (orientation) {
            "horizontal" -> {
                manager.orientation = RecyclerView.HORIZONTAL
                view.layoutManager = manager


            }
            "vertical" -> {
                manager.orientation = RecyclerView.VERTICAL
                view.layoutManager = manager


            }
            "grid" -> {
                view.layoutManager = GridLayoutManager(this, 2)


            }
            "3grid" -> {
                view.layoutManager = GridLayoutManager(this, 3)

            }
            "4grid" -> {
                view.layoutManager = GridLayoutManager(this, 4)


            }
            "customisablegrid" -> {
                view.layoutManager = GridLayoutManager(this, 3)
//                if (view.itemDecorationCount == 0) {
//                    view.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(4), true))
//                }
            }
        }
        return view
    }

}