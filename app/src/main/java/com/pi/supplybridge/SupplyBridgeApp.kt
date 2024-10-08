package com.pi.supplybridge

import android.app.Application
import com.pi.supplybridge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SupplyBridgeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SupplyBridgeApp)
            modules(appModule)
        }
    }
}
