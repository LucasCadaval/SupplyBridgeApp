package com.pi.supplybridge.di

import com.pi.supplybridge.data.cache.UserPreferences
import com.pi.supplybridge.data.repositories.AuthRepository
import com.pi.supplybridge.data.repositories.OrderRepository
import com.pi.supplybridge.data.repositories.UserRepository
import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.usecases.order.*
import com.pi.supplybridge.domain.usecases.user.*
import com.pi.supplybridge.presentation.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase dependencies
    single { FirebaseService }

    // Repositories
    single { OrderRepository(get()) }
    single { UserRepository(get()) }
    single { AuthRepository(get()) }

    // User Preferences (requires Context)
    single { UserPreferences(androidContext()) }

    // Use Cases para Order
    factory { GetOrdersUseCase(get()) }
    factory { GetOrderByIdUseCase(get()) }
    factory { SaveOrderUseCase(get()) }
    factory { GetOrdersByStatusUseCase(get()) }
    factory { GetOrdersByUserIdUseCase(get()) }
    factory { UpdateOrderStatusUseCase(get()) }

    // Use Cases para User
    factory { GetUserByIdUseCase(get()) }
    factory { SaveUserUseCase(get()) }
    factory { UpdateUserUseCase(get()) }

    // Use Case para Store Preferences
    factory { StorePreferencesUseCase(get()) }

    // ViewModels
    viewModel { OrderViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { UserViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
