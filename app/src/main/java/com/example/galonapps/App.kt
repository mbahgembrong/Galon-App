package com.example.galonapps

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.example.galonapps.model.Galon
import com.example.galonapps.model.Transaksi
import com.example.galonapps.storage.PreferencesHelper
import timber.log.Timber


val prefs: PreferencesHelper by lazy {
    App.prefs!!
}

open class App : Application() {
    companion object {
        var prefs: PreferencesHelper? = null
        lateinit var instance: App
            private set
        val GalonDataList = mutableListOf<Galon>(
            Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),
            Galon("2","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),
            Galon("3","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"))
        var TransaksiDataList= mutableListOf<Transaksi>()
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
