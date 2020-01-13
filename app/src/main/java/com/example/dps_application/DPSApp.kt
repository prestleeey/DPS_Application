package com.example.dps_application

import android.app.Activity
import android.app.Application
import com.example.dps_application.di.ApplicationInjector
import com.vk.api.sdk.VK
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class DPSApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector : DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        VK.initialize(this)
        ApplicationInjector.init(this)

    }

    override fun activityInjector() = activityInjector
}