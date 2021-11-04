package com.dicoding.fcmapplication.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.dicoding.fcmapplication.utils.extensions.disable
import com.dicoding.fcmapplication.utils.extensions.enable
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.network.NetworkInterceptor
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
    lateinit var session : Session

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = { ActivityLoginBinding.inflate(it) }

    override fun setupView() {

        mViewModel.uiState.observe(this,this)

        binding.btnLogin.setOnClickListener {
            doLogin()
        }
    }

    override fun onChanged(state: LoginViewModel.LoginUiState?) {
        when (state) {
            is LoginViewModel.LoginUiState.SuccessApi -> {
                with(binding) {
                    etUsername.setText("")
                    etPassword.setText("")
                    enable(btnLogin)
                }
                saveStateUser(state.user)
            }
            is LoginViewModel.LoginUiState.Loading -> {
                with(binding) {
                    disable(btnLogin)
                }
            }

            is LoginViewModel.LoginUiState.Failed -> {
                with(binding) {
                    etUsername.setText("")
                    etPassword.setText("")
                    enable(btnLogin)
                }

                handleFailure(state.failure)
            }
        }
    }

    private fun doLogin() {

        with(binding) {

            if (etUsername.text.isNullOrEmpty()) {
                etUsername.error = "Bidang ini wajib diisi"
                etUsername.requestFocus()
            }
            if (etPassword.text.isNullOrEmpty()) {
                binding.etPassword.error = "Bidang ini wajib diisi"
                binding.etPassword.requestFocus()
            }else {
                mViewModel.doLogin(
                    binding.etUsername.text.toString(),
                    binding.etPassword.text.toString()
                )
            }

        }
    }

    private fun handleFailure(failure: Failure) {
        if (failure.requestResults == RequestResults.NO_CONNECTION) {
            fancyToast(getString(R.string.error_unstable_network), FancyToast.ERROR)
        } else {
            if (failure.code == "400") {
                fancyToast("Email dan password salah. Silahkan cek kembali", FancyToast.ERROR)
            } else {
                fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }

        }

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