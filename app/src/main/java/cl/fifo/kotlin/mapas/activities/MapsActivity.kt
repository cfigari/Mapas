package cl.fifo.kotlin.mapas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cl.fifo.kotlin.mapas.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        val marcador:MarkerOptions?

        val maipu =LatLng(-33.475348137300315, -70.7516258955002)

      //  mMap.setMinZoomPreference(6.0f)
      //  mMap.setMaxZoomPreference(14.0f)

        marcador = MarkerOptions()
        marcador.position(maipu)
        marcador.title("Este es mi Marcador")
        marcador.snippet("Aqui puedes poner algún datos de localización")
        marcador.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on))
        marcador.draggable(true)

        mMap.addMarker(marcador)

        val camara = CameraPosition.Builder()
            .target(maipu)
            .bearing(0f)
            .zoom(17f)
            .tilt(0f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara))

      mMap.setOnMapClickListener { latLng ->
                                   Toast.makeText(this,"Las Cordenadas son : \n" +
                                  "Latitud: " + latLng.latitude + "\n" +
                                  "Longitud: " + latLng.longitude ,
                                   Toast.LENGTH_SHORT ).show()  }

        mMap.setOnMapLongClickListener { latLng ->
                Toast.makeText(this,"-- Las Cordenadas son : '\n" +
                "Latitud: " + latLng.latitude + "\n" +
                "Longitud: " + latLng.longitude ,
            Toast.LENGTH_SHORT ).show()  }

        mMap.setOnMarkerDragListener(object:GoogleMap.OnMarkerDragListener{
                override fun onMarkerDragEnd(marked: Marker) {
                    Toast.makeText(this@MapsActivity,"Drag Las Cordenadas son : '\n" +
                            "Latitud: " + marked.position.latitude + "\n" +
                            "Longitud: " + marked.position.longitude ,
                        Toast.LENGTH_SHORT ).show()  }

                override fun onMarkerDragStart(p0: Marker?) {

                }

                override fun onMarkerDrag(p0: Marker?) {

                }
        } )
    }
}
