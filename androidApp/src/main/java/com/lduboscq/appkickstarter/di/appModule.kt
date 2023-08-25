package com.lduboscq.appkickstarter.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import co.touchlab.kampkit.models.BreedViewModel
import com.lduboscq.appkickstarter.navigation.KTINavigator
import com.example.myapplication.common.coroutines.DispatcherProvider
import com.example.myapplication.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun appModule(application: Application) = module {
    single<Context> { application }
    viewModel { BreedViewModel(get(), get { parametersOf("BreedViewModel") }) }
    single<SharedPreferences> {
        get<Context>().getSharedPreferences(
            "KAMPSTARTER_SETTINGS",
            Context.MODE_PRIVATE
        )
    }
    single {
        { Log.i("Startup", "Hello from Android/Kotlin!") }
    }
    single<Navigator> {
        val dispatcherProvider: DispatcherProvider = get()
        val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)
        KTINavigator(scope)
    }
}