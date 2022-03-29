package com.example.shopifydemo

import PageSource
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.example.shopifydemomultipass.Query
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductVIewModel:ViewModel() {
    var livedata:MutableLiveData<Boolean> = MutableLiveData()
    lateinit var context: Context
    var number = 10
    val getallproducts: Flow<PagingData<Storefront.ProductEdge>> = Pager(config = PagingConfig(4,enablePlaceholders = false)){
        PageSource(number)
    }.flow.cachedIn(viewModelScope)
fun livedata():MutableLiveData<Boolean>
{
    return livedata
}
}