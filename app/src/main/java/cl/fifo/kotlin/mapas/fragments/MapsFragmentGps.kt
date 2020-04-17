package cl.fifo.kotlin.mapas.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import cl.fifo.kotlin.mapas.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * A simple [Fragment] subclass.
 */
class MapsFragmentGps : Fragment() ,OnMapReadyCallback,LocationListener{
    var miVista:View?=null

    var gestorDeLocacion: LocationManager?=null
    var marcador: Marker?=null

    var gMap: GoogleMap?=null

    var locacionActual:Location?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        miVista =inflater.inflate(R.layout.fragment_mapsgps, container, false)

       val fab:FloatingActionButton=miVista!!.findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            if( ActivityCompat.checkSelfPermission(
                    context!!,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                return@setOnClickListener
            }
            var location:Location?=gestorDeLocacion!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location==null)
            {
                location=gestorDeLocacion!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            locacionActual=location
            if (locacionActual!=null)
            {
                actualizaroCrearMarcador(location!!)


                val camara = CameraPosition.Builder()
                    .target(LatLng(location.latitude,location.longitude))
                    .bearing(0f)
                    .zoom(17f)
                    .tilt(0f)
                    .build()
                gMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(camara))


            }
        }
        return miVista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapView: MapView?


        mapView=miVista!!.findViewById(R.id.mapViewGps) as MapView
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
        gMap=googleMap

        gestorDeLocacion = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager


       if( ActivityCompat.checkSelfPermission(
                context!!,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return
        }
    //   gMap!!.isMyLocationEnabled = true
     //   gMap.uiSettings.isMyLocationButtonEnabled=false

        gestorDeLocacion!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0f,this)
        gestorDeLocacion!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0f,this)
    }

    override fun onLocationChanged(location: Location?) {

        actualizaroCrearMarcador(location!!)


        Toast.makeText(context,"Actualizado " + location.provider ,Toast.LENGTH_LONG).show()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    fun actualizaroCrearMarcador(location:Location){

        if(marcador==null)
        {
            marcador=gMap!!.addMarker(MarkerOptions().position(LatLng(location.latitude,location.longitude)).draggable(true))
        }else
        {
            marcador!!.position=LatLng(location.latitude,location.longitude)
        }
    }
}//



