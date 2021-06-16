package hani.momanii.supernova_emoji_library.gif

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author kasmadi
 * @date 15/06/21
 */

class Client{
    fun makeService(): TenorInterface {

        fun getClient(): OkHttpClient {
            return OkHttpClient().newBuilder()
                .build()
        }

        return Retrofit.Builder().baseUrl("https://g.tenor.com/v1/")
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TenorInterface::class.java)
    }
}