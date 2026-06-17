package com.example.empty_activity

import android.app.Application
import com.example.empty_activity.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
// import io.kotzilla.generated.monitoring

class MainApplication : Application()
{

    override fun onCreate()
    {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
            // monitoring()  // optional — Kotzilla observability, see docs/observability.md
        }
    }
}
