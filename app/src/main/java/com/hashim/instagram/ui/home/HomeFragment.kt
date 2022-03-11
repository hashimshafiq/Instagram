package com.hashim.instagram.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.databinding.FragmentHomeBinding
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.home.post.PostsAdapter
import com.hashim.instagram.ui.main.MainSharedViewModel
import com.hashim.instagram.utils.common.Status
import javax.inject.Inject


class HomeFragment : BaseFragment<HomeViewModel>() , onClickListener {

    companion object {

        const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    lateinit var linearLayoutManager: LinearLayoutManager


    lateinit var postsAdapter: PostsAdapter

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun provideLayoutId(): Int = R.layout.fragment_home


    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {

        _binding = FragmentHomeBinding.bind(view)

        linearLayoutManager = LinearLayoutManager(requireContext())

        postsAdapter = PostsAdapter(requireActivity().lifecycle, ArrayList())


        postsAdapter.setupOnClickListener(this)

        binding.rvPosts.apply {
            layoutManager = linearLayoutManager
            adapter = postsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if( this is LinearLayoutManager
                            && itemCount>1
                            && itemCount == findLastVisibleItemPosition() + 1
                        )
                            viewModel.onLoadMore()
                    }
                }
            })
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }


        viewModel.posts.observe(this) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data?.isNotEmpty() == true) {
                                postsAdapter.appendData(it.data.toMutableList())
                                showLoading(false)
                            }
                        }
                        Status.ERROR -> {
                            showMessage(getString(R.string.server_connection_error))
                            showLoading(false)
                        }
                        else -> {

                        }
                    }
        }


        mainSharedViewModel.newPost.observe(this) {
            it.getIfNotHandled()?.run {
                viewModel.onNewPost(this)
            }
        }

        viewModel.refreshPosts.observe(this) {
            it.data?.run {
                postsAdapter.updateData(this)
                binding.rvPosts.scrollToPosition(0)
            }
        }


                viewModel.isLoggedIn.observe(this) {
                    it.getIfNotHandled()?.run {
                        if (!this){
                            findNavController().navigate(R.id.action_itemHome_to_loginFragment)
                        }

                    }
                }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onClickPhoto() {
        mainSharedViewModel.onProfileRedirect()
    }

    override fun onLongClickPost(post: Post) {

        showDialog(post)
    }

    private fun showDialog(post: Post){
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(requireContext())

        // Set a title for alert dialog
        builder.setTitle("Delete Post")

        // Set a message for alert dialog
        builder.setMessage("Do you want to delete this post?")


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> viewModel.deleteUserPost(post)
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)

        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
