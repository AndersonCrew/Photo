package com.dazone.crewphoto.event

import android.graphics.Bitmap
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.model.FileModel
import java.io.File

object Event {
    const val GOTO_LOGIN = "GOTO_LOGIN"
    const val GOTO_MAIN = "GOTO_MAIN"
    const val GOTO_IMAGE_SHƠW = "GOTO_IMAGE_SHƠW"
    const val GOTO_IMAGE_SHƠW_CAP = "GOTO_IMAGE_SHƠW_CAP"
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
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_IMAGE_SHƠW to files))
    }

    fun goToImageShowCapture(files: ArrayList<File>) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_IMAGE_SHƠW_CAP to files))
    }

    fun goToDetailImage(file: FileModel) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_DETAIL to file))
    }

    fun getAllFile() {
        DazoneApplication.eventBus.onNext(hashMapOf(GET_ALL_FILE to ""))
    }

    fun deleteImage(file: File) {
        DazoneApplication.eventBus.onNext(hashMapOf(DELETE_IMAGE to file))
    }
}