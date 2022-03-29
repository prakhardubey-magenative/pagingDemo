package com.example.shopifydemomultipass

import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID

object Query {

    val shopDetails: Storefront.QueryRootQuery
        get() = Storefront.query { q ->
            q
                .shop { shop ->
                    shop.name()

                        .paymentSettings { pay ->
                            pay.enabledPresentmentCurrencies().currencyCode()
                        }

                }
//                .products (getData(), productDefinition())

        }

    fun getAllProducts(

        cursor: String,
        number: Int
    ): Storefront.QueryRootQuery {
        val definition: Storefront.QueryRootQuery.ProductsArgumentsDefinition
        if (cursor == "nocursor") {

            definition = Storefront.QueryRootQuery.ProductsArgumentsDefinition { args -> args.first(number).reverse(false)

            }

        } else {

            definition = Storefront.QueryRootQuery.ProductsArgumentsDefinition { args ->
                args.first(number).after(cursor).reverse(false)
            }

        }
        return Storefront.query { root ->
            root.products(
                definition,
                productDefinition()
            )
        }
    }
    fun productDefinition(): Storefront.ProductConnectionQueryDefinition {
        return Storefront.ProductConnectionQueryDefinition { productdata ->
            productdata
                .edges { edges ->
                    edges
                        .cursor()
                        .node { node ->
                            node
                                .title()
                                .images({ img -> img.first(10) }, { imag ->
                                    imag.edges({ imgedge ->
                                        imgedge
                                            .node({ imgnode ->
                                                imgnode
                                                    .originalSrc()
                                                    .transformedSrc({ t ->
                                                        t
                                                            .maxWidth(600)
                                                            .maxHeight(600)
                                                    }
                                                    )
                                            }
                                            )
                                    }
                                    )


                                })
                                .variants({ args ->
                                    args
                                        .first(120)
                                }, { variant ->
                                    variant
                                        .edges({ variantEdgeQuery ->
                                            variantEdgeQuery
                                                .node({ productVariantQuery ->
                                                    productVariantQuery
                                                        .priceV2({ price ->
                                                            price.amount().currencyCode()
                                                        })
                                                        .price()
                                                        .title()
                                                        .quantityAvailable()
                                                }
                                                )
                                        }
                                        )
                                }
                                )

                        }
                }
                .pageInfo(Storefront.PageInfoQueryDefinition { it.hasNextPage() }
                )
        }
    }

}