package org.experimentalplayers.faraday.http

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import getConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.experimentalplayers.faraday.R
import org.experimentalplayers.faraday.http.resp.BaseResponse
import org.experimentalplayers.faraday.utils.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClient {

    companion object {

        private const val TIMEOUT = 30L

        private object GetInstance {
            val INSTANCE = create()
        }

        val instance: HttpClient by lazy { GetInstance.INSTANCE }
        private lateinit var httpServiceInterface: HttpInterface

        private fun create(): HttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .apply {
                    if(BuildConfig.DEBUG)
                        addInterceptor(ChuckerInterceptor.Builder(MyApplication.instance!!).build())
                }
                .build()

            val url: String = MyApplication.instance!!.getConfig("url", R.raw.config) ?: ""

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .baseUrl("$url/")
                .build()

            httpServiceInterface = retrofit.create(HttpInterface::class.java)

            return HttpClient()
        }

    }

}

fun <T> Call<BaseResponse<T>>.enqueue(callback: (success: Boolean, result: T?, message: String?, error: Int?) -> Unit) {
    this.enqueue(object : Callback<BaseResponse<T>> {
        override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
            t.printStackTrace()
            callback.invoke(false, null, null, null)
        }

        override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
            if(response.isSuccessful && response.body() !== null) {
                if(response.body()?.error == 0) {
                    callback.invoke(true, response.body()?.response, response.body()?.message, response.body()?.error)
                } else {
                    callback.invoke(false, null, response.body()?.message, response.body()?.error)
                }
            } else {
                callback.invoke(false, null, null, null)
            }
        }
    })
}

