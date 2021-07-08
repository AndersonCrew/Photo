package com.dazone.crewphoto.event

import android.graphics.Bitmap
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.model.FileModel
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.SharePreferencesUtils
import java.io.File

object Event {
    const val GOTO_LOGIN = "GOTO_LOGIN"
    const val GOTO_MAIN = "GOTO_MAIN"
    const val GOTO_IMAGE_DETAIL= "GOTO_IMAGE_DETAIL"
    const val GOTO_IMAGE_CAP = "GOTO_IMAGE_CAP"
    const val GOTO_LIST_DATE = "GOTO_LIST_DATE"
    const val GOTO_DETAIL = "GOTO_DETAIL"
    const val GET_ALL_FILE = "GET_ALL_FILE"
    const val DELETE_IMAGE = "DELETE_IMAGE"

    fun goToLogin() {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_LOGIN to ""))
    }

    fun goToMain() {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_MAIN to ""))
    }

    fun goToImageShow(files: ArrayList<File>) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_IMAGE_DETAIL to files))
    }

    fun goToImageShowCapture(files: ArrayList<File>) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_IMAGE_CAP to files))
    }

    fun goToDetailImage(file: FileModel, list: ArrayList<FileModel>) {
        SharePreferencesUtils(DazoneApplication.getInstance()).setInt(Constants.INDEX_FILE, list.indexOf(file))
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_DETAIL to list))
    }

    fun getAllFile() {
        DazoneApplication.eventBus.onNext(hashMapOf(GET_ALL_FILE to ""))
    }

    fun deleteImage(file: File) {
        DazoneApplication.eventBus.onNext(hashMapOf(DELETE_IMAGE to file))
    }

    fun goToListDate(list: ArrayList<FileModel>) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_LIST_DATE to list))
    }
}