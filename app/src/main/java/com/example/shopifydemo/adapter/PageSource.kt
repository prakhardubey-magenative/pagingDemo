package com.example.shopifydemo.adapter

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.example.shopifydemo.CustomResponse
import com.example.shopifydemo.GraphQLResponse
import com.example.shopifydemo.Status
import com.example.shopifydemo.Urls
import com.example.shopifydemomultipass.Query
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.GraphClient

import com.shopify.buy3.Storefront
import com.shopify.graphql.support.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

class PageSource
constructor() : PagingSource<String,Storefront.ProductEdge>() {
    var edge: List<Storefront.ProductEdge>? = null
    val edgesdata = MutableLiveData<MutableList<Storefront.ProductEdge>>()
    private val DEFAULT_PAGE_INDEX= "nocursor"
    val message = MutableLiveData<String>()
    var previouscursor="nocursor"
    var nextcursor="nocursor"
var cursor="nocursor"
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Storefront.ProductEdge> {
        val page = params.key ?: DEFAULT_PAGE_INDEX

        return try {
            val urls= Urls()
            getallproducts(urls.graphClient)
//                doGraphQLQueryGraph(
//                    urls,
//                    Query.getAllProducts(cursor),
//                    customResponse = object : CustomResponse {
//                        override fun onSuccessQuery(result: GraphCallResult<Storefront.QueryRoot>) {
//                            invoke(result)
//                        }
//                    }
//                )

                LoadResult.Page(edge!!, prevKey = if(page == DEFAULT_PAGE_INDEX) null else "page-1", nextKey = if(edge!!.isEmpty()) null else page+1)




        } catch (exception:IOException){
            LoadResult.Error(exception)
        } catch (exception: HttpException){
            LoadResult.Error(exception)
        }
    }
    fun getallproducts(graph:GraphClient): List<Storefront.ProductEdge>? {
        try {
            previouscursor = cursor
            var call = graph.queryGraph(Query.getAllProducts(cursor))
//
//                call.enqueue(Handler(Looper.getMainLooper())) { result: GraphCallResult<Storefront.QueryRoot> -> this.invoke(result) }

            call.enqueue(Handler(Looper.getMainLooper())) { result: GraphCallResult<Storefront.QueryRoot> ->
                val output = (result as GraphCallResult.Success<Storefront.QueryRoot>).response
                nextcursor =
                    output.data?.products?.edges?.get(output!!.data?.products?.edges?.size!! - 1)!!.cursor
                edge = output.data!!.products.edges
                cursor = nextcursor
         }


        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        return edge
    }



    private operator fun invoke(result: GraphCallResult<Storefront.QueryRoot>) {
        if (result is GraphCallResult.Success<*>) {
            consumeResponse(GraphQLResponse.success(result as GraphCallResult.Success<*>))
        } else {
            consumeResponse(GraphQLResponse.error(result as GraphCallResult.Failure))
        }
        return
    }

    private fun consumeResponse(reponse: GraphQLResponse) {
        when (reponse.status) {
            Status.SUCCESS -> {
                val result =
                    (reponse.data as GraphCallResult.Success<Storefront.QueryRoot>).response
                if (result.hasErrors) {
                    val errors = result.errors
                    val iterator = errors.iterator()

                    val errormessage = StringBuilder()
                    var error: Error? = null
                    while (iterator.hasNext()) {
                        error = iterator.next()
                        errormessage.append(error.message())
                    }
                    message.setValue(errormessage.toString())
                } else {

//                    edge = result.data!!.products.edges
//                    edgesdata.setValue(edge!!.toMutableList())
//                    var itr = edge?.listIterator()
                    val output = (result as GraphCallResult.Success<Storefront.QueryRoot>).response
                    nextcursor =
                        output.data?.products?.edges?.get(output!!.data?.products?.edges?.size!! - 1)!!.cursor
                    edge = output.data!!.products.edges
                    cursor = nextcursor


                }
            }
            Status.ERROR -> message.setValue(reponse.error!!.error.message)
            else -> {
            }
        }
    }



    fun doGraphQLQueryGraph(
        urls: Urls,
        query: Storefront.QueryRootQuery,
        customResponse: CustomResponse
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val call = urls.graphClient.queryGraph(query)
            call.enqueue { result: GraphCallResult<Storefront.QueryRoot> ->
                GlobalScope.launch(Dispatchers.Main) {
                    customResponse.onSuccessQuery(result)
                }
            }
        }
    }
    override fun getRefreshKey(state: PagingState<String, Storefront.ProductEdge>): String? {
     return null
    }
}
