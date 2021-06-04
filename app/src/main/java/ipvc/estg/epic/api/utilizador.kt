package ipvc.estg.epic.api

data class utilizador (
    val id: Int,
    val email: String,
    val password: String,
    val nome: String,
    val foto_perfil: String
)