package com.dazone.crewphoto.event

import android.graphics.Bitmap
import com.dazone.crewphoto.base.DazoneApplication
import java.io.File

object Event {
    const val GOTO_LOGIN = "GOTO_LOGIN"
    const val GOTO_MAIN = "GOTO_MAIN"
    const val GOTO_IMAGE_SHƠW = "GOTO_IMAGE_SHƠW"
    const val GOTO_DETAIL = "GOTO_DETAIL"
    const val GET_ALL_FILE = "GET_ALL_FILE"

    fun goToLogin() {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_LOGIN to ""))
    }

    fun goToMain() {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_MAIN to ""))
    }

    fun goToImageShow(bitmap: Bitmap) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_IMAGE_SHƠW to bitmap))
    }

    fun goToDetailImage(bitmap: Bitmap) {
        DazoneApplication.eventBus.onNext(hashMapOf(GOTO_DETAIL to bitmap))
    }

    fun getAllFile() {
        DazoneApplication.eventBus.onNext(hashMapOf(GET_ALL_FILE to ""))
    }
}