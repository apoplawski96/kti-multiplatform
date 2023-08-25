package com.lduboscq.appkickstarter.di

import co.touchlab.kampkit.coreModule
import co.touchlab.kampkit.platformModule
import com.example.myapplication.di.dataModule
import com.example.myapplication.di.domainModule
import com.example.myapplication.di.screenModelModule
import com.lduboscq.appkickstarter.data.PersonRepository
import com.lduboscq.appkickstarter.data.SettingsFactory
import com.lduboscq.appkickstarter.detail.PersonDetailScreenModel
import com.lduboscq.appkickstarter.list.PersonListScreenModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun sharedPlatformModule(): Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        commonModule,
        sharedPlatformModule(),
        platformModule,
        coreModule,
        dataModule,
        domainModule,
        screenModelModule,
    )
}

// for ios
fun initKoin() {
    initKoin {  }
}

val commonModule = module {
    singleOf(::PersonRepository)
    factory { (id: String) -> PersonDetailScreenModel(id, repository = get()) }
    factoryOf(::PersonListScreenModel)
}
