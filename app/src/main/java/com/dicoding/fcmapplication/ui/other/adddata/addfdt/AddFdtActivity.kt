package com.dicoding.fcmapplication.ui.other.adddata.addfdt

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
import com.dicoding.fcmapplication.databinding.ActivityAddFdtBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.ui.dialogfilter.bottomdialogcoveredfat.BottomDialogCoveredFatFragment
import com.dicoding.fcmapplication.ui.other.adapter.CoveredFatAdapter
import com.dicoding.fcmapplication.ui.other.dialog.BackConfirmationDialogFragment
import com.dicoding.fcmapplication.ui.other.dialog.FatHasCoveredConfirmationFragment
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
class AddFdtActivity : BaseActivityBinding<ActivityAddFdtBinding>(),
    Observer<AddFdtViewModel.AddFdtUiState> {

    @Inject
    lateinit var mViewModel: AddFdtViewModel

    @Inject
    lateinit var session: Session

    private var isEmpty = false

    private var isService = false

    private var fdtDetail: FdtDetail? = null

    private var purposeOpen = ""

    private var isDefault = true

    private val coveredFatAdapter: CoveredFatAdapter by lazy { CoveredFatAdapter() }

    private var coveredFatList: List<FdtDetail.FatList> = mutableListOf()

    override val bindingInflater: (LayoutInflater) -> ActivityAddFdtBinding
        get() = { ActivityAddFdtBinding.inflate(layoutInflater) }

    override fun setupView() {

        mViewModel.uiState.observe(this, this)

        getPurposeIntent()
        getFdtDetail()
        fdtDetail?.let { setFdtDetailToField(it) }

        setDatePicker()

        checkDataChanged()

        if (purposeOpen == TO_EDIT) {
            binding.headerAddData.tvTitleHeader.text = getString(R.string.edit_fdt)
        } else {
            binding.headerAddData.tvTitleHeader.text = getString(R.string.add_fdt)
        }
        binding.headerAddData.btnBack.setOnClickListener {
            doBackPage()
        }

        binding.btnSwRepair.setOnCheckedChangeListener { _, isChecked ->
            isService = isChecked
        }

        binding.etRepairNote.setOnTouchListener({ view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        binding.btnSave.setOnClickListener {
            doAddData(isService, coveredFatList, fdtDetail?.fdtId.toString())
        }

    }

    override fun onChanged(state: AddFdtViewModel.AddFdtUiState?) {
        when (state) {
            is AddFdtViewModel.AddFdtUiState.SuccessPostOrPutFdtData -> {
                fancyToast(getString(R.string.success_post_fdt), FancyToast.SUCCESS)
                setResult(Activity.RESULT_OK)
                onBackPressed()
            }
            is AddFdtViewModel.AddFdtUiState.SuccessUpdateCoveredFatList -> {
                if (purposeOpen == TO_EDIT) {
                    coveredFatAdapter.submitList(state.coveredFatList)
                    coveredFatList = state.coveredFatList
                    binding.etCoveredFdt.setOnClickListener {
                        showCoveredFat(state.coveredFatList)
                    }
                    coveredFatAdapterActions(state.coveredFatList)
                }
            }
            is AddFdtViewModel.AddFdtUiState.Loading -> {
                binding.cvLottieLoading.visible()
                cursorIsNotVisible()
            }
            is AddFdtViewModel.AddFdtUiState.FailedLoadedOrTransaction -> {
                binding.cvLottieLoading.gone()
                cursorIsVisible()
                handleFailure(state.failure)
            }
        }
    }

    private fun doAddData(
        isService: Boolean,
        coveredFatList: List<FdtDetail.FatList> = emptyList(),
        fdtId: String = ""
    ) {
        with(binding) {

            if (etFdtName.text.isNullOrEmpty()) {
                etFdtNameInputLayout.error = "This field is required"
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
                    etLongitudeInputLayout.error =
                        "Enter the longitude number with the correct format"
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
            if (etLoss.text.isNullOrEmpty()) {
                etLossInputLayout.error = "This field is required"
                isEmpty = true
            }
            if (etActivationDate.text.isNullOrEmpty()) {
                etActivationDateInputLayout.error = "This field is required"
                isEmpty = true
            }

            //check everything is valid
            if (isEmpty) {
                return
            } else {
                if (purposeOpen == TO_EDIT) {
                    val postFDT = PostFDT(
                        fdt_name = etFdtName.text.toString().uppercase(),
                        fdt_total_core = etTotalCore.text.toString(),
                        fdt_core_used = etCoreUsed.text.toString(),
                        fdt_backup_core = etCoreBackup.text.toString(),
                        fdt_loss = etLoss.text.toString(),
                        fdt_activated = etActivationDate.text.toString(),
                        fdt_region = session.user?.region.toString(),
                        fdt_in_repair = isService,
                        fdt_location = etLongitude.text.toString() + "," + etLatitude.text.toString(),
                        fdt_note = if (etRepairNote.text.isNullOrEmpty()) {
                            "None"
                        } else {
                            etRepairNote.text.toString()
                        },
                        fat_covered_lists = coveredFatList.map { PostFDT.CoveredFAT(id = it.fatId.toString()) }
                    )
                    mViewModel.putFdtData(fdtId, postFDT)
                } else {
                    val postFDT = PostFDT(
                        fdt_name = etFdtName.text.toString().uppercase(),
                        fdt_total_core = etTotalCore.text.toString(),
                        fdt_core_used = etCoreUsed.text.toString(),
                        fdt_backup_core = etCoreBackup.text.toString(),
                        fdt_loss = etLoss.text.toString(),
                        fdt_activated = etActivationDate.text.toString(),
                        fdt_region = session.user?.region.toString(),
                        fdt_in_repair = isService,
                        fdt_location = etLatitude.text.toString() + "," + etLongitude.text.toString(),
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
    }

    private fun coveredFatAdapterActions(coveredFatList: List<FdtDetail.FatList>) {
        val mutableList = coveredFatList.toMutableList()
        with(binding.rvFatSelect) {
            adapter = coveredFatAdapter
            setHasFixedSize(true)

            coveredFatAdapter.setOnDeleteSkill { deleteData ->
                //for data changed
                isDefault = false
                mutableList.remove(deleteData)
                mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(mutableList))
            }
        }

    }

    private fun setActivationDate(calendar: Calendar) {
        val calFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(calFormat, Locale.UK)
        binding.etActivationDate.setText(sdf.format(calendar.time))
        if (binding.etActivationDate.text.isNullOrEmpty()) {
            binding.etActivationDateInputLayout.error = "This field is required"
            isEmpty = true
        }
    }

    private fun getPurposeIntent() {
        purposeOpen = intent.extras?.getString(PURPOSE_OPEN) ?: ""
        if (purposeOpen == TO_ADD) {
            purposeOpen = TO_ADD
            binding.grpCoveredFdt.gone()
        } else {
            purposeOpen = TO_EDIT
            binding.grpCoveredFdt.visible()
        }
    }

    private fun getFdtDetail() {
        fdtDetail = intent.extras?.getParcelable(FDT_DETAIL)
        fdtDetail?.fatCoveredList?.let {
            mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(it))
        }
    }

    private fun setDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
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
            etLongitude.isCursorVisible = true
            etLatitude.isCursorVisible = true
            etCoreBackup.isCursorVisible = true
        }
    }

    private fun cursorIsNotVisible() {
        with(binding) {
            etFdtName.isCursorVisible = false
            etCoreUsed.isCursorVisible = false
            etTotalCore.isCursorVisible = false
            etLoss.isCursorVisible = false
            etLongitude.isCursorVisible = false
            etLatitude.isCursorVisible = false
            etCoreBackup.isCursorVisible = false
        }
    }


    private fun doBackPage() {
        if (isDefault) {
            onBackPressed()
        } else {
            val dialogFragment = BackConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "back_Confirmation")
        }
        hideKeyboard(this)
    }

    private fun setFdtDetailToField(fdtDetail: FdtDetail) {
        val latLng = fdtDetail.fdtLocation?.split(",")
        with(binding) {
            etFdtName.setText(fdtDetail.fdtName)
            etTotalCore.setText(fdtDetail.fdtCore)
            etCoreUsed.setText(fdtDetail.fdtCoreUsed)
            etCoreBackup.setText(fdtDetail.fdtCoreRemaining)
            etLongitude.setText(latLng?.get(1) ?: "")
            etLatitude.setText(latLng?.get(0) ?: "")
            etLoss.setText(fdtDetail.fdtLoss)
            etActivationDate.setText(fdtDetail.fdtActivationDate)
            btnSwRepair.isChecked = fdtDetail.fdtIsService!!
            etRepairNote.setText(fdtDetail.fdtNote)
        }
    }

    private fun showCoveredFat(coveredFatList: List<FdtDetail.FatList>) {
        //init dialog
        val bottomDialogCoveredFat = BottomDialogCoveredFatFragment()
        bottomDialogCoveredFat.show(
            supportFragmentManager,
            BottomDialogCoveredFatFragment::class.java.simpleName
        )

        val mutableList = coveredFatList.toMutableList()
        var isNotSame = false

        bottomDialogCoveredFat.setOnClickItemListener(
            titleDialog = getString(R.string.choose_fat)
        ) { data ->
            if (data.fdt.isNullOrEmpty() || data.fdt == ""){
                if (mutableList.isEmpty()) {
                    mutableList.add(data)
                    mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(mutableList))
                } else {
                    for (it in mutableList) {
                        if (it == data) {
                            fancyToast("${data.fatName} has been added", FancyToast.INFO)
                            isNotSame = false
                            break
                        } else {
                            isNotSame = true
                        }
                    }
                    if (isNotSame){
                        mutableList.add(data)
                        mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(mutableList))
                    }
                }
            } else if (data.fdt == fdtDetail?.fdtName) {

                if (mutableList.isEmpty()) {
                    mutableList.add(data)
                    mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(mutableList))
                } else {
                    for (it in mutableList) {
                        if (it == data) {
                            fancyToast("${data.fatName} has been added", FancyToast.INFO)
                            isNotSame = false
                            break
                        } else {
                            isNotSame = true
                        }
                    }
                    if (isNotSame) {
                        mutableList.add(data)
                        mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(mutableList))
                    }
                }
            } else {
                val fatHasCoveredDialog = FatHasCoveredConfirmationFragment()
                fatHasCoveredDialog.show(
                    supportFragmentManager,
                    FatHasCoveredConfirmationFragment::class.java.simpleName
                )

                fatHasCoveredDialog.setConfirmationListener(fdtDetail?.fdtName.toString()) { isConfirm ->
                    if (isConfirm) {
                        for (it in mutableList) {
                            if (it == data) {
                                fancyToast("${data.fatName} has been added", FancyToast.INFO)
                                isNotSame = false
                                break
                            } else {
                                isNotSame = true
                            }
                        }
                        if (isNotSame){
                            mutableList.add(data)
                            mViewModel.doSomething(AddFdtViewModel.Event.UpdateCoveredFat(mutableList))
                        }
                    }
                }
            }
            //for data changed
            isDefault = false

            bottomDialogCoveredFat.dismiss()
        }
    }

    private fun checkDataChanged() {
        with(binding) {
            etFdtName.doAfterTextChanged {
                etFdtNameInputLayout.error = null
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

            etActivationDate.doAfterTextChanged {
                etActivationDateInputLayout.error = null
                isDefault = false
            }

            etLoss.doAfterTextChanged {
                etLossInputLayout.error = null
                isDefault = false
            }

            btnSwRepair.setOnClickListener {
                isDefault = fdtDetail?.fdtIsService == btnSwRepair.isChecked
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

    companion object {
        const val FDT_DETAIL = "fdt_detail"
        const val PURPOSE_OPEN = "purpose_open"
        const val TO_ADD = "to_add"
        const val TO_EDIT = "to_edit"
    }

}