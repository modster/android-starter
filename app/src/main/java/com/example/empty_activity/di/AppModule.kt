package com.example.empty_activity.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import com.example.empty_activity.MainScreenViewModel
import com.example.empty_activity.data.DataRepository
import com.example.empty_activity.data.DataRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.plugin.module.dsl.single

val appModule = module {
    single { DataRepositoryImpl() } bind DataRepository::class
    viewModel { MainScreenViewModel(get()) }
}
