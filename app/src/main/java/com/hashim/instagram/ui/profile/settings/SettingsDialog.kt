package com.hashim.instagram.ui.profile.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hashim.instagram.R
import com.hashim.instagram.databinding.DialogSettingsLayoutBinding
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

    private var _binding: DialogSettingsLayoutBinding? = null
    private val binding get() = _binding!!

    override fun provideLayoutId(): Int = R.layout.dialog_settings_layout

    override fun injectDependencies(dialogComponent: DialogFragmentComponent) =
        dialogComponent.inject(this)


    override fun setupView(view: View) {

        _binding = DialogSettingsLayoutBinding.bind(view)

        binding.btCancel.setOnClickListener {
            findNavController().navigate(R.id.action_settingsDialog_to_itemProfile)
        }

        binding.btApply.setOnClickListener {
            viewModel.doApplySettings(binding.rgM.checkedRadioButtonId)
        }
    }


    override fun setupObservers() {
        super.setupObservers()
        viewModel.dismissDialog.observe(this, {
            dismiss()
        })

        viewModel.rDefault.observe(this, {
            binding.rbSystemDefault.isChecked = true
        })

        viewModel.rLight.observe(this, {
            binding.rbLight.isChecked = true
        })

        viewModel.rDark.observe(this, {
            binding.rbDark.isChecked = true
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}