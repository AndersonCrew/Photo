package com.dazone.crewphoto.model

import com.google.gson.annotations.SerializedName

class FileModel {
    @SerializedName("url")
    var url : String?= null

    @SerializedName("Uname")
    var name : String?= null

    @SerializedName("lDate")
    var time : Long = 0
}