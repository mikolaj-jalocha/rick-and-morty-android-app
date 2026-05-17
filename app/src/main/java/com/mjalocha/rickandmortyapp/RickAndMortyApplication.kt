package com.mjalocha.rickandmortyapp

import android.app.Application
import com.mjalocha.rickandmortyapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class MyApp
class RickAndMortyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin<MyApp> {
            androidLogger()
            androidContext(this@RickAndMortyApplication)
        }
    }
}