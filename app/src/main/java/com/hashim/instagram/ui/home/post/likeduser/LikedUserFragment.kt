package com.hashim.instagram.ui.home.post.likeduser

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.databinding.FragmentLikedUserBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class LikedUserFragment : BaseFragment<LikedUserViewModel>() {

    private var _binding: FragmentLikedUserBinding? = null

    private val binding get() = _binding!!

    override fun provideLayoutId(): Int = R.layout.fragment_liked_user

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    @Inject
    lateinit var likedUserAdapter: LikedUserAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun setupView(view: View) {

        _binding = FragmentLikedUserBinding.bind(view)

        if(activity?.intent!!.hasExtra("data")){
            activity?.intent!!.getParcelableArrayListExtra<Post.User>("data").run {
                this?.let { viewModel.loadData(it) }
            }
        }

        val bundle = arguments
        if (bundle == null) {
            Timber.e("LikedUserFragment did not receive traveler information")
            return
        }

        val args  = LikedUserFragmentArgs.fromBundle(bundle)

        viewModel.loadData(args.data.toList())

        binding.rvLike.apply {
            layoutManager = linearLayoutManager
            adapter = likedUserAdapter

        }
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.likedUsers.observe(this, {
            likedUserAdapter.appendData(it.data!!)
            binding.tvLikesNumber.text = getString(R.string.user_post_likes_label,it.data.size)
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
