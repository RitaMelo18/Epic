package ipvc.estg.epic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Classificacoes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classificacoes)
    }

    //Ir para a Página Inicial
    fun Home(view: View) {
        var intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    //Ir para os Serviços
    fun Servicos(view: View) {
        var intent = Intent(this, Servicos::class.java)
        startActivity(intent)
    }

    //Ir para a Atividade
    fun Atividade(view: View) {
        var intent = Intent(this, Atividade::class.java)
        startActivity(intent)
    }

    //Ir para a Saude
    fun Saude(view: View) {
        var intent = Intent(this, Saude::class.java)
        startActivity(intent)
    }
}