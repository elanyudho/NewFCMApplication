package com.dicoding.fcmapplication.ui.other.adddata.addfat

import android.app.Activity
import android.app.DatePickerDialog
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.RequestResults
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityAddFatBinding
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.domain.model.PostFAT
import com.dicoding.fcmapplication.ui.dialogfilter.bottomdialogchoosefdt.BottomDialogChooseFdtFragment
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
class AddFatActivity : BaseActivityBinding<ActivityAddFatBinding>(),
    Observer<AddFatViewModel.AddFatUiState> {

    @Inject
    lateinit var mViewModel: AddFatViewModel

    @Inject
    lateinit var session: Session

    private var isEmpty = false

    private var isDefault = false

    private var isService = false

    private var idFdtToAdd: String = ""

    private var fatDetail: FatDetail? = null

    private var purposeOpen = ""

    override val bindingInflater: (LayoutInflater) -> ActivityAddFatBinding
        get() = { ActivityAddFatBinding.inflate(layoutInflater) }

    override fun setupView() {
        mViewModel.uiState.observe(this, this)

        getFatDetail()
        getPurposeIntent()
        fatDetail?.let { setFatDetailToField(it) }
        idFdtToAdd = fatDetail?.fdtBind?.fdtId.toString()

        setDatePicker()

        if(purposeOpen == TO_EDIT){
            binding.headerAddData.tvTitleHeader.text = getString(R.string.edit_fat)
        }else{
            binding.headerAddData.tvTitleHeader.text = getString(R.string.add_fat)
        }
        binding.headerAddData.btnBack.setOnClickListener {
            doBackPage()
        }

        binding.btnSwRepair.setOnCheckedChangeListener { _, isChecked ->
            isService = isChecked
        }

        binding.etChooseFat.setOnClickListener {
            showChooseFdt()
        }

        binding.btnSave.setOnClickListener { doAddData(isService, fatDetail?.fatId.toString()) }
    }

    override fun onChanged(state: AddFatViewModel.AddFatUiState?) {
        when(state){
            is AddFatViewModel.AddFatUiState.SuccessPostOrPutFatData -> {
                fancyToast(getString(R.string.success_post_fat), FancyToast.SUCCESS)
                setResult(Activity.RESULT_OK)
                onBackPressed()
            }
            is AddFatViewModel.AddFatUiState.Loading -> {
                binding.cvLottieLoading.visible()
                cursorIsNotVisible()
            }
            is AddFatViewModel.AddFatUiState.FailedLoadedOrTransaction -> {
                binding.cvLottieLoading.gone()
                cursorIsVisible()
                handleFailure(state.failure)
            }
        }
    }

    private fun doAddData(isService: Boolean, id: String) {
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
            }else{
                etActivationDate.error = null
                etActivationDate.clearFocus()
                isEmpty = false
            }
            if (etLoss.text.isNullOrEmpty()) {
                etLoss.error = "This field is required"
                etLoss.requestFocus()
                isEmpty = true
            }
            if (etChooseFat.text.isNullOrEmpty()) {
                etChooseFat.error = "This field is required"
                etChooseFat.requestFocus()
                isEmpty = true
            }else{
                etChooseFat.error = null
                etChooseFat.clearFocus()
            }

            //check everything is valid
            if (isEmpty) {
                return
            } else {
                if(purposeOpen == TO_EDIT){
                    val postFDT = PostFAT(

                        fat_name = etFatName.text.toString(),
                        fat_total_core = etTotalCore.text.toString(),
                        fat_core_used = etCoreUsed.text.toString(),
                        fat_backup_core = etCoreBackup.text.toString(),
                        fat_loss = etLoss.text.toString(),
                        home_covered = etCoveredHome.text.toString(),
                        fat_activated = etActivationDate.text.toString(),
                        fat_region = session.user?.region.toString(),
                        fat_in_repair = isService,
                        fat_location = etLocation.text.toString(),
                        fat_note = if (etRepairNote.text.isNullOrEmpty()) {
                            "None"
                        } else {
                            etRepairNote.text.toString()
                        },
                        fdt = PostFAT.FDT(idFdtToAdd)
                    )
                    mViewModel.putFatData(id, postFDT)
                }else{
                    val postFDT = PostFAT(

                        fat_name = etFatName.text.toString(),
                        fat_total_core = etTotalCore.text.toString(),
                        fat_core_used = etCoreUsed.text.toString(),
                        fat_backup_core = etCoreBackup.text.toString(),
                        fat_loss = etLoss.text.toString(),
                        home_covered = etCoveredHome.text.toString(),
                        fat_activated = etActivationDate.text.toString(),
                        fat_region = session.user?.region.toString(),
                        fat_in_repair = isService,
                        fat_location = etLocation.text.toString(),
                        fat_note = if (etRepairNote.text.isNullOrEmpty()) {
                            "None"
                        } else {
                            etRepairNote.text.toString()
                        },
                        fdt = PostFAT.FDT(idFdtToAdd)
                    )
                    mViewModel.postFdtData(postFDT)
                }

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
                    || etLoss.text.isNullOrEmpty() || etRepairNote.text.isNullOrEmpty() || !isService || etChooseFat.text.isNullOrEmpty())
        }
        if (isDefault) {
            onBackPressed()
        }else {
            val dialogFragment = BackConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "back_Confirmation")
        }
    }



    private fun handleFailure(failure: Failure) {
        if (failure.requestResults == RequestResults.NO_CONNECTION) {
            fancyToast(getString(R.string.error_unstable_network), FancyToast.ERROR)
        } else {
            fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
        }
    }

    private fun showChooseFdt() {
        //init dialog
        val bottomDialogChooseFdt = BottomDialogChooseFdtFragment()
        bottomDialogChooseFdt.show(supportFragmentManager, BottomDialogChooseFdtFragment::class.java.simpleName)

        bottomDialogChooseFdt.setOnClickItemListener(
            titleDialog = getString(R.string.choose_fdt)
        ) { data ->
            //set region field
            with(binding) {

                binding.etChooseFat.setText(data.fdtName)
                idFdtToAdd = data.fdtId
                if (etChooseFat.text.isNullOrEmpty()) {
                    etChooseFat.error = "This field is required"
                    etChooseFat.requestFocus()
                    isEmpty = true
                }else{
                    etChooseFat.error = null
                    etChooseFat.clearFocus()
                }
            }
            bottomDialogChooseFdt.dismiss()
        }
    }

    private fun getPurposeIntent() {
        purposeOpen = intent.extras?.getString(PURPOSE_OPEN) ?: ""
        purposeOpen = if (purposeOpen == TO_ADD) {
            TO_ADD
        } else {
            TO_EDIT
        }
    }

    private fun getFatDetail() {
        fatDetail = intent.extras?.getParcelable(FAT_DETAIL)
    }

    private fun setFatDetailToField(fatDetail: FatDetail) {
        with(binding) {
            etChooseFat.setText(fatDetail.fdtBind?.fdtName)
            etFatName.setText(fatDetail.fatName)
            etTotalCore.setText(fatDetail.fatCore)
            etCoreUsed.setText(fatDetail.fatCoreUsed)
            etCoreBackup.setText(fatDetail.fatCoreRemaining)
            etCoveredHome.setText(fatDetail.fatCoveredHome)
            etLocation.setText(fatDetail.fatLocation)
            etLoss.setText(fatDetail.fatLoss)
            etActivationDate.setText(fatDetail.fatActivationDate)
            btnSwRepair.isChecked = fatDetail.fatIsService!!
            etRepairNote.setText(fatDetail.fatNote)
        }
    }

    private fun cursorIsVisible() {
        with(binding) {
            etFatName.isCursorVisible = true
            etCoreUsed.isCursorVisible = true
            etTotalCore.isCursorVisible = true
            etLoss.isCursorVisible = true
            etRepairNote.isCursorVisible = true
            etLocation.isCursorVisible = true
            etCoreBackup.isCursorVisible = true
            etCoveredHome.isCursorVisible = true
        }
    }

    private fun cursorIsNotVisible() {
        with(binding) {
            etFatName.isCursorVisible = false
            etCoreUsed.isCursorVisible = false
            etTotalCore.isCursorVisible = false
            etLoss.isCursorVisible = false
            etRepairNote.isCursorVisible = false
            etLocation.isCursorVisible = false
            etCoreBackup.isCursorVisible = false
            etCoveredHome.isCursorVisible = false
        }
    }

    companion object {
        const val FAT_DETAIL = "fat_detail"
        const val PURPOSE_OPEN = "purpose_open"
        const val TO_ADD = "to_add"
        const val TO_EDIT = "to_edit"
    }

}