package ipvc.estg.epic.api

data class feed (
    val id: Int ? =null,
    val utilizador_id: Int? =null,
    val calorias: Int? =null,
    val passos: Int? =null,
    val distancia: Float? =null,
    val data_inicio: String? =null,
    val data_fim: String? =null,
    val tempo: Int? =null,
    val velocidade_media: Int? =null,
    val imagem_mapa: String? =null
)