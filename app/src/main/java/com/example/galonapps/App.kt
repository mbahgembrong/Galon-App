package com.example.galonapps

import android.app.Application
import android.os.UserManager
import com.example.galonapps.model.Galon
import com.example.galonapps.storage.PreferencesHelper




open class App : Application() {
    open val userManager by lazy {
//        UserManager(PreferencesHelper(this))
    }
    companion object{
        val GalonDataList = mutableListOf<Galon>(Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"),Galon("1","Aqua","200ml",10,5000,10000,"https://images.tokopedia.net/img/cache/700/product-1/2018/4/28/2642564/2642564_8fff405b-851f-4196-b943-acae3d697e63_800_800.jpg"))
    }
}
