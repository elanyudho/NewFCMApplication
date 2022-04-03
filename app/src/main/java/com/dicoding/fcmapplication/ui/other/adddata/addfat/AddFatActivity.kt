package com.dicoding.fcmapplication.ui.other.adddata.addfat

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityAddFatBinding
import com.dicoding.fcmapplication.databinding.ActivityAddFdtBinding
import com.dicoding.fcmapplication.domain.model.PostFAT
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.ui.other.dialog.BackConfirmationDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddFatActivity : BaseActivityBinding<ActivityAddFatBinding>(),
    Observer<AddFatViewModel.AddFatUiState> {

    @Inject
    lateinit var mViewModel: AddFatViewModel

    @Inject
    lateinit var session: Session

    private var isEmpty = false

    private var isDefault = false

    private var isService = false

    override val bindingInflater: (LayoutInflater) -> ActivityAddFatBinding
        get() = { ActivityAddFatBinding.inflate(layoutInflater) }

    override fun setupView() {
        mViewModel.uiState.observe(this, this)

        setDatePicker()

        binding.headerAddData.tvTitleHeader.text = getString(R.string.add_fat)
        binding.headerAddData.btnBack.setOnClickListener {
            doBackPage()
        }
    }

    override fun onChanged(t: AddFatViewModel.AddFatUiState?) {
        TODO("Not yet implemented")
    }

    private fun doAddData(isService: Boolean) {
        with(binding) {
            if (etFatName.text.isNullOrEmpty()) {
                etFatName.error = "This field is required"
                etFatName.requestFocus()
                isEmpty = true
            }
            if (etTotalCore.text.isNullOrEmpty()) {
                etTotalCore.error = "This field is required"
                etTotalCore.requestFocus()
                isEmpty = true
            }
            if (etCoreUsed.text.isNullOrEmpty()) {
                etCoreUsed.error = "This field is required"
                etCoreUsed.requestFocus()
                isEmpty = true
            }
            if (etCoreBackup.text.isNullOrEmpty()) {
                etCoreBackup.error = "This field is required"
                etCoreBackup.requestFocus()
                isEmpty = true
            }
            if (etLocation.text.isNullOrEmpty()) {
                etLocation.error = "This field is required"
                etLocation.requestFocus()
                isEmpty = true
            }
            if (etActivationDate.text.isNullOrEmpty()) {
                etActivationDate.error = "This field is required"
                etActivationDate.requestFocus()
                isEmpty = true
            }
            if (etLoss.text.isNullOrEmpty()) {
                etLoss.error = "This field is required"
                etLoss.requestFocus()
                isEmpty = true
            }

            //check everything is valid
            if (isEmpty) {
                return
            } else {
                val postFDT = PostFAT(

                    fat_name = etFatName.text.toString(),
                    fat_total_core = etTotalCore.text.toString(),
                    fat_core_used = etCoreUsed.text.toString(),
                    fat_backup_core = etCoreBackup.text.toString(),
                    fat_loss = etLoss.text.toString(),
                    home_covered = etCoveredHome.toString(),
                    fat_activated = etActivationDate.text.toString(),
                    fat_region = session.user?.region.toString(),
                    fat_in_repair = isService,
                    fat_location = etLocation.text.toString(),
                    fat_note = if (etRepairNote.text.isNullOrEmpty()) {
                        "None"
                    } else {
                        etRepairNote.text.toString()
                    }
                )
                mViewModel.postFdtData(postFDT)
            }
        }
    }

    private fun setActivationDate(calendar: Calendar) {
        val calFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(calFormat, Locale.UK)
        binding.etActivationDate.setText(sdf.format(calendar.time))
    }

    private fun setDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setActivationDate(calendar)
        }

        binding.etActivationDate.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun doBackPage() {
        with(binding) {
            isDefault = (etFatName.text.isNullOrEmpty() || etTotalCore.text.isNullOrEmpty() || etCoreUsed.text.isNullOrEmpty()
                    || etCoreBackup.text.isNullOrEmpty() || etLocation.text.isNullOrEmpty() || etActivationDate.text.isNullOrEmpty()
                    || etLoss.text.isNullOrEmpty() || etRepairNote.text.isNullOrEmpty() || !isService)
        }
        if (isDefault) {
            onBackPressed()
        }else {
            val dialogFragment = BackConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "back_Confirmation")
        }
    }


}