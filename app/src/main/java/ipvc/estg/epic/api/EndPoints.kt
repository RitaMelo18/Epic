package ipvc.estg.epic.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/myslim2/api/utilizador")
    fun postTest(@Field("email") first: String): Call<utilizador>

    @GET("/myslim2/api/atividade")
    fun getAtividade(): Call<MutableList<feed>>

    @FormUrlEncoded
    @POST("/myslim2/api/saudeUtl")
    fun postDadosSaude(@Field("id") first: Int): Call<saude>

}