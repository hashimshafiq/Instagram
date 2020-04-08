package com.hashim.instagram.ui.profile.settings

import android.os.Bundle
import com.hashim.instagram.R
import com.hashim.instagram.di.component.DialogFragmentComponent
import com.hashim.instagram.ui.base.BaseDialog

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

    override fun injectDependencies(dialogComponent: DialogFragmentComponent) {
        dialogComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {

    }
}