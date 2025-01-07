package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.idyllic.core_api.model.LineDto

@Keep
data class Line (
    @SerializedName("id"          ) var id          : Int?        = null,
    @SerializedName("status"      ) var status      : Int?        = null,
    @SerializedName("fromRegion"  ) var fromRegion  : FromRegion? = FromRegion(),
    @SerializedName("toRegion"    ) var toRegion    : ToRegion?   = ToRegion(),
    @SerializedName("description" ) var description : String?     = null,
    @SerializedName("lineAction"  ) var lineAction  : LineAction? = LineAction(),
    @SerializedName("userCar"     ) var userCar     : UserCar?    = UserCar(),
    @SerializedName("user"        ) var user        : User?       = User()
) {

    constructor(dto: LineDto) : this(
        dto.id,
        dto.status,
        FromRegion(dto.fromRegion),
        ToRegion(dto.toRegion),
        dto.description,
        LineAction(dto.lineAction),
        UserCar(dto.userCar),
        User(dto.user)
    )

    fun toDto() = LineDto(
        id,
        status,
        fromRegion?.toDto(),
        toRegion?.toDto(),
        description,
        lineAction?.toDto(),
        userCar?.toDto(),
        user?.toDto()
    )

    @Keep
    data class FromRegion (
        @SerializedName("id"   ) var id   : Int?    = null,
        @SerializedName("name" ) var name : String? = null
    ) {

        constructor(dto: LineDto.FromRegionDto?) : this(
            dto?.id,
            dto?.name
        )

        fun toDto() = LineDto.FromRegionDto(
            id,
            name
        )

    }

    @Keep
    data class ToRegion (
        @SerializedName("id"   ) var id   : Int?    = null,
        @SerializedName("name" ) var name : String? = null
    ) {

        constructor(dto: LineDto.ToRegionDto?) : this(
            dto?.id,
            dto?.name
        )

        fun toDto() = LineDto.ToRegionDto(
            id,
            name
        )

    }

    @Keep
    data class LineAction (
        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("status"     ) var status    : Int?    = null,
        @SerializedName("start_date" ) var startDate : Int?    = null,
        @SerializedName("end_date"   ) var endDate   : String? = null,
        @SerializedName("mail_price" ) var mailPrice : String? = null,
        @SerializedName("is_mail"    ) var isMail    : Int?    = null,
        @SerializedName("price"      ) var price     : Int?    = null,
        @SerializedName("comment"    ) var comment   : String? = null
    ) {

        constructor(dto: LineDto.LineActionDto?) : this(
            dto?.id,
            dto?.status,
            dto?.startDate,
            dto?.endDate,
            dto?.mailPrice,
            dto?.isMail,
            dto?.price,
            dto?.comment,
        )

        fun toDto() = LineDto.LineActionDto(
            id,
            status,
            startDate,
            endDate,
            mailPrice,
            isMail,
            price,
            comment
        )

    }

    @Keep
    data class UserCar (
        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("status"     ) var status    : Int?    = null,
        @SerializedName("state"      ) var state     : Int?    = null,
        @SerializedName("user_id"    ) var userId    : Int?    = null,
        @SerializedName("car_number" ) var carNumber : String? = null,
        @SerializedName("car"        ) var car       : Car?    = Car(),
        @SerializedName("created_at" ) var createdAt : Int?    = null,
        @SerializedName("updated_at" ) var updatedAt : Int?    = null,
        @SerializedName("height"     ) var height    : String? = null,
        @SerializedName("weight"     ) var weight    : String? = null,
        @SerializedName("length"     ) var length    : String? = null,
        @SerializedName("width"      ) var width     : String? = null
    ) {

        constructor(dto: LineDto.UserCarDto?) : this(
            dto?.id,
            dto?.status,
            dto?.state,
            dto?.userId,
            dto?.carNumber,
            Car(dto?.car),
            dto?.createdAt,
            dto?.updatedAt,
            dto?.height,
            dto?.weight,
            dto?.length,
            dto?.width
        )

        fun toDto() = LineDto.UserCarDto(
            id,
            status,
            state,
            userId,
            carNumber,
            car?.toDto(),
            createdAt,
            updatedAt,
            height,
            weight,
            length,
            width
        )

    }

    @Keep
    data class Car (
        @SerializedName("id"          ) var id         : Int?    = null,
        @SerializedName("title"       ) var title      : String? = null,
        @SerializedName("status"      ) var status     : Int?    = null,
        @SerializedName("capacity"    ) var capacity   : Int?    = null,
        @SerializedName("category_id" ) var categoryId : Int?    = null,
        @SerializedName("type"        ) var type       : Int?    = null,
        @SerializedName("image"       ) var image      : String? = null
    ) {

        constructor(dto: LineDto.CarDto?) : this(
            dto?.id,
            dto?.title,
            dto?.status,
            dto?.capacity,
            dto?.categoryId,
            dto?.type,
            dto?.image
        )

        fun toDto() = LineDto.CarDto(
            id,
            title,
            status,
            capacity,
            categoryId,
            type,
            image
        )

    }

}
