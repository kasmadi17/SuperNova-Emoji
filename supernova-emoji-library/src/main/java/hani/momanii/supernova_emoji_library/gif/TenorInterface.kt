package hani.momanii.supernova_emoji_library.gif


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author kasmadi
 * @date 15/06/21
 */
interface TenorInterface {
    @GET("search")
    fun searchGift(
        @Query("q") q: String,
        @Query("key") key: String = "SGLHRLYSEO8R",
        @Query("locale") locale: String = "id_ID",
        @Query("limit") limit: Int = 5,
    ): Call<Response>

    @GET("trending")
    fun trendingGift(
        @Query("key") key: String = "SGLHRLYSEO8R",
        @Query("locale") locale: String = "id_ID",
        @Query("limit") limit: Int = 5,
    ): Call<Response>
}