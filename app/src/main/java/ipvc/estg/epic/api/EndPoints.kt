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

    @FormUrlEncoded
    @POST("myslim2/api/registAtividade")
    fun addAtividade(@Field("tempo") first: Int, @Field("distancia") second: Double,
                @Field("passos") third: Int, @Field("velocidade_media") fourth: Double,
                @Field("calorias") fifth: Int, @Field("utilizador_id") sixth: Int,
                @Field("imagem_mapa") seventh: String, @Field("data_inicio") eighth: String,
                     @Field("data_fim") ninth: String, @Field("foto_utilizador") tenth: String,
                     @Field("nome") eleventh: String, @Field("publicado") twelfth: Int): Call<atividade>

}