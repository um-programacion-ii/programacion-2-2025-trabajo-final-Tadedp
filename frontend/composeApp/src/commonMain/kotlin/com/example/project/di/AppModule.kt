package com.example.project.di

import com.example.project.data.local.AuthSettings
import com.example.project.domain.repository.AuthRepository
import com.example.project.domain.repository.AuthRepositoryImpl
import com.example.project.domain.repository.EventoRepository
import com.example.project.domain.repository.EventoRepositoryImpl
import com.example.project.domain.repository.SesionRepository
import com.example.project.domain.repository.SesionRepositoryImpl
import com.example.project.util.ManejadorSesion
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val appModule = module {
    single { Settings() }
    single { AuthSettings(get()) }
    single { ManejadorSesion(get()) }

    includes(networkModule)
    includes(viewModelModule)

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<SesionRepository> { SesionRepositoryImpl(get(), get()) }
    single<EventoRepository> { EventoRepositoryImpl(get()) }
}