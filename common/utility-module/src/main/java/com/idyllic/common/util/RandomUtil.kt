package com.idyllic.common.util

import kotlin.random.Random

fun getRandomNumberForString(input: String, range: IntRange): Int {
    val seed = input.hashCode()
    val random = Random(seed.toLong())
    return range.random(random)
}