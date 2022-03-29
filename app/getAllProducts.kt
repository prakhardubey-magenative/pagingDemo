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