package com.dicoding.fcmapplication.ui.other.dialog

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseDialogBinding
import com.dicoding.fcmapplication.data.pref.EncryptedPreferences
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentLogoutDialogBinding
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoutDialogFragment : BaseDialogBinding<FragmentLogoutDialogBinding>(false) {

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLogoutDialogBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentLogoutDialogBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    override fun setupView() {
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnYes.setOnClickListener {
            session.isLogin = false
            session.user = User()
            encryptedPreferences.clear()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            dismiss()
        }
    }


}