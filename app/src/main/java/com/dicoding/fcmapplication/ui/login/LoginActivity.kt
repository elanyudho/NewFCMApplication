package com.dicoding.fcmapplication.ui.login

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.RequestResults
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.EncryptedPreferences
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityLoginBinding
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.ui.main.MainActivity
import com.dicoding.fcmapplication.ui.register.RegisterActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivityBinding<ActivityLoginBinding>(),
    Observer<LoginViewModel.LoginUiState> {

    @Inject
    lateinit var mViewModel: LoginViewModel

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    @Inject
    lateinit var session: Session

    private var isClick = true

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = { ActivityLoginBinding.inflate(it) }

    override fun setupView() {

        mViewModel.uiState.observe(this, this)

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        if (isClick){
            enable(binding.etUsername)
            enable(binding.etPassword)
        }else{
            disable(binding.etUsername)
            disable(binding.etPassword)
        }
    }

    override fun onChanged(state: LoginViewModel.LoginUiState?) {
        when (state) {
            is LoginViewModel.LoginUiState.SuccessApi -> {
                binding.cvLottieLoading.gone()
                saveStateUser(state.user)
            }
            is LoginViewModel.LoginUiState.Loading -> {
                isClick = false
                with(binding) {
                    cvLottieLoading.visible()
                    etUsername.isCursorVisible = false
                    etPassword.isCursorVisible = false
                }
            }

            is LoginViewModel.LoginUiState.Failed -> {
                isClick = true
                with(binding) {
                    cvLottieLoading.gone()
                    etUsername.isCursorVisible = true
                    etPassword.isCursorVisible = true
                }
                handleFailure(state.failure)

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun doLogin() {

        var isEmpty = false

        with(binding) {

            if (etUsername.text.isNullOrEmpty()) {
                etUsername.error = "This field is required"
                etUsername.requestFocus()
                isEmpty = true
            }
            if (etPassword.text.isNullOrEmpty()) {
                binding.etPassword.error = "This field is required"
                binding.etPassword.requestFocus()
                isEmpty = true
            }

            if (isEmpty) {
                return
            } else {
                mViewModel.doLogin(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }

        }
    }

    private fun handleFailure(failure: Failure) {
        if (failure.requestResults == RequestResults.NO_CONNECTION) {
            fancyToast(getString(R.string.error_unstable_network), FancyToast.ERROR)
        } else {
            if (failure.code == "400") {
                fancyToast("Incorrect username or password. Please check again", FancyToast.ERROR)
            } else {
                fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }

        }
        Log.d("Session Login", session.isLogin.toString())

        //e { "Type: ${failure.requestResults} || Body: ${failure.throwable.message}" }
    }

    private fun saveStateUser(user: User) {
        encryptedPreferences.encryptedToken = user.token
        session.isLogin = true
        session.user = user
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

}