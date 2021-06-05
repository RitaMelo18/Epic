package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

import android.widget.ImageView
import com.squareup.picasso.Picasso

class Atividade : AppCompatActivity(), SensorEventListener {

    var ativo = 0
    private var sensorManager: SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var passos: TextView? = null
    private var distancia: TextView? = null
    private var velocidade_media: TextView? = null
    private var calorias: TextView? = null
    private var cronometro: Chronometer? = null

    private var passosTotais = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade)

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

        resetPassos()
        cronometro = this.findViewById<Chronometer>(R.id.simpleChronometer)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        passos = this.findViewById<TextView>(R.id.passos)
        distancia = this.findViewById<TextView>(R.id.distancia)
        velocidade_media = this.findViewById<TextView>(R.id.velocidade_media)
        calorias = this.findViewById<TextView>(R.id.calorias)
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

    //Ir para Saude
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
            putString(getString(R.string.paginaRetornoConta), "Atividade")
            commit()
        }

        var intent = Intent(this, Conta::class.java)
        startActivity(intent)
    }

    //Iniciar registo de Atividade Fisica
    fun iniciar_atividade(view: View) {

        if(ativo == 0){     // iniciar a atividade
            cronometro?.setBase(SystemClock.elapsedRealtime())   // por o cronometro a 0
            cronometro?.start()      // iniciar cronometro
            running = true          // iniciar sensor contagem de passos
            ativo++
        }else if(ativo == 1){   // terminar a atividade
            ativo--
            var tempo = SystemClock.elapsedRealtime()-cronometro!!.base
            Toast.makeText(this, "Time: " + tempo, Toast.LENGTH_SHORT).show()
            cronometro?.stop()
            running = false
        }

    }

    override fun onResume() {
        super.onResume()

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this, "Não foi detetado nenhum Sensor", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            totalSteps = event!!.values[0]

            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            var km = passosTotais * 0.00047
            var milisegundos = SystemClock.elapsedRealtime()-cronometro!!.base
            var segundos = milisegundos/1000
            var horas = segundos * 0.000277
            var velocidade_m = km * (1/horas.toFloat())
            //Log.d("TAG**", "Mili: " + milisegundos.toString() + " Horas: " + velocidade_m)

            if(previousTotalSteps != 0f){
                passosTotais += currentSteps

            }

            previousTotalSteps = totalSteps

            passos?.text  = ("$passosTotais")
            distancia?.text = String.format("%.2f", km)

            velocidade_media?.text = String.format("%.1f", velocidade_m)    // Vmedia = delta dist / destal tempo  => converter para km e para horas
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    private fun resetPassos (){
        passos?.text = 0.toString()
    }


}