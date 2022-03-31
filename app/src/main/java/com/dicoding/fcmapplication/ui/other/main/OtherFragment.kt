package com.dicoding.fcmapplication.ui.other.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentOtherBinding
import com.dicoding.fcmapplication.ui.other.adddata.AddDataActivity
import com.dicoding.fcmapplication.ui.other.companyprofile.CompanyProfileActivity
import com.dicoding.fcmapplication.ui.other.dialog.LogoutDialogFragment
import com.dicoding.fcmapplication.ui.other.repairlist.RepairListActivity
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.visible
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

            if (session.user?.isAdmin == true){
                if (session.user?.isCenterAdmin == true){
                    // TODO: add logic
                }
                else{
                    binding.rowAddFdtFat.visible()
                }
            }

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

                rowAddFdtFat.setOnClickListener {
                    startActivity(Intent(requireContext(), AddDataActivity::class.java))
                }

                rowRegionAdmin.setOnClickListener {  }

                rowLogout.setOnClickListener {
                    val dialogFragment = LogoutDialogFragment()
                    dialogFragment.show(childFragmentManager, "dialog")
                }
        }

        }
    }

}