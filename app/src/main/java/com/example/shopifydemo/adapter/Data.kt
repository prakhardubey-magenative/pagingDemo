package com.example.shopifydemo.adapter

import com.shopify.buy3.Storefront

interface Data {
    fun response(list: MutableList<Storefront.ProductEdge>)
}