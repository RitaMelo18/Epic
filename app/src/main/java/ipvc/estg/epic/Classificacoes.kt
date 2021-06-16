package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso

class Classificacoes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classificacoes)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            val imagem = findViewById<ImageView>(R.id.imageView16)
            val foto = sharedPref.getString(getString(R.string.fotoUser), "0")
            Picasso.get().load(foto).into(imagem)
            imagem.getLayoutParams().height = 120;
            imagem.getLayoutParams().width = 120;


            imagem.requestLayout();
        }
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

    //Ir para Conta
    fun Conta(view: View) {
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.conta), Context.MODE_PRIVATE
        )
        with(sharedPref.edit()){
            putString(getString(R.string.paginaRetornoConta), "Classificacoes")
            commit()
        }

        var intent = Intent(this, Conta::class.java)
        startActivity(intent)
    }

    fun class_amigos(view: View) {
        val btn_amigos = findViewById<Button>(R.id.amigos)
        val btn_globais = findViewById<Button>(R.id.globais)

        val card_1 = findViewById<CardView>(R.id.primeiro)
        val card_2 = findViewById<CardView>(R.id.segundo)
        val card_3 = findViewById<CardView>(R.id.terceiro)

        val card_1_g = findViewById<CardView>(R.id.primeiro_g)
        val card_2_g = findViewById<CardView>(R.id.segundo_g)
        val card_3_g = findViewById<CardView>(R.id.terceiro_g)
        val card_4_g = findViewById<CardView>(R.id.quarto_g)

        btn_amigos.setTextColor(Color.BLUE)
        btn_globais.setTextColor(Color.BLACK)

        card_1.isVisible = true
        card_2.isVisible = true
        card_3.isVisible = true

        card_1_g.isVisible = false
        card_2_g.isVisible = false
        card_3_g.isVisible = false
        card_4_g.isVisible = false

    }

    fun class_global(view: View) {
        val btn_amigos = findViewById<Button>(R.id.amigos)
        val btn_globais = findViewById<Button>(R.id.globais)

        val card_1 = findViewById<CardView>(R.id.primeiro)
        val card_2 = findViewById<CardView>(R.id.segundo)
        val card_3 = findViewById<CardView>(R.id.terceiro)

        val card_1_g = findViewById<CardView>(R.id.primeiro_g)
        val card_2_g = findViewById<CardView>(R.id.segundo_g)
        val card_3_g = findViewById<CardView>(R.id.terceiro_g)
        val card_4_g = findViewById<CardView>(R.id.quarto_g)

        btn_amigos.setTextColor(Color.BLACK)
        btn_globais.setTextColor(Color.BLUE)

        card_1.isVisible = false
        card_2.isVisible = false
        card_3.isVisible = false

        card_1_g.isVisible = true
        card_2_g.isVisible = true
        card_3_g.isVisible = true
        card_4_g.isVisible = true

    }
}