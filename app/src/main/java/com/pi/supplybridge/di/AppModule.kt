package com.pi.supplybridge.di

import com.pi.supplybridge.data.repositories.AuthRepository
import com.pi.supplybridge.data.repositories.OrderRepository
import com.pi.supplybridge.data.repositories.UserRepository
import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.usecases.order.GetOrderByIdUseCase
import com.pi.supplybridge.domain.usecases.order.GetOrdersUseCase
import com.pi.supplybridge.domain.usecases.order.SaveOrderUseCase
import com.pi.supplybridge.domain.usecases.user.GetUserByIdUseCase
import com.pi.supplybridge.domain.usecases.user.SaveUserUseCase
import com.pi.supplybridge.domain.usecases.user.UpdateUserUseCase
import com.pi.supplybridge.presentation.viewmodels.ProfileViewModel
import com.pi.supplybridge.presentation.viewmodels.ForgotPasswordViewModel
import com.pi.supplybridge.presentation.viewmodels.LoginViewModel
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase dependencies
    single { FirebaseService }

    // Repositories
    single { OrderRepository(get()) }
    single { UserRepository(get()) }
    single { AuthRepository(get()) }

    // Use Cases para Order
    factory { GetOrdersUseCase(get()) }
    factory { GetOrderByIdUseCase(get()) }
    factory { SaveOrderUseCase(get()) }

    // Use Cases para User
    factory { GetUserByIdUseCase(get()) }
    factory { SaveUserUseCase(get()) }
    factory { UpdateUserUseCase(get()) }

    // ViewModels
    viewModel { OrderViewModel(get(), get(), get()) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
