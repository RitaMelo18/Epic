package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    //Ir para as classificações
    fun Classificacoes(view: View) {
        var intent = Intent(this, Classificacoes::class.java)
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

    //Ir para a Saúde
    fun Saude(view: View) {
        var intent = Intent(this, Saude::class.java)
        startActivity(intent)
    }

    //Ir para Conta
    fun Conta(view: View) {
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.conta), Context.MODE_PRIVATE
        )
        with(sharedPref.edit()){
            putString(getString(R.string.paginaRetornoConta), "Home")
            commit()
        }

        var intent = Intent(this, Conta::class.java)
        startActivity(intent)
    }
}