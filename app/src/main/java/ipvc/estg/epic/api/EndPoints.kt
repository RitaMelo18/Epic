package ipvc.estg.epic.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/myslim2/api/utilizador")
    fun postTest(@Field("email") first: String): Call<utilizador>

    @FormUrlEncoded
    @POST("/myslim2/api/utilizadorAll")
    fun getUtlAll(@Field("id") first: Int): Call<utilizador>

}