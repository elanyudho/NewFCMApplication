package com.dicoding.fcmapplication.ui.other.adddata.addfat

import android.app.Activity
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
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
import com.dicoding.fcmapplication.utils.variables.longlat.LongLatRegex
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

    private var isDefault = true

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

        checkDataChanged()

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

        binding.etRepairNote.setOnTouchListener({ view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

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
                isEmpty = true
            }
            if (etTotalCore.text.isNullOrEmpty()) {
                etTotalCoreInputLayout.error = "This field is required"
                isEmpty = true
            }
            if (etCoreUsed.text.isNullOrEmpty()) {
                etCoreUsedInputLayout.error = "This field is required"
                isEmpty = true
            }
            if (etCoreBackup.text.isNullOrEmpty()) {
                etCoreBackupInputLayout.error = "This field is required"
                isEmpty = true
            }
            if (etLongitude.text.isNullOrEmpty()) {
                etLongitudeInputLayout.error = "This field is required"
                isEmpty = true
            } else {
                if (!etLongitude.text.matches(LongLatRegex.LONGITUDE_PATTERN)) {
                    etLongitudeInputLayout.error = "Enter the longitude number with the correct format"
                    isEmpty = true
                }
            }
            if (etLatitude.text.isNullOrEmpty()) {
                etLatitudeInputLayout.error = "This field is required"
                isEmpty = true
            } else {
                if (!etLatitude.text.matches(LongLatRegex.LATITUDE_PATTERN)) {
                    etLatitudeInputLayout.error = "Enter the latitude number with the correct format"
                    isEmpty = true
                }
            }
            if (etActivationDate.text.isNullOrEmpty()) {
                etActivationDateInputLayout.error = "This field is required"
                isEmpty = true
            }else{
                etActivationDateInputLayout.error = null
                isEmpty = false
            }
            if (etLoss.text.isNullOrEmpty()) {
                etLossInputLayout.error = "This field is required"
                isEmpty = true
            }
            if (etCoveredHome.text.isNullOrEmpty()) {
                etCoveredHomeInputLayout.error = "This field is required"
                isEmpty = true
            }
            if (etChooseFat.text.isNullOrEmpty()) {
                etChooseFatInputLayout.error = "This field is required"
                isEmpty = true
            }else{
                etChooseFat.error = null
            }

            //check everything is valid
            if (isEmpty) {
                return
            } else {
                if(purposeOpen == TO_EDIT){
                    val postFDT = PostFAT(

                        fat_name = etFatName.text.toString().uppercase(),
                        fat_total_core = etTotalCore.text.toString(),
                        fat_core_used = etCoreUsed.text.toString(),
                        fat_backup_core = etCoreBackup.text.toString(),
                        fat_loss = etLoss.text.toString(),
                        home_covered = etCoveredHome.text.toString(),
                        fat_activated = etActivationDate.text.toString(),
                        fat_region = session.user?.region.toString(),
                        fat_in_repair = isService,
                        fat_location = etLatitude.text.toString() + "," + etLongitude.text.toString(),
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

                        fat_name = etFatName.text.toString().uppercase(),
                        fat_total_core = etTotalCore.text.toString(),
                        fat_core_used = etCoreUsed.text.toString(),
                        fat_backup_core = etCoreBackup.text.toString(),
                        fat_loss = etLoss.text.toString(),
                        home_covered = etCoveredHome.text.toString(),
                        fat_activated = etActivationDate.text.toString(),
                        fat_region = session.user?.region.toString(),
                        fat_in_repair = isService,
                        fat_location = etLatitude.text.toString() + "," + etLongitude.text.toString(),
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
        if (isDefault) {
            onBackPressed()
        }else {
            val dialogFragment = BackConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "back_Confirmation")
        }
        hideKeyboard(this)
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
                    etChooseFatInputLayout.error = "This field is required"
                    isEmpty = true
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
        val latLng = fatDetail.fatLocation?.split(",")
        with(binding) {
            etChooseFat.setText(fatDetail.fdtBind?.fdtName)
            etFatName.setText(fatDetail.fatName)
            etTotalCore.setText(fatDetail.fatCore)
            etCoreUsed.setText(fatDetail.fatCoreUsed)
            etCoreBackup.setText(fatDetail.fatCoreRemaining)
            etCoveredHome.setText(fatDetail.fatCoveredHome)
            etLongitude.setText(latLng?.get(1) ?: "")
            etLatitude.setText(latLng?.get(0) ?: "")
            etLoss.setText(fatDetail.fatLoss)
            etActivationDate.setText(fatDetail.fatActivationDate)
            btnSwRepair.isChecked = fatDetail.fatIsService!!
            etRepairNote.setText(fatDetail.fatNote)
        }
    }
    private fun checkDataChanged() {
        with(binding) {
            etFatName.doAfterTextChanged {
                etFatNameInputLayout.error = null
                isDefault = false
            }

            etChooseFat.doAfterTextChanged {
                etChooseFatInputLayout.error = null
                isDefault = false
            }

            etTotalCore.doAfterTextChanged {
                etTotalCoreInputLayout.error = null
                isDefault = false
            }

            etCoreUsed.doAfterTextChanged {
                etCoreUsedInputLayout.error = null
                isDefault = false
            }

            etCoreBackup.doAfterTextChanged {
                etCoreBackupInputLayout.error = null
                isDefault = false
            }

            etLongitude.doAfterTextChanged {
                etLongitudeInputLayout.error = null
                isDefault = false
            }

            etLatitude.doAfterTextChanged {
                etLatitudeInputLayout.error = null
                isDefault = false
            }

            etCoveredHome.doAfterTextChanged {
                etCoveredHomeInputLayout.error = null
                isDefault = false
            }

            etActivationDate.doAfterTextChanged {
                etActivationDateInputLayout.error = null
                isDefault = false
            }

            etLoss.doAfterTextChanged {
                etLossInputLayout.error = null
                isDefault = false
            }

            btnSwRepair.setOnClickListener {
                isDefault = fatDetail?.fatIsService == btnSwRepair.isChecked
            }

            etRepairNote.doAfterTextChanged {
                isDefault = false
            }
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun cursorIsVisible() {
        with(binding) {
            etFatName.isCursorVisible = true
            etCoreUsed.isCursorVisible = true
            etTotalCore.isCursorVisible = true
            etLoss.isCursorVisible = true
            etRepairNote.isCursorVisible = true
            etLongitude.isCursorVisible = true
            etLatitude.isCursorVisible = true
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
            etLongitude.isCursorVisible = false
            etLatitude.isCursorVisible = false
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