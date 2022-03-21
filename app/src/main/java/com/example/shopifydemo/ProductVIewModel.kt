package com.example.shopifydemo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.shopifydemo.adapter.PageSource
import com.example.shopifydemomultipass.Query
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductVIewModel:ViewModel() {

    lateinit var context: Context
    val message = MutableLiveData<String>()
    var edge: List<Storefront.ProductEdge>? = null
    val edgesdata = MutableLiveData<MutableList<Storefront.ProductEdge>>()
    var cursor = "nocursor"
        set(cursor) {
            field = cursor
            Response()
        }
    var number = 10

    fun Response()
    {
//        getallproducts()


    }
    val getallproducts: Flow<PagingData<Storefront.ProductEdge>> = Pager(config = PagingConfig(4,enablePlaceholders = false)){
        PageSource()
    }.flow.cachedIn(viewModelScope)

}