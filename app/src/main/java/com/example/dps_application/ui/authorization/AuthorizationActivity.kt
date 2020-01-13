package com.example.dps_application.ui.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dps_application.R
import com.example.dps_application.databinding.ActivityAuthorizationBinding
import com.example.dps_application.di.Injectable
import com.example.dps_application.ui.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import java.util.*
import javax.inject.Inject

class AuthorizationActivity : AppCompatActivity(), AuthorizationHandler, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val authorizationViewModel: AuthorizationViewModel by viewModels {
        viewModelFactory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityAuthorizationBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_authorization
            )
        binding.handler = this

        authorizationViewModel.getLogInEvent().observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
        })

        checkAuthorization()

    }

    private fun checkAuthorization() {
        if (VK.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun logIn() {
        VK.login(this, ArrayList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                authorizationViewModel.saveUserInfo(token.accessToken)
            }

            override fun onLoginFailed(errorCode: Int) {
                Log.d("1337", "onLoginFailed")
            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}