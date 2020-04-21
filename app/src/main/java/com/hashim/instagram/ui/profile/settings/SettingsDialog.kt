package com.hashim.instagram.ui.profile.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.hashim.instagram.R
import com.hashim.instagram.di.component.DialogFragmentComponent
import com.hashim.instagram.ui.base.BaseDialog
import com.hashim.instagram.ui.main.MainSharedViewModel
import kotlinx.android.synthetic.main.dialog_settings_layout.*
import javax.inject.Inject

class SettingsDialog : BaseDialog<SettingsDialogViewModel>() {

    companion object {

        const val TAG = "SettingsDialog"

        fun newInstance(): SettingsDialog {
            val args = Bundle()
            val fragment = SettingsDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.dialog_settings_layout

    override fun injectDependencies(dialogComponent: DialogFragmentComponent) =
        dialogComponent.inject(this)


    override fun setupView(view: View) {

        btCancel.setOnClickListener {
            viewModel.doDismissDialog()
        }

        btApply.setOnClickListener {
            viewModel.doApplySettings(rgM.checkedRadioButtonId)
        }
    }


    override fun setupObservers() {
        super.setupObservers()
        viewModel.dismissDialog.observe(this, Observer {
            dismiss()
        })

        viewModel.rDefault.observe(this, Observer {
            rbSystemDefault.isChecked = true
        })

        viewModel.rLight.observe(this, Observer {
            rbLight.isChecked = true
        })

        viewModel.rDark.observe(this, Observer {
            rbDark.isChecked = true
        })



    }


}