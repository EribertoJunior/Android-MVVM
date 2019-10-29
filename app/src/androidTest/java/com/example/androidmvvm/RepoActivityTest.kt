package com.example.androidmvvm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.androidmvvm.view.RepoActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RepoActivityTest {

    private val mockWebServer: MockWebServer = MockWebServer()

    init {
        mockWebServer.start(8081)
    }

    @get:Rule
    var mActivityRule: IntentsTestRule<RepoActivity>? =
        IntentsTestRule(RepoActivity::class.java, false, true)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val json = loadContent("mock_repo_response_ok.json")
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(json))

    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun givenInitialState_when_shold() {

        onView(withId(R.id.recyclerViewRepositorios)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefresh_repositorios)).check(matches(isDisplayed()))
    }

    @Test
    fun whenResultIsOk_shouldDisplayListWithRepositores() {

    }

    private fun loadContent(asset: String): String {
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        return assets.open(asset).reader().readText()
    }

}