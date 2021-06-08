package com.dazone.crewphoto.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dazone.crewphoto.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserData(user: User)
}