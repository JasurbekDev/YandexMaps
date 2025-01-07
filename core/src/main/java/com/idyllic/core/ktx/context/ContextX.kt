package com.idyllic.core.ktx.context

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_STREAM
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.idyllic.core.R
import com.idyllic.core.ktx.toast
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels


inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels


inline fun <reified T : Activity> Context.startActivity(vararg pairs: Pair<String, Any?>): Unit =
    startActivity(newIntent<T>(*pairs))

inline fun <reified T : Activity> Context.startActivity(
    flags: Int,
    vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent<T>(flags, *pairs))

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    vararg pairs: Pair<String, Any?>
): Unit = startActivityForResult(newIntent<T>(*pairs), requestCode)

inline fun <reified T : Activity> Context.startActivityNewTask(
    vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent<T>(FLAG_ACTIVITY_NEW_TASK, *pairs))


inline fun <reified T : Activity> Context.startClearActivity(
    vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent<T>(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK, *pairs))


inline fun <reified T : Activity> Context.startClearTopActivity(
    vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent<T>(FLAG_ACTIVITY_CLEAR_TOP, *pairs))


fun Context.startActivity(
    clazzName: String, vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent(clazzName, *pairs))


fun Context.startActivity(
    clazzName: String, flags: Int, vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent(clazzName, flags, *pairs))


fun Context.startClearTopActivity(
    clazzName: String, vararg pairs: Pair<String, Any?>
): Unit = startActivity(newIntent(clazzName, FLAG_ACTIVITY_CLEAR_TOP, *pairs))


@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T> Context.getSystemService(name: String): T {
    return getSystemService(name) as T
}

fun Context.shareText(message: String) {
    val sendIntent: Intent = Intent().apply {
        action = ACTION_SEND
        putExtra(EXTRA_TEXT, message)
        type = "text/plain"
    }

    val shareIntent = createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context.call(phone: String) {
    val intent = Intent(ACTION_CALL, Uri.parse("tel:$phone"))
    startActivity(intent)
}


fun Context.openUrl(url: String) {
    val fullUrl =
        if (!url.startsWith("http://") && !url.startsWith("https://")) "http://$url" else url
    val browserIntent = Intent(ACTION_VIEW, Uri.parse(fullUrl))
    startActivity(browserIntent)
}

private fun copyClipboard(context: Context, text: String) {
    val clipboard: ClipboardManager =
        context.getSystemService<ClipboardManager>(Context.CLIPBOARD_SERVICE)
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}


/**
 * method save file to disc
 * return path file when saved successfully else return null
 */

fun Context.writeResponseBodyToDisk(body: ResponseBody, fileName: String): String? {
    return try {
        val dirPath = this.getExternalFilesDir(null)?.absolutePath ?: return null
        val dir = File(dirPath)
        if (!dir.exists()) dir.mkdirs()

        val fileAddress = dirPath.plus("/").plus(fileName)

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            val fileReader = ByteArray(4096)
            val fileSize: Long = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = FileOutputStream(fileAddress)
            while (true) {
                val read: Int = inputStream.read(fileReader)
                if (read == -1) break
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
            }
            outputStream.flush()
            fileAddress
        } catch (e: IOException) {
            null
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: IOException) {
        null
    }
}

fun Context.startDocActivity(pdfFilePath: String): Unit =
    startFileActivity(pdfFilePath, "application/msword")

fun Context.startPdfActivity(pdfFilePath: String): Unit =
    startFileActivity(pdfFilePath, "application/pdf")

private fun Context.startFileActivity(pdfFilePath: String, type: String) {
    try {
        val intent = Intent(ACTION_VIEW)
        val file = File(pdfFilePath)
        var fileUri = Uri.fromFile(file)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        }
        intent.setDataAndType(fileUri, type)
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    } catch (e: Exception) {
        toast(R.string.cannot_open_file)
    }
}

/**
 * Share PDF file
 *
 */

fun Context.sharePDF(file: File) {
    val intentShareFile = Intent(ACTION_SEND)
    val shareText = getString(R.string.share_file)

    if (file.exists()) {
        intentShareFile.type = "application/pdf"
        intentShareFile.putExtra(EXTRA_STREAM, Uri.parse("file://${file.absoluteFile}"))
        intentShareFile.putExtra(
            EXTRA_SUBJECT,
            shareText
        )
        intentShareFile.putExtra(EXTRA_TEXT, shareText)
        startActivity(createChooser(intentShareFile, shareText))
    }
}