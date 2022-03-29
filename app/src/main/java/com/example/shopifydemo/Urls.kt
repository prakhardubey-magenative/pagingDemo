package com.example.shopifydemo

import android.util.Log
import com.example.shopifydemomultipass.MainActivity
import com.shopify.buy3.GraphClient
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class Urls {

    val shopdomain: String
        get() {
            var domain = "demoshoppingstoresetup.myshopify.com" //magenative-store.myshopify.com
            Log.i("MageNative", "domain" + domain)
            return domain
        }

    val apikey: String
        get() {
            var key = "c7eb6dd49c7e97d327abb9bf616e8692" //63893d2330e639632e2eab540e9d2d75
            Log.i("MageNative", "domain" + key)
            return key
        }
    val graphClient: GraphClient
        get() {

            return GraphClient.build(
                MainActivity.context,
                shopdomain,
                apikey,
            ) {
                httpClient = requestHeader
                httpCache(MainActivity.context.cacheDir) {
                    cacheMaxSizeBytes = 1024 * 1024 * 10
                    //defaultCachePolicy = Constant.policy
                    Unit
                }
                Unit
            }

        }

    internal val requestHeader: OkHttpClient
        get() {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder().build()
                chain.proceed(request)
            }
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)


            return httpClient.build()
        }

}