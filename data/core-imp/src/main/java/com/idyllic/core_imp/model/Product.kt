package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.idyllic.core_api.model.ProductDto

@Keep
data class Product(
    @SerializedName("id"                 ) var id                 : Int?    = null,
    @SerializedName("title"              ) var title              : String? = null,
    @SerializedName("description"        ) var description        : String? = null,
    @SerializedName("price"              ) var price              : Int?    = null,
    @SerializedName("discountPercentage" ) var discountPercentage : Double? = null,
    @SerializedName("rating"             ) var rating             : Double? = null,
    @SerializedName("stock"              ) var stock              : Int?    = null,
    @SerializedName("brand"              ) var brand              : String? = null,
    @SerializedName("category"           ) var category           : String? = null,
    @SerializedName("thumbnail"          ) var thumbnail          : String? = null
) {

    constructor(dto: ProductDto) : this(
        dto.id,
        dto.title,
        dto.description,
        dto.price,
        dto.discountPercentage,
        dto.rating,
        dto.stock,
        dto.brand,
        dto.category,
        dto.thumbnail
    )

    fun toDto() = ProductDto(
        id,
        title,
        description,
        price,
        discountPercentage,
        rating,
        stock,
        brand,
        category,
        thumbnail
    )
}