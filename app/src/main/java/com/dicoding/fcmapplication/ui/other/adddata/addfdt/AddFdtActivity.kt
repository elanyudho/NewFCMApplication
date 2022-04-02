package com.dicoding.fcmapplication.ui.other.adddata.addfdt

import android.app.DatePickerDialog
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.RequestResults
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityAddFdtBinding
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.ui.other.dialog.BackConfirmationDialogFragment
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddFdtActivity : BaseActivityBinding<ActivityAddFdtBinding>(),
    Observer<AddFdtViewModel.AddFdtUiState> {

    @Inject
    lateinit var mViewModel: AddFdtViewModel

    @Inject
    lateinit var session: Session

    private var isEmpty = false

    private var isDefault = false

    private var isService = false

    override val bindingInflater: (LayoutInflater) -> ActivityAddFdtBinding
        get() = { ActivityAddFdtBinding.inflate(layoutInflater) }

    override fun setupView() {

        mViewModel.uiState.observe(this, this)

        getPurposeIntent()

        setDatePicker()

        binding.headerAddData.tvTitleHeader.text = getString(R.string.add_fdt)
        binding.headerAddData.btnBack.setOnClickListener {
            doBackPage()
        }

        binding.btnSwRepair.setOnCheckedChangeListener { _, isChecked ->
            isService = isChecked
        }

        binding.btnSave.setOnClickListener {
            doAddData(isService)
        }


    }

    override fun onChanged(state: AddFdtViewModel.AddFdtUiState?) {
        when (state) {
            is AddFdtViewModel.AddFdtUiState.SuccessPostFdtData -> {
                fancyToast(getString(R.string.success_post_fdt), FancyToast.SUCCESS)
                onBackPressed()
            }
            is AddFdtViewModel.AddFdtUiState.LoadingPostFdtData -> {
                binding.cvLottieLoading.visible()
                cursorIsNotVisible()
            }
            is AddFdtViewModel.AddFdtUiState.FailedPostFdtData -> {
                binding.cvLottieLoading.gone()
                cursorIsVisible()
                handleFailure(state.failure)
            }
        }
    }

    private fun doAddData(isService: Boolean) {
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
            if (etLoss.text.isNullOrEmpty()) {
                etLoss.error = "This field is required"
                etLoss.requestFocus()
                isEmpty = true
            }

            //check everything is valid
            if (isEmpty) {
                return
            } else {
                val postFDT = PostFDT(
                    fdt_name = etFdtName.text.toString(),
                    fdt_total_core = etTotalCore.text.toString(),
                    fdt_core_used = etCoreUsed.text.toString(),
                    fdt_backup_core = etCoreBackup.text.toString(),
                    fdt_loss = etLoss.text.toString(),
                    fdt_activated = etActivationDate.text.toString(),
                    fdt_region = session.user?.region.toString(),
                    fdt_in_repair = isService,
                    fdt_location = etLocation.text.toString(),
                    fdt_note = if (etRepairNote.text.isNullOrEmpty()) {
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

    private fun getPurposeIntent() {
        val purposeOpen = intent.getStringExtra(PURPOSE_OPEN)
        if (purposeOpen == TO_ADD) {
            binding.grpCoveredFdt.gone()
        } else {
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
            DatePickerDialog(
                this,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun handleFailure(failure: Failure) {
        if (failure.requestResults == RequestResults.NO_CONNECTION) {
            fancyToast(getString(R.string.error_unstable_network), FancyToast.ERROR)
        } else {
            fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
        }
    }

    private fun cursorIsVisible() {
        with(binding) {
            etFdtName.isCursorVisible = true
            etCoreUsed.isCursorVisible = true
            etTotalCore.isCursorVisible = true
            etLoss.isCursorVisible = true
            etRepairNote.isCursorVisible = true
            etLocation.isCursorVisible = true
            etCoreBackup.isCursorVisible = true
        }
    }

    private fun cursorIsNotVisible() {
        with(binding) {
            etFdtName.isCursorVisible = false
            etCoreUsed.isCursorVisible = false
            etTotalCore.isCursorVisible = false
            etLoss.isCursorVisible = false
            etRepairNote.isCursorVisible = false
            etLocation.isCursorVisible = false
            etCoreBackup.isCursorVisible = false
        }
    }


    private fun doBackPage() {
        with(binding) {
            isDefault = (etFdtName.text.isNullOrEmpty() || etTotalCore.text.isNullOrEmpty() || etCoreUsed.text.isNullOrEmpty()
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

    companion object {
        const val PURPOSE_OPEN = "purpose_open"
        const val TO_ADD = "to_add"
        const val TO_EDIT = "to_edit"
    }

}