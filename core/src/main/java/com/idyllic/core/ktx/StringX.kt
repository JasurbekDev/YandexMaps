package com.idyllic.core.ktx

import timber.log.Timber
import java.lang.StringBuilder
import java.util.*
import kotlin.math.pow


val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
val snakeRegex = "_[a-zA-Z]".toRegex()

// String extensions
fun String.camelToSnakeCase(): String {
    return camelRegex.replace(this) {
        "_${it.value}"
    }.lowercase()
}

fun String.snakeToLowerCamelCase(): String {
    return snakeRegex.replace(this) {
        it.value.replace("_", "").uppercase()
    }
}

fun String.snakeToUpperCamelCase(): String {
    return this.snakeToLowerCamelCase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.camelToUpperSnakeCase(): String {
    return this.camelToSnakeCase().uppercase()
}

fun String.substringMaskedPan(): String {
    val start = this.substring(0, 6)
    val end = this.substring(12)
    return start + end
}

fun String?.panOpenUp(): String {
    if (this == null) return ""
    if (this.length != 16) return this
    val thisText = this.replace(" ", "")
    if (thisText.length != 16) return this
    val textNew = StringBuilder()
    thisText.toList().mapIndexed { index, c ->
        if (index % 4 == 0) textNew.append(" ")
        textNew.append(c)
    }
    return textNew.toString().trim()
}

fun String?.panOpenUpWithMasc(): String {
    if (this == null) return ""
    if (this.length != 16) return this
    val thisText = this.replace(" ", "")
    if (thisText.length != 16) return this
    val textNew = StringBuilder()
    thisText.toList().mapIndexed { index, c ->
        if (index % 4 == 0) textNew.append(" ")
        if (index in 6..11) textNew.append("*")
        else textNew.append(c)
    }
    return textNew.toString().trim()
}

fun String?.panOpenUpWithMascFirst(): String {
    if (this == null) return ""
    if (this.length != 16) return this
    val thisText = this.replace(" ", "")
    if (thisText.length != 16) return this
    val substring = thisText.substring(0, 6)
    return substring.plus("**")
}

fun String?.panOpenUpWithMascLast(): String {
    val replaceText = this?.replace(" ", "") ?: return ""
    if (replaceText.length != 16) return this
    val substring = replaceText.substring(12)
    return "**".plus(substring)
}

fun String?.expireFormat(): String {
    if (this == null) return ""
    if (this.length != 4) return this
    return "${this.substring(0, 2)}/${this.substring(2, 4)}"
}

fun Int.toTimer(): String {
    val second = this / 60
    val minute = this % 60
    val secondWithNol = if (minute >= 10) second.toString() else "0".plus(second)
    val minuteWithNol = if (minute >= 10) minute.toString() else "0".plus(minute)
    return secondWithNol.plus(":").plus(minuteWithNol)
}

fun String?.equalsPanMaskedPan(maskedPan: String?): Boolean {
    if (this.isNullOrBlank()) return false
    if (maskedPan.isNullOrBlank()) return false
    val start = this.substring(0, 6) == maskedPan.substring(0, 6)
    val end = this.substring(12) == maskedPan.substring(12)
    return start && end
}

fun String?.toSumString(): String {
    if (this == null) return "0"
    val a = this.reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Long?.toSumString(): String {
    if (this == null) return "0"
    val a = this.toString().reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Double?.toSumString(): String {
    if (this == null) return "0"
    val a = this.toLong().toString().reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Int?.toSumString(): String {
    if (this == null) return "0"
    val a = this.toString().reversed()
    var count = 1
    var b = ""
    a.forEachIndexed { index, c ->
        b += c
        if (count % 3 == 0) {
            b += " "
        }
        count++
    }
    return b.reversed().trim()
}

fun Long?.toFractionalPart(count: Int): String {
    if (this == null) {
        return "00"
    }
    val divSum = 10.0.pow(count.toDouble())
    val textTemp = String.format("%.${count}f", this.div(divSum))
    val f = textTemp.length.minus(count)
    return textTemp.substring(f)
}

fun Double?.toFractionalPart(count: Int): String {
    if (this == null) {
        return "00"
    }
    val divSum = 10.0.pow(count.toDouble())
    val textTemp = String.format("%.${count}f", this.div(divSum))
    val f = textTemp.length.minus(count)
    return textTemp.substring(f)
}


fun Long?.myDiv(other: Long): Long {
    if (this == null) {
        return 0
    }
    return this.div(other)
}


fun Double?.myDiv(other: Long): Double {
    if (this == null) {
        return 0.0
    }
    return this.div(other)
}

fun timber(massage: String, tag: String = "TAG") {
    Timber.d("$tag: $massage")
}

fun String?.cyrillicToLatin(): String? {
    val text = this ?: return null
    val a = hashMapOf(
        "А" to "A",
        "Б" to "B",
        "В" to "V",
        "Г" to "G",
        "Д" to "D",
        "Е" to "E",
        "Ё" to "YO",
        "Ж" to "J",
        "З" to "Z",
        "И" to "I",
        "Й" to "Y",
        "К" to "K",
        "Л" to "L",
        "М" to "M",
        "Н" to "N",
        "О" to "O",
        "П" to "P",
        "Р" to "R",
        "С" to "S",
        "Т" to "T",
        "У" to "U",
        "Ф" to "F",
        "Х" to "X",
        "Ц" to "TS",
        "Ч" to "CH",
        "Ш" to "SH",
        "Щ" to "SH",
        "Э" to "E",
        "Ю" to "YU",
        "Я" to "YA",
        "Қ" to "Q",
        "Ў" to "O'",
        "Ҳ" to "H",
        "Ғ" to "G'",
        "ғ" to "g'",
        "ҳ" to "h",
        "а" to "a",
        "б" to "b",
        "в" to "v",
        "г" to "g",
        "д" to "d",
        "е" to "e",
        "ё" to "yo",
        "ж" to "j",
        "з" to "z",
        "и" to "i",
        "й" to "y",
        "к" to "k",
        "л" to "l",
        "м" to "m",
        "н" to "n",
        "о" to "o",
        "п" to "p",
        "р" to "r",
        "с" to "s",
        "т" to "t",
        "у" to "u",
        "ф" to "f",
        "х" to "x",
        "ц" to "ts",
        "ч" to "ch",
        "ш" to "sh",
        "щ" to "sh",
        "ы" to "iy",
        "э" to "e",
        "ю" to "yu",
        "я" to "ya",
        "қ" to "q",
        "ь" to "ʹ"
    )
    var temp = ""
    text.forEach {
        val s = a[it.toString()]
        if (s != null) temp += s
        else temp += it
    }
    return temp
}