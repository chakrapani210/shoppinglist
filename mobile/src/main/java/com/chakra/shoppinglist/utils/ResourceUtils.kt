package com.chakra.shoppinglist.utils

import android.content.Context
import android.net.Uri
import android.support.v4.content.FileProvider
import com.chakra.shoppinglist.BuildConfig
import java.io.File
import java.io.FileOutputStream

object ResourceUtils {
    private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider"
    @JvmStatic
    @Throws(Exception::class)
    fun uri(context: Context): Uri {
        return FileProvider.getUriForFile(context, AUTHORITY, newFile(context.cacheDir))
    }

    @JvmStatic
    @Throws(Exception::class)
    fun fileFromUri(context: Context, uri: Uri?): File {
        val file = newFile(context.filesDir)
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    @JvmStatic
    @Throws(Exception::class)
    fun createFile(context: Context, content: ByteArray?): File {
        val file = newFile(context.filesDir)
        FileOutputStream(file).use { fos ->
            fos.write(content)
            fos.close()
        }
        return file
    }

    @Throws(Exception::class)
    private fun newFile(parent: File): File {
        return File.createTempFile("image", "", parent)
    }
}