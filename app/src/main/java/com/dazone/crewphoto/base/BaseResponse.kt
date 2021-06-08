package com.dazone.crewphoto.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BaseResponse: Serializable {
    @SerializedName("d")
    val response: Any?= null
}