package com.hashim.instagram.ui.home

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.hashim.instagram.R
import com.hashim.instagram.TestComponentRule
import com.hashim.instagram.data.model.User
import com.hashim.instagram.utils.RVMatcher.atPositionOnView
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class HomeFragmentTest {

    private val component =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val chain : RuleChain = RuleChain.outerRule(component)

    @Before
    fun setUp() {

        val userRepository = component.testComponent!!.getUserRepository()
        val currentUser = User("id","name","email","accessToken","image")
        userRepository.saveCurrentUser(currentUser)
    }

    @After
    fun tearDown() {
        val userRepository = component.testComponent!!.getUserRepository()
        userRepository.removeCurrentUser()
    }


    @Test
    fun homePostListAvailable_shouldDisplay(){
        launchFragmentInContainer<HomeFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.rvPosts)).check(matches(isDisplayed()))

        onView(withId(R.id.rvPosts))
            .check(matches(atPositionOnView(0, withText("name1"),R.id.tvName)))

        onView(withId(R.id.rvPosts))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPositionOnView(1, withText("name2"),R.id.tvName)))
    }
}