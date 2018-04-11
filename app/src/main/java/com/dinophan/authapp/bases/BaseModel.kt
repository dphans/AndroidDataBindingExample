package com.dinophan.authapp.bases

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Suppress("MemberVisibilityCanBePrivate", "RedundantVisibilityModifier")
abstract class BaseModel: Serializable {

    @SerializedName("_id")
    var _id: Long               = -1

    @SerializedName("_created_at")
    var _created_at: Long       = System.currentTimeMillis()

    @SerializedName("_updated_at")
    var _updated_at: Long       = System.currentTimeMillis()

    @Expose
    var errorMessage: String?       = null

    open fun validates(): Boolean   = true

    fun toJson(): String = try {
        Gson().toJson(this@BaseModel)
    } catch (jsonParsingException: Exception) {
        this@BaseModel.errorMessage = jsonParsingException.localizedMessage
        "{}"
    }

}