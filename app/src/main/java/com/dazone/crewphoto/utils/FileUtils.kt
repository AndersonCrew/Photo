package com.dazone.crewphoto.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class FileUtils(context: Context) {
    private val context: Context = context
    fun getFilePathByUri(uri: Uri): String? {
        var path: String? = null
        // start with file://
        if (ContentResolver.SCHEME_FILE == uri.scheme) {
            path = uri.path
            return path
        }
        // The ones beginning with /storage are also returned directly
        if (isOtherDocument(uri)) {
            path = uri.path
            return path
        }
        // Start with content://, such as content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cursor: Cursor? = context.contentResolver
                .query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val columnIndex: Int =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex)
                    }
                }
                cursor.close()
            }
            if (path != null) return path
        }
        // 4.4 and later start with content://, such as content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT == uri.scheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        path = Environment.getExternalStorageDirectory()
                            .toString() + "/" + split[1]
                        return path
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    //When downloading content providers, you should determine whether the download manager is disabled
                    val stateCode: Int = context.packageManager
                        .getApplicationEnabledSetting("com.android.providers.downloads")
                    if (stateCode != 0 && stateCode != 1) {
                        return null
                    }
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    path = getDataColumn(contentUri, null, null)
                    return path
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs =
                        arrayOf(split[1])
                    path = getDataColumn(contentUri!!, selection, selectionArgs)
                    return path
                }
            }
        }
        return null
    }

    private fun getDataColumn(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } catch (iae: IllegalArgumentException) {
            iae.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isOtherDocument(uri: Uri?): Boolean {
        // The ones beginning with /storage are also returned directly
        if (uri != null && uri.path != null) {
            val path: String = uri.path?: ""
            if (path.startsWith("/storage")) {
                return true
            }
            if (path.startsWith("/external_files")) {
                return true
            }
        }
        return false
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun convertImageUriToFile(imageUri: Uri, activity: Activity): File? {
        var cursor: Cursor? = null //  w  w  w.  ja  va  2 s .  c o m
        return try {
            val proj = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.ORIENTATION
            )
            cursor = activity
                .contentResolver.query(imageUri, proj, null, null, null)
            val fileColumnIndex = cursor
                ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor?.moveToFirst() == true) {
                return File(cursor.getString(fileColumnIndex ?: 0))
            }
            null
        } finally {
            cursor?.close()
        }
    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}