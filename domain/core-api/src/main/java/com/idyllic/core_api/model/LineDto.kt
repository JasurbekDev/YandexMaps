package com.idyllic.core_api.model

data class LineDto (
    var id          : Int?           = null,
    var status      : Int?           = null,
    var fromRegion  : FromRegionDto? = FromRegionDto(),
    var toRegion    : ToRegionDto?   = ToRegionDto(),
    var description : String?        = null,
    var lineAction  : LineActionDto? = LineActionDto(),
    var userCar     : UserCarDto?    = UserCarDto(),
    var user        : UserDto?       = UserDto()
) {

    data class FromRegionDto (
        var id   : Int?    = null,
        var name : String? = null
    )

    data class ToRegionDto (
        var id   : Int?    = null,
        var name : String? = null
    )

    data class LineActionDto (
        var id        : Int?    = null,
        var status    : Int?    = null,
        var startDate : Int?    = null,
        var endDate   : String? = null,
        var mailPrice : String? = null,
        var isMail    : Int?    = null,
        var price     : Int?    = null,
        var comment   : String? = null
    )

    data class UserCarDto (
        var id        : Int?    = null,
        var status    : Int?    = null,
        var state     : Int?    = null,
        var userId    : Int?    = null,
        var carNumber : String? = null,
        var car       : CarDto?    = CarDto(),
        var createdAt : Int?    = null,
        var updatedAt : Int?    = null,
        var height    : String? = null,
        var weight    : String? = null,
        var length    : String? = null,
        var width     : String? = null
    )

    data class CarDto (
        var id         : Int?    = null,
        var title      : String? = null,
        var status     : Int?    = null,
        var capacity   : Int?    = null,
        var categoryId : Int?    = null,
        var type       : Int?    = null,
        var image      : String? = null
    )

}
