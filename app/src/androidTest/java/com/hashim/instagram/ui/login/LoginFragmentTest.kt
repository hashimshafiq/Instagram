package com.hashim.instagram.ui.login

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.hashim.instagram.R
import com.hashim.instagram.TestComponentRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class LoginFragmentTest {

    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    private val main = IntentsTestRule(LoginFragment::class.java, false, false)

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(component).around(main)

    @Before
    fun setup() {

    }

    @Test
    fun testCheckViewsDisplay() {
        main.launchActivity(Intent(component.getContext(), LoginFragment::class.java))
        onView(withId(R.id.layout_email))
            .check(matches(isDisplayed()))
        onView(withId(R.id.layout_password))
            .check(matches(isDisplayed()))
        onView(withId(R.id.bt_login))
            .check(matches(isDisplayed()))
    }

}