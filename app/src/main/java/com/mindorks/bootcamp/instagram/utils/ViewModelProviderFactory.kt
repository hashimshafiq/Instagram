package com.mindorks.bootcamp.instagram.utils


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * When ViewModel require parameters in the constructor then ViewModelProviders.of(activity).get(ViewModel.class) do not work
 * In this case we need to provide our own ViewModelProvider's Factory.
 * create method is called by Android Framework when it needs to create a ViewModel instance.
 * NOTE: When activity rotates then create method is not called but earlier instance of ViewModel is returned.
 * that is why creator is provided here so that Android Framework can create the ViewModel instance according to its need.
 * @T: It says that the ViewModelProviderFactory works with variable of type SplashViewModel
 * Example: T -> SplashViewModel,
 *
 */
@Singleton
class ViewModelProviderFactory<T : ViewModel>(
    private val kClass: KClass<T>, // KClass is the holder of class of type ViewModel that needs to be inject
    private val creator: () -> T // This is the Lambda function, this is provided be the ActivityModule/FragmentModule,
    // when creator lambda is called then that module creates and return the instance of ViewModel
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(kClass.java)) return creator() as T
        throw IllegalArgumentException("Unknown class name")
    }
}
