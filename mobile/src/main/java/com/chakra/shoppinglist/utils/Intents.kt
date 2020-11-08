package com.chakra.shoppinglist.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun takePictureIntent(context: Context, uri: Uri): Intent {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    } else {
        val clip = ClipData.newUri(context.contentResolver, "picture", uri)
        intent.clipData = clip
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
    return intent
}