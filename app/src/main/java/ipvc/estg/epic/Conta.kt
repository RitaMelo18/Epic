package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences

class Conta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conta)
    }

    //Voltar para o Feed
    fun VoltarFeed(view: View) {

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.conta), Context.MODE_PRIVATE
        )

        if((sharedPref.all[getString(R.string.paginaRetornoConta)])?.equals("Home") == true){
            var intent = Intent(this, Home::class.java)
            startActivity(intent)
            with(sharedPref.edit()){
                putString(getString(R.string.paginaRetornoConta), "")
                commit()
            }
        }
        if((sharedPref.all[getString(R.string.paginaRetornoConta)])?.equals("Classificacoes") == true){
            var intent = Intent(this, Classificacoes::class.java)
            startActivity(intent)
            with(sharedPref.edit()){
                putString(getString(R.string.paginaRetornoConta), "")
                commit()
            }
        }
        if((sharedPref.all[getString(R.string.paginaRetornoConta)])?.equals("Atividade") == true){
            var intent = Intent(this, Atividade::class.java)
            startActivity(intent)
            with(sharedPref.edit()){
                putString(getString(R.string.paginaRetornoConta), "")
                commit()
            }
        }
        if((sharedPref.all[getString(R.string.paginaRetornoConta)])?.equals("Saude") == true){
            var intent = Intent(this, Saude::class.java)
            startActivity(intent)
            with(sharedPref.edit()){
                putString(getString(R.string.paginaRetornoConta), "")
                commit()
            }
        }
        if((sharedPref.all[getString(R.string.paginaRetornoConta)])?.equals("Servicos") == true){
            var intent = Intent(this, Servicos::class.java)
            startActivity(intent)
            with(sharedPref.edit()){
                putString(getString(R.string.paginaRetornoConta), "")
                commit()
            }
        }


    }

    //Terminar Sessão
    fun TerminarSessao(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.TerminarSessao)
        builder.setMessage(R.string.MensagemTerminarSessao)
        builder.setIcon(R.drawable.logo)
        builder.setPositiveButton(R.string.Sim) { dialog: DialogInterface?, which: Int ->
            //Fab
            /*val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )
            with(sharedPref.edit()){
                putBoolean(getString(R.string.LoginShared), false)
                putString(getString(R.string.EmailShared), "")
                commit()
            }*/
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        builder.setNegativeButton(R.string.Cancelar) { dialog: DialogInterface?, which: Int ->}
        builder.show()

    }
}