package org.experimentalplayers.faraday.http.resp

import com.google.gson.annotations.SerializedName

data class BaseResponse<T> (
    @SerializedName("ERROR")
    var error: Int,
    @SerializedName("MESSAGE")
    var message: String,
    @SerializedName("RESPONSE")
    var response: T?
) {

}