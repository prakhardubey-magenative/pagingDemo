package com.example.shopifydemo

import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.Storefront

interface CustomResponse {
    fun onSuccessQuery(result: GraphCallResult<Storefront.QueryRoot>) {}
}
