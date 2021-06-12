package ipvc.estg.epic.api

data class atividade (
    val tempo: Int,
    val distancia: Double,
    val passos: Int,
    val velocidade_media: Double,
    val calorias: Int,
    val utilizador_id: Int,
    val imagem_mapa: String,
    val data_inicio: String,
    val data_fim: String,
    val foto_utilizador: String,
    val nome: String,
    val publicado: Int,
    val imagem: String,
    val nomeImagem: String
)