package com.idyllic.core.ktx


fun Long?.toAllSum(): String {
    if (this == null) return "0.00"
    val a = this / 100
    val toSumString = a.toSumString()
    val toFractionalPart = this.toFractionalPart(2)
    return "$toSumString.$toFractionalPart"
}

fun Double?.toAllSum(): String {
    if (this == null) return "0.00"
    val a = (this / 100).toLong()
    val toSumString = a.toSumString()
    val toFractionalPart = this.toFractionalPart(2)
    return "$toSumString.$toFractionalPart"
}

fun Int?.toAllSum(): String {
    if (this == null) return "0.00"
    val a = (this / 100).toLong()
    val toSumString = a.toSumString()
    val toFractionalPart = this.toLong().toFractionalPart(2)
    return "$toSumString.$toFractionalPart"
}
