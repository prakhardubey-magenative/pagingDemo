import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.bumptech.glide.load.HttpException
import com.example.shopifydemo.*
import com.example.shopifydemo.adapter.Data
import com.example.shopifydemomultipass.MainActivity
import com.example.shopifydemomultipass.Query
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.GraphClient
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.Error
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.io.IOException


    class PageSource(val number: Int) : PagingSource<String, Storefront.ProductEdge>() {
    var edge:MutableList<Storefront.ProductEdge>?=null
    val edgesdata = MutableLiveData<MutableList<Storefront.ProductEdge>>()
    private val DEFAULT_PAGE_INDEX = "nocursor"
    val message = MutableLiveData<String>()
    var previouscursor = "nocursor"
    var nextcursor = "nocursor"
    var cursor = "nocursor"




    override suspend fun load(params: LoadParams<String>):LoadResult<String, Storefront.ProductEdge> {
        val page = params.key ?: DEFAULT_PAGE_INDEX

        return try {
            val urls = Urls()
//            withContext(Dispatchers.Main) {
//                val urls = Urls()
//                async {
//                    getallproducts(urls.graphClient, number)
//                }.await()

//            MainActivity.productlist?.livedata()?.observe(context, Observer { if(it)
//            {
//            }})

         getallproducts(urls.graphClient, number,response = object : Data{
             override fun response(list: MutableList<Storefront.ProductEdge>) {
                 edge=list
             }


         })
//            coroutineScope {
//                delay(2000)
//            }

          LoadResult.Page(
              data = edge.orEmpty(),
                    prevKey = if (page == DEFAULT_PAGE_INDEX) null else previouscursor,
                    nextKey = if (edge==null) null else nextcursor
                )


            }







        catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

     fun getallproducts(graph: GraphClient,number: Int,response : Data) {
        try  {

                previouscursor = cursor
                var call = graph.queryGraph(Query.getAllProducts(cursor,number))

                call.enqueue {

                        result: GraphCallResult<Storefront.QueryRoot> ->

                    val output = (result as GraphCallResult.Success<Storefront.QueryRoot>).response

                        nextcursor =
                            output.data?.products?.edges?.get(output.data?.products?.edges?.size!! - 1)!!.cursor
                        edge = output.data!!.products.edges
                        cursor = nextcursor

                  response.response(edge!!)

            }



        }
        catch (e: Exception) {
            e.printStackTrace()
        }

//        return edge!!

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
                        output.data?.products?.edges?.get(output.data?.products?.edges?.size!! - 1)!!.cursor
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

        val call = urls.graphClient.queryGraph(query)
        call.enqueue { result: GraphCallResult<Storefront.QueryRoot> ->
            customResponse.onSuccessQuery(result)

        }

    }

    override fun getRefreshKey(state: PagingState<String, Storefront.ProductEdge>): String? {
        return null
    }
}
