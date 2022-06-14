package com.example.galonapps

import android.app.Application
import android.os.Bundle
import com.example.galonapps.storage.SharedPreferencesStorage
import com.example.galonapps.storage.manager.UserManager



open class App : Application() {
    open val userManager by lazy {
        UserManager(SharedPreferencesStorage(this))
    }
}
