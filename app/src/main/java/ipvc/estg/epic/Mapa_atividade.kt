package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import ipvc.estg.epic.databinding.ActivityMapaAtividadeBinding
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.widget.Toast
import com.squareup.picasso.Picasso
import ipvc.estg.epic.api.EndPoints
import ipvc.estg.epic.api.ServiceBuilder
import ipvc.estg.epic.api.atividade
import ipvc.estg.epic.api.utilizador
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Base64.getEncoder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url


class Mapa_atividade : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaAtividadeBinding

    private lateinit var mSnapshot : Bitmap

    private var calorias_total: Int = 0
    private var passos_total: Int = 0
    private var distancia_total: Double = 0.0
    private var velocidade_m_total: Double = 0.0
    private var tempo: Long = 0
    private var data_inicio = ""
    private var data_fim = ""
    private var string_caminho_lats : String? = null
    private var string_caminho_lngs : String? = null

    private var id_utl : Any? = 0
    private var foto_utl : Any? = ""
    private var nome_utl : Any? = ""

    private lateinit var encodedImage : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapaAtividadeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val calorias = intent.getStringExtra("CALORIAS")
        calorias_total = calorias!!.toInt()
        val passos = intent.getStringExtra("PASSOS")
        passos_total = passos!!.toInt()
        val dist = intent.getStringExtra("DISTANCIA")
        distancia_total = dist!!.toDouble()
        val velocidade = intent.getStringExtra("VELOCIDADE")
        velocidade_m_total = velocidade!!.toDouble()

        data_inicio = intent.getStringExtra("DATA_INICIO").toString()
        data_fim = intent.getStringExtra("DATA_FIM").toString()

        val temp = intent.getStringExtra("TEMPO")
        if (temp != null) {
            tempo = temp.toLong()
        }

        val lats = intent.getStringExtra("LATS")
        string_caminho_lats = lats

        val lngs = intent.getStringExtra("LNGS")
        string_caminho_lngs = lngs


        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            foto_utl = sharedPref.getString(getString(R.string.fotoUser), "0")
            id_utl = sharedPref.all[getString(R.string.Id_LoginUser)]
            nome_utl = sharedPref.all[getString(R.string.nomeUser)]
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val array_coords_lat = string_caminho_lats?.split(",")?.toTypedArray()    // dividir a string das latitutes e converter para array
        val array_coords_lng = string_caminho_lngs?.split(",")?.toTypedArray()

        val coordList = ArrayList<LatLng>()
        var ultima_coord : LatLng? = null

        if (array_coords_lat != null && array_coords_lng != null) {
            for (i in array_coords_lat.indices){
                if(i !== 0){
                    Log.d("TAG**", i.toString())
                    Log.d("TAG**", array_coords_lat[i] + " - " + array_coords_lng[i])
                    coordList.add(LatLng(array_coords_lat[i].toDouble(), array_coords_lng[i].toDouble()))
                    ultima_coord = LatLng(array_coords_lat[i].toDouble(), array_coords_lng[i].toDouble())
                }
            }
        }

        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(coordList)
        polylineOptions.width(5f).color(Color.BLUE)
        mMap.addPolyline(polylineOptions)

        // centra o mapa na ultima coordenada do utilizador
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ultima_coord, 14.0f))

    }

    fun captureScreen() {
       /* val callback =
            SnapshotReadyCallback { snapshot -> // TODO Auto-generated method stub
                if (snapshot != null) {
                    mSnapshot = snapshot
                }

                val imgView = this.findViewById<ImageView>(R.id.imagem_1)

                imgView.setImageBitmap(snapshot)

                var fout: OutputStream? = null
                val filePath = System.currentTimeMillis().toString() + ".jpeg"

                try {

                    fout = openFileOutput(filePath, MODE_APPEND)

                    // above "/mnt ..... png" => is a storage path (where image will be stored) + name of image you can customize as per your Requirement
                    var ola= mSnapshot.compress(Bitmap.CompressFormat.JPEG, 90, fout)
                    fout.flush()
                    fout.close()

                    Log.d("TAG**", ola.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("TAG**", e.toString())
                }

                val file: File = getFileStreamPath(filePath)
                Log.d("TAG**", file.getAbsolutePath().toString())

            } */





        //mMap.snapshot(callback)
    }


    private fun snapshot_mapa() {
        val callback: SnapshotReadyCallback = object : SnapshotReadyCallback {
            var bitmap: Bitmap? = null
            override fun onSnapshotReady(snapshot: Bitmap?) {
                bitmap = snapshot
                val bos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                encodedImage = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
                Log.d("TAG**", encodedImage)
                /*try {
                    var fout: OutputStream? = null
                    val filePath = System.currentTimeMillis().toString() + ".jpeg"
                    fout = openFileOutput(filePath, MODE_APPEND)
                    bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, fout)
                    fout.flush()
                    fout.close()
                    val file: File = getFileStreamPath(filePath)
                    Log.d("TAG**", bitmap.toString())
                    Log.d("TAG**", "DEU")
                } catch (e: Exception) {
                    Log.d("TAG**", e.toString())
                }*/
            }
        }
        mMap.snapshot(callback)
    }

    private fun publicar_atividade(publicar: Int) {

        val resto = tempo%1000
        val segundo = (tempo - resto)/1000
        val tempo_segundos = segundo.toInt()    // tempo em segundos

        val id_u : Int = id_utl.toString().toInt()
        val nome_u : String = nome_utl.toString()
        val foto_u : String = foto_utl.toString()

        // tempo_segundos - distancia_total - passos_total - velocidade_m_total - calorias_total - id_utl - encodedImage - data_inicio - data_fim - foto_utl - nome_utl

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.addAtividade(tempo_segundos, distancia_total, passos_total, velocidade_m_total, calorias_total, id_u, "xxxxx", data_inicio, data_fim, foto_u, nome_u, publicar)
        var intent = Intent(this, Home::class.java)

        call.enqueue(object : Callback<atividade> {
            override fun onResponse(call: Call<atividade>, response: Response<atividade>) {
                if (response.isSuccessful){

                    Toast.makeText(this@Mapa_atividade, R.string.atividade_publicada, Toast.LENGTH_SHORT).show()
                    startActivity(intent)


                } else {
                    Toast.makeText(this@Mapa_atividade, R.string.erro, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<atividade>, t: Throwable) {
                //Toast.makeText(this@Editar_eliminarPontos, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun editar_dados_utilizador() {
        //chamar os dados atuais do utilizador
        //distancia_total - passos_total

        var km_total : Double = 0.0
        var passos_total : Int = 0

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUtlAll(id_utl as Int)

        call.enqueue(object : Callback<utilizador> {
            override fun onResponse(call: Call<utilizador>, response: Response<utilizador>) {
                if (response.isSuccessful){
                    val e: utilizador = response.body()!!
                    Log.d("TAG**", "km: " + e.km_totais.toString() + " passos: " + e.passos_totais.toString())
                        km_total = e.km_totais
                        passos_total = e.passos_totais
                }
            }

            override fun onFailure(call: Call<utilizador>, t: Throwable) {
                /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
            }
        })

        //alterar novos dados

        val call_1 = request.atualizarUtl(id_utl as Int, (km_total + 2), (passos_total + 1500))

        call_1.enqueue(object : Callback<utilizador>{
            override fun onResponse(call: Call<utilizador>, response: Response<utilizador>) {
                if(response.isSuccessful){
                    Log.d("TAG**", "FUNCIONEI")
                }
            }
            override fun onFailure(call: Call<utilizador>, t: Throwable) {
                /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
                Log.d("TAG**", "NAOO FUNCIONEI")
            }
        })

    }


    fun publicar(view: View) {
        snapshot_mapa()
        editar_dados_utilizador()
        //publicar_atividade(1)     // 1 -  publicar a atividade
    }

    fun registar(view: View) {
        snapshot_mapa()
        editar_dados_utilizador()
        //publicar_atividade(0)     // 0 - não publicar a atividade
    }


}