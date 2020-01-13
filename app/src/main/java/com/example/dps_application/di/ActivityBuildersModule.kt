package com.example.dps_application.di

import com.example.dps_application.ui.MainActivity
import com.example.dps_application.ui.authorization.AuthorizationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthorizationActivity(): AuthorizationActivity
}
