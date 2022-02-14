package com.dicoding.fcmapplication.ui.other.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentOtherBinding
import com.dicoding.fcmapplication.ui.other.companyprofile.CompanyProfileActivity
import com.dicoding.fcmapplication.ui.other.dialog.LogoutDialogFragment
import com.dicoding.fcmapplication.ui.other.repairlist.RepairListActivity
import com.dicoding.fcmapplication.utils.extensions.invisible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtherFragment : BaseFragmentBinding<FragmentOtherBinding>() {

    @Inject
    lateinit var session: Session

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOtherBinding
        get() = {layoutInflater, viewGroup, b ->
            FragmentOtherBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun setupView() {
        callOnceWhenCreated {
            with(binding) {
                headerOther.btnBack.invisible()
                headerOther.tvTitleHeader.text = getString(R.string.others)

                tvUserName.text = session.user?.username

                rowCompanyProfile.setOnClickListener {
                    startActivity(Intent(requireContext(), CompanyProfileActivity::class.java))
                }

                rowRepairList.setOnClickListener {
                    startActivity(Intent(requireContext(), RepairListActivity::class.java))
                }

                rowAddFdtFat.setOnClickListener {  }

                rowRegionAdmin.setOnClickListener {  }

                rowLogout.setOnClickListener {
                    val dialogFragment = LogoutDialogFragment()
                    dialogFragment.show(childFragmentManager, "dialog")
                }
        }

        }
    }

}