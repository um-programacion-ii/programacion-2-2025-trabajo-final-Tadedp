package com.example.project.di

import com.example.project.ui.screens.asistentes.AsistentesScreenModel
import com.example.project.ui.screens.detalle.DetalleScreenModel
import com.example.project.ui.screens.home.HomeScreenModel
import com.example.project.ui.screens.login.LoginScreenModel
import com.example.project.ui.screens.registro.RegistroScreenModel
import com.example.project.ui.screens.venta.VentaScreenModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { LoginScreenModel(get(), get(), get()) }
    factory { RegistroScreenModel(get()) }
    factory { HomeScreenModel(get(), get()) }
    factory { DetalleScreenModel(get(), get(), get()) }
    factory { AsistentesScreenModel(get()) }
    factory { VentaScreenModel(get(), get(), get()) }
}