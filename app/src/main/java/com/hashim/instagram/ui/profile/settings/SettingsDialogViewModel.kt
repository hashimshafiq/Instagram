package com.hashim.instagram.ui.profile.settings

import androidx.lifecycle.MutableLiveData
import com.hashim.instagram.R
import com.hashim.instagram.data.repository.UserRepository
import com.hashim.instagram.ui.base.BaseViewModel
import com.hashim.instagram.utils.ThemeManager
import com.hashim.instagram.utils.network.NetworkHelper
import com.hashim.instagram.utils.rx.CoroutineDispatchers


class SettingsDialogViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(coroutineDispatchers, networkHelper) {

    val dismissDialog : MutableLiveData<Boolean> = MutableLiveData()
    val rLight : MutableLiveData<Boolean> = MutableLiveData()
    val rDark : MutableLiveData<Boolean> = MutableLiveData()
    val rDefault : MutableLiveData<Boolean> = MutableLiveData()

    init {
        fetchCurrentlySelectedMode()
    }

    override fun onCreate() {}



    fun doDismissDialog(){
        dismissDialog.value = true
    }

    private fun fetchCurrentlySelectedMode(){
        userRepository.getThemeMode().let {
            when(it){
                null -> { }
                "Light", "light" ->{
                    rLight.value = true
                }
                "Dark", "dark" ->{
                    rDark.value = true
                }
                "default", "Default" ->{
                    rDefault.value = true
                }
            }
        }
    }

    fun doApplySettings(checkedRadioButtonId: Int) {
        when(checkedRadioButtonId){
            R.id.rbDark -> {
                userRepository.saveThemeMode("Dark")
                ThemeManager.applyTheme("Dark")
            }
            R.id.rbLight -> {
                userRepository.saveThemeMode("Light")
                ThemeManager.applyTheme("Light")

            }
            R.id.rbSystemDefault -> {
                userRepository.saveThemeMode("Default")
                ThemeManager.applyTheme("Default")
            }

        }
        userRepository.saveThemeChange(true)
        doDismissDialog()
    }





}