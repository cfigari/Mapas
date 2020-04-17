package cl.fifo.kotlin.mapas

import android.app.Application

import android.os.SystemClock

class DummySplash() : Application() {

    override fun onCreate() {
        super.onCreate()
        SystemClock.sleep(3000)
    }
}