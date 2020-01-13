package com.example.dps_application.di

import com.example.dps_application.ui.chat.ChatFragment
import com.example.dps_application.ui.map.MapFragment
import com.example.dps_application.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun contributeChatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

}