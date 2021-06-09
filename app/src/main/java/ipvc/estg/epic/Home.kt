package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ipvc.estg.epic.adapter.PostAdapter
import ipvc.estg.epic.api.EndPoints
import ipvc.estg.epic.api.ServiceBuilder
import ipvc.estg.epic.api.feed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

        //Listar atividade no feed

        val recyclerView = findViewById<RecyclerView>(R.id.myRecyclerview)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAtividade()


            call.enqueue(object : Callback<MutableList<feed>> {
                override fun onResponse(call: Call<MutableList<feed>>, response: Response<MutableList<feed>>) {
                    if (response.isSuccessful) {
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(this@Home)
                            adapter = PostAdapter(response.body()!!)
                            Log.d("****AQUI", response.body().toString())
                        }


                    }
                }

                override fun onFailure(call: Call<MutableList<feed>>, t: Throwable) {
                    /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
                    t.printStackTrace()
                    Log.d("****ERRO", t.message.toString())
                }
            })







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

    //Ir para o Chat
    fun Chat(view: View) {
        var intent = Intent(this, Chat::class.java)
        startActivity(intent)
    }
}

