package com.dazone.crewphoto.base

import com.dazone.crewphoto.database.DazoneDatabase
import com.dazone.crewphoto.retrofit.Result
import retrofit2.Response
import java.lang.Exception

open class BaseRepository {
    var db: DazoneDatabase?= null
    init {
        db = DazoneDatabase.getDatabase(DazoneApplication.getInstance())
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): Result<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
              Result.Success(myResp.body()!!)
            } else {
                /*
              handle standard error codes
              if (myResp.code() == 403){
                  Log.i("responseCode","Authentication failed")
              }
              .
              .
              .
               */

                val error = if (myResp.errorBody()?.string().isNullOrEmpty()) errorMessage else myResp.errorBody()?.string()
                    ?: errorMessage
                Result.Error(error)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val error = if(ex.message.isNullOrEmpty()) errorMessage else ex.message?: errorMessage
            return Result.Error(error)
        }
    }
}