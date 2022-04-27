package com.dicoding.fcmapplication.ui.other.adddata

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityAddDataBinding
import com.dicoding.fcmapplication.ui.other.adddata.addfat.AddFatActivity
import com.dicoding.fcmapplication.ui.other.adddata.addfdt.AddFdtActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDataActivity : BaseActivityBinding<ActivityAddDataBinding>() {

    private var resultLauncher : ActivityResultLauncher<Intent>? = null

    override val bindingInflater: (LayoutInflater) -> ActivityAddDataBinding
        get() = { ActivityAddDataBinding.inflate(layoutInflater) }

    override fun setupView() {

        setResultLauncher()

        with(binding){
            headerAddData.tvTitleHeader.setText(R.string.add_fd_fat)
            headerAddData.btnBack.setOnClickListener {
                onBackPressed()
            }
            rowAddFdt.setOnClickListener {
                val intent = Intent(this@AddDataActivity, AddFdtActivity::class.java)
                val extras = Bundle()
                extras.putString(AddFdtActivity.PURPOSE_OPEN, AddFdtActivity.TO_ADD)
                intent.putExtras(extras)
                resultLauncher?.launch(intent)
            }
            rowAddFat.setOnClickListener {
                val intent = Intent(this@AddDataActivity, AddFatActivity::class.java)
                val extras = Bundle()
                extras.putString(AddFdtActivity.PURPOSE_OPEN, AddFatActivity.TO_ADD)
                intent.putExtras(extras)
                resultLauncher?.launch(intent)
            }
        }
    }
    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
            }
        }
    }

}