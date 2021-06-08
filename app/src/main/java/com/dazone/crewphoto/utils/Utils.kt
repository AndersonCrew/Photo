package com.dazone.crewphoto.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Environment
import com.dazone.crewphoto.base.DazoneApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object Utils {
    fun setServerSite(mDomain: String, userID: String, password: String): String? {
        var domain = mDomain.toLowerCase()
        val domains = domain.split("[.]".toRegex()).toTypedArray()
        if (domain.contains(".bizsw.co.kr") && !domain.contains("8080")) {
            domain = domain.replace(".bizsw.co.kr", ".bizsw.co.kr:8080")
        }

        if (domains.size == 1) {
            domain = domains[0] + ".crewcloud.net"
        }

        if (domain.endsWith("/")) {
            domain = domain.replace("/", "")
        }

        if (domain.startsWith("http://")) {
            domain = domain.replace("http://", "")
        }

        if (domain.startsWith("https://")) {
            domain = domain.replace("https://", "")
        }

        val head = if (DazoneApplication.getInstance().mPref?.getBoolean(Constants.HAS_SSL, false) == true
        ) "https://" else "http://"

        val domainCompany = head + domain
        DazoneApplication.getInstance().mPref?.setString(Constants.DOMAIN, domainCompany)
        DazoneApplication.getInstance().mPref?.setString(Constants.COMPANY_NAME, domain)
        DazoneApplication.getInstance().mPref?.setString(Constants.USER_ID, userID)
        DazoneApplication.getInstance().mPref?.setString(Constants.PASSWORD, password)
        return domainCompany
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

    fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(DazoneApplication.getInstance().applicationContext)

        val directory = cw.getDir("DazoneCrewPhoto", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return directory.absolutePath
    }


}