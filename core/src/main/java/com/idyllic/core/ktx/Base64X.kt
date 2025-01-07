package com.idyllic.core.ktx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException

fun String.convertBase64ToBitmap(): Bitmap {
    val imageAsBytes: ByteArray = Base64.decode(this.toByteArray(), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun Bitmap.convertBitmapToBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun String.convertBase64ToPdfFileOrNull(
    context: Context
): File? = try {
    val pdfAsBytes: ByteArray = Base64.decode(this.toByteArray(), Base64.DEFAULT)

    val dirPath = context.getExternalFilesDir(null)!!.absolutePath
    val dir = File(dirPath)
    if (!dir.exists()) dir.mkdirs()

    val fileAddress = dirPath.plus("/share.pdf")
    val pdf = File(fileAddress)

    val fop = FileOutputStream(pdf)

    fop.write(pdfAsBytes)
    fop.flush()
    fop.close()

    pdf
} catch (e: Exception) {
    null
}

fun String.convertBase64ToPdfFileOrNull(
    fileName: String,
    context: Context
): File? = try {
    val pdfAsBytes: ByteArray = Base64.decode(this.toByteArray(), Base64.DEFAULT)

    val dirPath = context.getExternalFilesDir(null)!!.absolutePath
    val dir = File(dirPath)
    if (!dir.exists()) dir.mkdirs()

    val fileAddress = dirPath.plus("/${fileName}.pdf")
    val pdf = File(fileAddress)

    val fop = FileOutputStream(pdf)

    fop.write(pdfAsBytes)
    fop.flush()
    fop.close()
    Timber.d("eeeL : $pdf")
    pdf
} catch (e: Exception) {
    Timber.d("eeeL : $e")
    null
}

fun String.base64Decode(): String {
    return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
}

fun String.base64Encode(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
}

