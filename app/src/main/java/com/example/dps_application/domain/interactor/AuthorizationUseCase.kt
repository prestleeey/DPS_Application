package com.example.dps_application.domain.interactor

import com.example.dps_application.domain.executor.PostExecutionThread
import com.example.dps_application.domain.executor.ThreadExecutor
import com.example.dps_application.domain.interactor.base.SingleUseCase
import com.example.dps_application.domain.repository.TokenRepository
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.domain.model.AccessToken
import com.example.dps_application.domain.model.User
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class AuthorizationUseCase
@Inject constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
): SingleUseCase<Unit, String>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseSingle(params: String?) =
        Single.zip(
            userRepository.getUserInfo(),
            tokenRepository.authorization(params ?: ""),
            BiFunction<User, AccessToken, Unit> { user, tokenServer ->
                userRepository.saveUserInfo(user)
                tokenRepository.saveToken(tokenServer)
            }
        )
        .retry(2)

}