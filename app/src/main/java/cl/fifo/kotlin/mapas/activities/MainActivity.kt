package cl.fifo.kotlin.mapas.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import cl.fifo.kotlin.mapas.R
import cl.fifo.kotlin.mapas.fragments.MapsFragment
import cl.fifo.kotlin.mapas.fragments.MapsFragmentGps
import cl.fifo.kotlin.mapas.fragments.mainFragment
import com.google.android.gms.maps.model.Marker

class MainActivity : AppCompatActivity() {
    var fragmentoActual: Fragment? = null


override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentoActual=mainFragment()
        cambiarfragmento(fragmentoActual)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_principal -> fragmentoActual = mainFragment()
            R.id.menu_mapa ->fragmentoActual =MapsFragment()
            R.id.menu_mapa_gps->fragmentoActual= MapsFragmentGps()
        }
        cambiarfragmento(fragmentoActual)
        return super.onOptionsItemSelected(item)
    }

    fun cambiarfragmento(fragmento:Fragment?){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentsContainer, fragmento!!)
            .commit()
    }
}


