package com.idyllic.common.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.*
import java.util.Base64

@Suppress("DEPRECATION")
inline fun <reified T : java.io.Serializable> Bundle.customGetSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.customGetParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key) as? T
    }
}

fun serializeToString(obj: Serializable): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ObjectOutputStream(byteArrayOutputStream).use { it.writeObject(obj) }
    return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
}

fun <T : Serializable> deserializeFromString(serializedString: String): T? {
    val byteArray = Base64.getDecoder().decode(serializedString)
    val byteArrayInputStream = ByteArrayInputStream(byteArray)
    return ObjectInputStream(byteArrayInputStream).use { it.readObject() as? T }
}
