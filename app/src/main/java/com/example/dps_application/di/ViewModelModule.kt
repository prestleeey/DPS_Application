package com.example.dps_application.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dps_application.ui.authorization.AuthorizationViewModel
import com.example.dps_application.ui.chat.ChatViewModel
import com.example.dps_application.ui.map.MapViewModel
import com.example.dps_application.viewmodel.DPSViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    abstract fun bindAuthorizationViewModel(authorizationViewModel: AuthorizationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(chatViewModel: ChatViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DPSViewModelFactory): ViewModelProvider.Factory
}
