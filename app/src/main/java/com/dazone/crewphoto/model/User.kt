package com.dazone.crewphoto.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "USER")
class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?= null

    @ColumnInfo(name = "userID")
    @SerializedName("userID")
    var userID: String?= null

    @ColumnInfo(name = "MailAddress")
    @SerializedName("MailAddress")
    var email: String?= null

    @ColumnInfo(name = "FullName")
    @SerializedName("FullName")
    var name: String?= null

    @ColumnInfo(name = "session")
    @SerializedName("session")
    var session: String?= null

    @ColumnInfo(name = "avatar")
    @SerializedName("avatar")
    var avatar: String?= null

    @ColumnInfo(name = "permissionType")
    @SerializedName("PermissionType")
    var permissionType: Int?= null

    @ColumnInfo(name = "NameCompany")
    @SerializedName("NameCompany")
    var companyName: String?= null
}