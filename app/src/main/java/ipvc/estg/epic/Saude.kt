package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import ipvc.estg.epic.api.EndPoints
import ipvc.estg.epic.api.ServiceBuilder
import ipvc.estg.epic.api.saude
import ipvc.estg.epic.api.utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Saude : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saude)

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

        val iduser = sharedPref.getInt(getString(R.string.Id_LoginUser), 0)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postDadosSaude(iduser)

        call.enqueue(object : Callback<saude> {
            override fun onResponse(call: Call<saude>, response: Response<saude>) {
                if (response.isSuccessful){
                    val e: saude = response.body()!!

                    //Peso
                    findViewById<TextView>(R.id.PesoRecebido).setText("PESO: " + e.peso + " KG")

                    //Altura
                    findViewById<TextView>(R.id.AlturaRecebida).setText("ALTURA: " + e.altura + " CM")

                    val genero = e.genero //1-Masculino || 2-Feminino
                    val homem = findViewById<TextView>(R.id.Homem)
                    val masculino = findViewById<ImageView>(R.id.Masculino)
                    val mulher = findViewById<TextView>(R.id.Mulher)
                    val feminino = findViewById<ImageView>(R.id.Feminino)

                    if(genero == 1){ //Se for homem
                        homem.visibility = (View.VISIBLE)
                        masculino.visibility = (View.VISIBLE)
                    } else {
                        mulher.visibility = (View.VISIBLE)
                        feminino.visibility = (View.VISIBLE)
                    }

                    val idade = findViewById<TextView>(R.id.Idade)
                    val dataNascimento = e.data_nascimento.split("-").toTypedArray()
                    val diaAniversario = dataNascimento[2].toInt()
                    val mesAniversario = dataNascimento[1].toInt()
                    val anoAniversario = dataNascimento[0].toInt()

                    val diaAtual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    val mesAtual = Calendar.getInstance().get(Calendar.MONTH)
                    val anoAtual = Calendar.getInstance().get(Calendar.YEAR)

                    var idadeCalculada = 0;

                    if(mesAtual < mesAniversario){
                        idadeCalculada = (anoAtual - 1) - anoAniversario
                    } else if(mesAtual > mesAniversario){
                        idadeCalculada = anoAtual - anoAniversario
                    } else if(mesAtual == mesAniversario){
                        if(diaAniversario < diaAtual){
                            idadeCalculada = anoAtual - anoAniversario
                        } else if(diaAniversario > diaAtual){
                            idadeCalculada = (anoAtual - 1) - anoAniversario
                        } else if(diaAniversario == diaAtual){
                            idadeCalculada = anoAtual - anoAniversario
                        }
                    }

                    idade.setText( "IDADE: " + idadeCalculada + " ANOS")
                    var alturaM : Double
                    alturaM = (e.altura.toDouble() / 100)


                    var IMC = (e.peso/(alturaM * alturaM))
                    val pesoMin = ((alturaM) * (alturaM) * 18.6).toInt()
                    val pesoMax = ((alturaM) * (alturaM) * 25 ).toInt()

                    findViewById<TextView>(R.id.Peso_ideal).setText("ENTRE " + pesoMin.toString() + "KG" + " E " + pesoMax.toString() + "KG")

                    val altura2  = alturaM * alturaM

                   Log.d("*****", IMC.toString())

                   val imagem = sharedPref.getString(getString(R.string.fotoUser), "0")

                   val baixoPeso = findViewById<ImageView>(R.id.abaixoPeso)
                   val pesoNormal = findViewById<ImageView>(R.id.PesoNormal)
                   val acimaPeso = findViewById<ImageView>(R.id.AcimaPeso)
                   val Obeso = findViewById<ImageView>(R.id.ExcessoPeso)

                   if(IMC < 18.5){
                       Picasso.get().load(imagem).into(baixoPeso)
                       baixoPeso.getLayoutParams().height = 80;
                       baixoPeso.getLayoutParams().width = 80;
                       baixoPeso.requestLayout();
                       baixoPeso.visibility = (View.VISIBLE)
                   } else if( IMC > 18.5 && IMC < 25){
                       Picasso.get().load(imagem).into(pesoNormal)
                       pesoNormal.getLayoutParams().height = 80;
                       pesoNormal.getLayoutParams().width = 80;
                       pesoNormal.requestLayout();
                       pesoNormal.visibility = (View.VISIBLE)
                   } else if( IMC > 25 && IMC < 30){
                       Picasso.get().load(imagem).into(acimaPeso)
                       acimaPeso.getLayoutParams().height = 80;
                       acimaPeso.getLayoutParams().width = 80;
                       acimaPeso.requestLayout();
                       acimaPeso.visibility = (View.VISIBLE)
                   }else if( IMC  > 30 ){
                       Picasso.get().load(imagem).into(Obeso)
                       Obeso.getLayoutParams().height = 80;
                       Obeso.getLayoutParams().width = 80;
                       Obeso.requestLayout();
                       Obeso.visibility = (View.VISIBLE)
                   }


                }
            }

            override fun onFailure(call: Call<saude>, t: Throwable) {
            /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
            }
        })
    }

    //Ir para as Classificações
    fun Classificacoes(view: View) {
    var intent = Intent(this, Classificacoes::class.java)
    startActivity(intent)
    }

    //Ir para a Página inicial
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

    //Ir para Conta
    fun Conta(view: View) {
    val sharedPref: SharedPreferences = getSharedPreferences(
    getString(R.string.conta), Context.MODE_PRIVATE
    )
    with(sharedPref.edit()){
    putString(getString(R.string.paginaRetornoConta), "Saude")
    commit()
    }

    var intent = Intent(this, Conta::class.java)
    startActivity(intent)
    }
}