package br.com.francielilima.githubfetch

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(appModule, viewModelModule, networkModule, databaseModule, repositoryModule)
        }
    }
}