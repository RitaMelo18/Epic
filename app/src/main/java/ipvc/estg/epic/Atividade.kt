package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

import com.squareup.picasso.Picasso
import ipvc.estg.epic.api.EndPoints
import ipvc.estg.epic.api.ServiceBuilder
import ipvc.estg.epic.api.utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private var mets = 0.0

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallBack: LocationCallback

    lateinit var loc : LatLng
    lateinit var lista_lat_lng : MutableList<LatLng>

    //var array_coords = arrayOf<LatLng?>()
    //var array_lat = arrayOf<Double?>()
    //var array_lng = arrayOf<Double?>()

    var string_caminho_lat = String()
    var string_caminho_lng = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade)



        cronometro = this.findViewById<Chronometer>(R.id.simpleChronometer)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        passos = this.findViewById<TextView>(R.id.passos)
        distancia = this.findViewById<TextView>(R.id.distancia)
        velocidade_media = this.findViewById<TextView>(R.id.velocidade_media)
        calorias = this.findViewById<TextView>(R.id.calorias)

        var id_utl : Any? = 0

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            val imagem = findViewById<ImageView>(R.id.imageView16)
            val foto = sharedPref.getString(getString(R.string.fotoUser), "0")
            id_utl = sharedPref.all[getString(R.string.Id_LoginUser)]

            Picasso.get().load(foto).into(imagem)
            imagem.getLayoutParams().height = 120;
            imagem.getLayoutParams().width = 120;

            imagem.requestLayout();
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUtlAll(id_utl as Int)

        call.enqueue(object : Callback<utilizador> {
            override fun onResponse(call: Call<utilizador>, response: Response<utilizador>) {
                if (response.isSuccessful){
                    val e: utilizador = response.body()!!
                    mets = (42 * e.peso.toInt())*0.005   // mets - variavel necessaria apra calcular as calorias
                    Log.d("TAG**", "mets: "+mets)
                }
            }

            override fun onFailure(call: Call<utilizador>, t: Throwable) {
                /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
            }
        })


        resetPassos()   // limpar dados


        /* ------ OBTER LOCALIZAÇÃO CALLBACK--------- */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation

                loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                //array_coords = add_to_array(array_coords, loc)
                //array_lat = add_to_array_double(array_lat, loc.latitude)
                //array_lng = add_to_array_double(array_lng, loc.longitude)

                string_caminho_lat = string_caminho_lat + "," + loc.latitude.toString()
                string_caminho_lng = string_caminho_lng + "," + loc.longitude.toString()

                Log.d("TAG**", "String: " + string_caminho_lat)
                //Log.d("TAG**", "latitude: " + loc.latitude + " - longitude: " + loc.longitude)
            }
        }

        createLocationRequest()
        // --------------------------------------------- //

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

        var btn_ini_fim = this.findViewById<Button>(R.id.button2)

        if(ativo == 0){     // iniciar a atividade
            btn_ini_fim.text = getString(R.string.terminar_atividade)
            btn_ini_fim.setBackgroundResource(R.drawable.atividade_btn)

            cronometro?.setBase(SystemClock.elapsedRealtime())   // por o cronometro a 0
            cronometro?.start()      // iniciar cronometro
            running = true          // iniciar sensor contagem de passos
            ativo++

            // LOCALIZAÇÃO
            startLocationUpdates()

        }else if(ativo == 1){   // terminar a atividade
            val intent = Intent(this, Mapa_atividade::class.java)   //página do mapa
            var tempo = SystemClock.elapsedRealtime()-cronometro!!.base

            val calorias_total = calorias?.text.toString()
            val passos_total = passos?.text.toString()
            val distancia_total = distancia?.text.toString()
            val velocidade_m_total = velocidade_media?.text.toString()

            ativo--
            cronometro?.stop()
            running = false

            // PARAR SENSOR
            sensorManager?.unregisterListener(this)

            // PARAR LOCALIZAÇÃO
            fusedLocationClient.removeLocationUpdates(locationCallBack)

            intent.putExtra("CALORIAS", calorias_total)  //calorias
            intent.putExtra("PASSOS", passos_total)  //passos
            intent.putExtra("DISTANCIA", distancia_total)  //distancia
            intent.putExtra("VELOCIDADE", velocidade_m_total)  //velocidade media
            intent.putExtra("TEMPO", tempo.toString())  //tempo - milisegundos
            intent.putExtra("LATS", string_caminho_lat)
            intent.putExtra("LNGS", string_caminho_lng)

            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()

        // SENSOR
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this, "Não foi detetado nenhum Sensor", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

    }

    override fun onPause() {
        super.onPause()

        // PARAR SENSOR
        sensorManager?.unregisterListener(this)

        // PARAR LOCALIZAÇÃO
        //fusedLocationClient.removeLocationUpdates(locationCallBack)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            totalSteps = event!!.values[0]

            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            var km = passosTotais * 0.00047
            var milisegundos = SystemClock.elapsedRealtime()-cronometro!!.base
            var segundos = milisegundos/1000
            var minutos = segundos * 0.01667
            var horas = segundos * 0.000277
            var velocidade_m = km * (1/horas.toFloat())
            var cal = minutos * mets
            Log.d("TAG**", "Minutos: " + minutos + " mets: " + mets + " Calorias: " + cal)

            if(previousTotalSteps != 0f){
                passosTotais += currentSteps

            }

            previousTotalSteps = totalSteps

            passos?.text  = ("$passosTotais")

            distancia?.text = String.format("%.2f", km)

            velocidade_media?.text = String.format("%.1f", velocidade_m)    // Vmedia = delta dist / destal tempo  => converter para km e para horas

            calorias?.text = String.format("%.2f", cal)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    private fun resetPassos (){
        passos?.text = 0.toString()
    }

    // Função para adicionar a LOC atual ao array_coords
    /*fun add_to_array (arr: Array<LatLng?>, coords: LatLng): Array<LatLng?> {
        val array = arr.copyOf(arr.size + 1)
        array[arr.size] = coords

        return array
    }*/

    fun add_to_array_double (arr: Array<Double?>, coords: Double): Array<Double?> {
        val array = arr.copyOf(arr.size + 1)
        array[arr.size] = coords

        return array
    }


    /* FUNÇÕES LOCALIZAÇÃO */

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 5000     // 5 segundos
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, null)
    }

    // ------------------- //


}