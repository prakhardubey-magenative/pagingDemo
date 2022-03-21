package com.example.shopifydemo

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.shopify.buy3.GraphCallResult

class GraphQLResponse private constructor(val status: Status, @param:Nullable @field:Nullable
val data: GraphCallResult.Success<*>?, @param:Nullable @field:Nullable
                                          val error: GraphCallResult.Failure?) {
    companion object {

        fun success(@NonNull data: GraphCallResult.Success<*>): GraphQLResponse {
            return GraphQLResponse(Status.SUCCESS, data, null)
        }

        fun error(@NonNull error: GraphCallResult.Failure): GraphQLResponse {
            return GraphQLResponse(Status.ERROR, null, error)
        }
    }

}
