package com.hashim.instagram.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hashim.instagram.R
import com.hashim.instagram.data.model.Post
import com.hashim.instagram.di.component.FragmentComponent
import com.hashim.instagram.ui.base.BaseFragment
import com.hashim.instagram.ui.home.post.PostsAdapter
import com.hashim.instagram.ui.home.post.likeduser.LikedUserAdapter
import com.hashim.instagram.ui.main.MainSharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
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

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var postsAdapter: PostsAdapter

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    override fun provideLayoutId(): Int = R.layout.fragment_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {

        postsAdapter.setupOnClickListener(this)

        rvPosts.apply {
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

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.posts.observe(this, Observer {
            it.data?.run { postsAdapter.appendData(it.data) }
        })

        mainSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run {
                viewModel.onNewPost(this)
            }
        })

        viewModel.refreshPosts.observe(this, Observer {
            it.data?.run {
                postsAdapter.updateData(this)
                rvPosts.scrollToPosition(0)
            }
        })
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



}
