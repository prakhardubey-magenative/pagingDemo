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
import com.example.shopifydemo.ProductVIewModel
import com.example.shopifydemo.adapter.PagingAdapter
import com.example.shopifydemomultipass.databinding.ActivityMainBinding
import com.shopify.buy3.Storefront
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
         var productlist: ProductVIewModel? = null
    }

     var pageadapter= PagingAdapter()


    private var recyclertlist: RecyclerView? = null
    private var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        context = this.applicationContext
        productlist = ViewModelProvider(this).get(ProductVIewModel::class.java)
        productlist?.context = this
        initRecyclerview()
        productlist!!.livedata().observe(this, Observer { consumeResponse(it) })
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

    fun consumeResponse(response:Boolean)
    {

    }

    private fun initRecyclerview() {
        binding?.productrecyclerview?.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = pageadapter
            }
        }
    }


