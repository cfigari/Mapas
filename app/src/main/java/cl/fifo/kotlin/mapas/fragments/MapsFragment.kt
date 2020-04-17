package cl.fifo.kotlin.mapas.fragments

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cl.fifo.kotlin.mapas.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : Fragment() ,OnMapReadyCallback{
    var miVista:View?=null
    var geoCoder:Geocoder?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        miVista =inflater.inflate(R.layout.fragment_maps, container, false)
        return miVista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView: MapView?
        mapView=miVista!!.findViewById(R.id.mapView) as MapView
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
        checarSenialGPS()
    }
    override fun onResume() {
        super.onResume()
        checarSenialGPS()
    }

    fun checarSenialGPS(){

       try {
           val lm: LocationManager =
               context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

           val gps =lm.isProviderEnabled(LocationManager.GPS_PROVIDER)

           if (!gps) {
               Toast.makeText(context,"Por favor para continuar, habilita la señal Gps",Toast.LENGTH_SHORT).show()
               val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
               startActivity(intent)}
           }catch(e: SecurityException){
               e.printStackTrace()
           }
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        val gMap: GoogleMap? =googleMap
        val maipu = LatLng(-33.475348137300315, -70.7516258955002)
        val zoom = CameraUpdateFactory.zoomTo(12f)
        gMap!!.addMarker(MarkerOptions().position(maipu).draggable(true))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(maipu))
        gMap.animateCamera(zoom)

        /* val camara = CameraPosition.Builder()
            .target(maipu)
            .bearing(0f)
            .zoom(17f)
            .tilt(0f)
            .build()
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara))
*/
        gMap.setOnMarkerDragListener(object:GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragEnd(marked: Marker) {
                var direccion : List<Address>?=null
                val latitud =marked.position.latitude
                val longitud =marked.position.longitude
                    try {
                        direccion = geoCoder!!.getFromLocation(latitud, longitud, 1)
                    }catch(e: IOException){
                        e.printStackTrace()
                    }

                //val pais =direccion!![0].countryName
                //val estado =direccion[0].adminArea
                val ciudad =direccion!![0].locality
                val calle =direccion[0].getAddressLine(0)
               // val codigoPOstal =direccion[0].postalCode

                marked.title=ciudad
                marked.snippet=calle
                marked.showInfoWindow()

              /*  Toast.makeText(context,"La Locación es : '\n" +
                        "pais: " + pais + "\n" +
                        "estado: " + estado + "\n" +
                         "ciudad: " + ciudad + "\n" +
                         "calle: " + calle + "\n" +
                         "codigoPostal: " + codigoPOstal ,
                    Toast.LENGTH_SHORT ).show() */ }

            override fun onMarkerDragStart(marked: Marker?) {
                marked!!.hideInfoWindow()
            }

            override fun onMarkerDrag(marked: Marker?) {

            }
        } )

        geoCoder = Geocoder(context, Locale.getDefault())

    }
}



