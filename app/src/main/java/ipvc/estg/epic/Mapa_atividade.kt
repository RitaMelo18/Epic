package ipvc.estg.epic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import ipvc.estg.epic.databinding.ActivityMapaAtividadeBinding


class Mapa_atividade : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaAtividadeBinding

    private var calorias_total: Int = 0
    private var passos_total: Int = 0
    private var distancia_total: Double = 0.0
    private var velocidade_m_total: Double = 0.0
    private var tempo: Long = 0
    private var string_caminho_lats : String? = null
    private var string_caminho_lngs : String? = null


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

        val temp = intent.getStringExtra("TEMPO")
        if (temp != null) {
            tempo = temp.toLong()
        }

        val lats = intent.getStringExtra("LATS")
        string_caminho_lats = lats

        val lngs = intent.getStringExtra("LNGS")
        string_caminho_lngs = lngs
        //Log.d("TAG**", "tempo: " + passos + " - String: " + string_caminho_lngs)

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
}