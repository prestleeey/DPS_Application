package com.example.dps_application.ui.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dps_application.util.SingleLiveEvent
import com.example.dps_application.domain.interactor.AuthorizationUseCase
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class AuthorizationViewModel
@Inject constructor(private val authorizationUseCase: AuthorizationUseCase) : ViewModel() {

    private val logInEvent = SingleLiveEvent<Unit>()

    fun getLogInEvent() = logInEvent

    fun saveUserInfo(token : String) = authorizationUseCase.execute(object :DisposableSingleObserver<Unit>() {
        override fun onSuccess(t: Unit) {
            logInEvent.call()
        }

        override fun onError(e: Throwable) {
            Log.d("1337", "error: $e")
        }

    }, token)

    override fun onCleared() {
        authorizationUseCase.dispose()
        super.onCleared()
    }

}