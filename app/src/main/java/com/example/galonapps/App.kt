package com.example.galonapps

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.galonapps.storage.PreferencesHelper
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat


val prefs: PreferencesHelper by lazy {
    App.prefs!!
}

open class App : Application() {
    companion object {
        var prefs: PreferencesHelper? = null
        lateinit var instance: App
        fun alertDialog(context: Context, message: String = "Apakah anda yakin?", action: () -> Unit) {
            AlertDialog.Builder(context)
                .setTitle(message)
                .setPositiveButton("Ya") { dialog, which ->
                    action()
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        fun currencyFormat(value: Int?): String {
            val formatter: NumberFormat = DecimalFormat("#,###")
            return "Rp. ${formatter.format(value ?: 0)}"
        }
    }

    override fun onCreate() {
        super.onCreate()


        instance = this
        prefs = PreferencesHelper(applicationContext)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }
    }
}
