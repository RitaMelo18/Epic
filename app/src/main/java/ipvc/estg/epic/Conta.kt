package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class Conta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conta)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            val iduser = sharedPref.getString(getString(R.string.nomeUser), "0")
            findViewById<TextView>(R.id.textView7).setText(""+iduser)

            val imagem = findViewById<ImageView>(R.id.imageView10)
            val foto = sharedPref.getString(getString(R.string.fotoUser), "0")
            Picasso.get().load(foto).into(imagem)
            imagem.getLayoutParams().height = 300; // ajudtar tamanho da iamgem
            imagem.getLayoutParams().width = 300;
            imagem.requestLayout();

        }
        
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
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )
            with(sharedPref.edit()){
                putBoolean(getString(R.string.LoginShared), false)
                putString(getString(R.string.nomeUser), "")
                putString(getString(R.string.fotoUser), "")
                putString(getString(R.string.Id_LoginUser), "")
                commit()
            }
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        builder.setNegativeButton(R.string.Cancelar) { dialog: DialogInterface?, which: Int ->}
        builder.show()

    }
}