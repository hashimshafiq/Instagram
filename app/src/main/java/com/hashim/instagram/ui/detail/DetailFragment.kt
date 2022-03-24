package com.hashim.instagram.ui.detail

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hashim.instagram.R
import com.hashim.instagram.databinding.FragmentDetailBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.utils.common.GlideHelper

class DetailFragment : BaseFragment<DetailViewModel>() {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    companion object {

        const val TAG = "DetailFragment"

    }

    override fun provideLayoutId(): Int = R.layout.fragment_detail

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {


        _binding = FragmentDetailBinding.bind(view)

        val bundle = arguments
        if (bundle == null){
            showMessage("Empty Image")
            return
        }

        val args = DetailFragmentArgs.fromBundle(bundle)


        args.image.run {
            val glideRequest = Glide
                .with(requireContext())
                .load(GlideHelper.getProtectedUrl(url, headers))

            if (placeholderWidth > 0 && placeholderHeight > 0) {
                val params = binding.ivImage.layoutParams as ViewGroup.LayoutParams
                params.width = placeholderWidth
                params.height = placeholderHeight
                binding.ivImage.layoutParams = params
                glideRequest.apply {
                    apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                    apply(RequestOptions.placeholderOf(R.drawable.ic_photo))
                }
            }
            glideRequest.into(binding.ivImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}