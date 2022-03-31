package com.dicoding.fcmapplication.ui.other.adddata.addfdt

import android.app.DatePickerDialog
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityAddFdtBinding
import com.dicoding.fcmapplication.ui.other.dialog.BackConfirmationDialogFragment
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddFdtActivity : BaseActivityBinding<ActivityAddFdtBinding>() {

    private var isEmpty = false

    override val bindingInflater: (LayoutInflater) -> ActivityAddFdtBinding
        get() = { ActivityAddFdtBinding.inflate(layoutInflater) }

    override fun setupView() {

        getPurposeIntent()

        setDatePicker()

        binding.headerAddData.tvTitleHeader.text = getString(R.string.add_fdt)
        binding.headerAddData.btnBack.setOnClickListener {
            val dialogFragment = BackConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "back_Confirmation")
        }

        binding.btnSwRepair.setOnCheckedChangeListener{ _, isChecked ->

        }

        binding.btnSave.setOnClickListener {
            doAddData()
        }


    }

    private fun doAddData() {
        with(binding) {
            if (etFdtName.text.isNullOrEmpty()) {
                etFdtName.error = "This field is required"
                etFdtName.requestFocus()
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
            
            //check everything is valid
            if (isEmpty) {
                return
            }else{
                // TODO: Send data 
            }
        }
    }

    private fun setActivationDate(calendar: Calendar) {
        val calFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(calFormat, Locale.UK)
        binding.etActivationDate.setText(sdf.format(calendar.time))
    }

    private fun getPurposeIntent() {
        val purposeOpen = intent.getStringExtra(PURPOSE_OPEN)
        if (purposeOpen == TO_ADD){
            binding.grpCoveredFdt.gone()
        }
        else{
            binding.grpCoveredFdt.visible()
        }
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
            DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    companion object {
        const val PURPOSE_OPEN = "purpose_open"
        const val TO_ADD = "to_add"
        const val TO_EDIT = "to_edit"
    }

}