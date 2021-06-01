package ipvc.estg.epic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface

class Conta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conta)
    }

    //Voltar para o Feed
    fun VoltarFeed(view: View) {
        var intent = Intent(this, Home::class.java)
        startActivity(intent)
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