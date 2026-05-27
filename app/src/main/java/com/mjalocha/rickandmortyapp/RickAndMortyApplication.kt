package com.mjalocha.rickandmortyapp

import android.app.Application
import com.mjalocha.rickandmortyapp.di.AppModule
import com.mjalocha.rickandmortyapp.di.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.plugin.module.dsl.modules

class RickAndMortyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RickAndMortyApplication)
            modules(AppModule::class)
            modules(navigationModule)
        }
    }
}
