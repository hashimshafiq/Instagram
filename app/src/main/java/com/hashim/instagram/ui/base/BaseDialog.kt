package com.hashim.instagram.ui.base

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.hashim.instagram.InstagramApplication
import com.hashim.instagram.R
import com.hashim.instagram.di.component.DaggerDialogFragmentComponent
import com.hashim.instagram.di.component.DialogFragmentComponent
import com.hashim.instagram.di.module.DialogFragmentModule
import com.hashim.instagram.utils.display.Toaster
import javax.inject.Inject

abstract class BaseDialog<VM : BaseViewModel> : DialogFragment(){

    @Inject
    lateinit var viewModel: VM

    private var baseActivity : BaseActivity<BaseViewModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        setupObservers()
        viewModel.onCreate()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            baseActivity = context as BaseActivity<BaseViewModel>
            //baseActivity?.onAttachFragment(this)
        }
    }


//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = Dialog(requireActivity())
//        dialog.setContentView(provideLayoutId())
//        dialog.setCanceledOnTouchOutside(true)
//        return dialog
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_settings_layout,container,false)
    }

    private fun buildFragmentComponent() =
        DaggerDialogFragmentComponent
            .builder()
            .applicationComponent((requireContext().applicationContext as InstagramApplication).applicationComponent)
            .dialogFragmentModule(DialogFragmentModule(this))
            .build()



    protected open fun setupObservers() {
        viewModel.messageString.observe(baseActivity!!, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(baseActivity!!, Observer {
            it.data?.run { showMessage(this) }
        })
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    fun showMessage(message: String) = Toaster.show(baseActivity!!.applicationContext, message)

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    fun goBack() {
        if (activity is BaseActivity<*>) (activity as BaseActivity<*>).goBack()
    }

    open fun dismissDialog(tag: String?) {
        dismiss()
    }



    protected abstract fun provideLayoutId(): View

    protected abstract fun injectDependencies(dialogComponent: DialogFragmentComponent)

    protected abstract fun setupView(view: View)
}