package com.example.androidmvvm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
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

    @get:Rule
    val mActivityRule = IntentsTestRule(RepoActivity::class.java, false, true)

    private var mockWebServer = MockWebServer()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockWebServer = MockWebServer()
        //mockWebServer.start()
        //setupServerUrl()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun setupServerUrl() {
        val url = mockWebServer.url("/").toString()

    }


    @Test
    fun givenInitialState_when_shold() {
        onView(withId(R.id.recyclerViewRepositorios)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefresh_repositorios)).check(matches(isDisplayed()))
    }

    @Test
    fun whenResultIsOk_shouldDisplayListWithRepositores() {
        val json = loadContent("mock_repo_respose_ok.json")

        mockWebServer.url("/")
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(json))
        mockWebServer.start()

        val request = mockWebServer.takeRequest()

        assert(request.body.equals(loadContent(json)))

//        onView(withId(R.id.recyclerViewRepositorios)).check(matches(isDisplayed()))

    }

    /*@Test
    fun testLoadLibraryWithOnePlatformAndOneOwner() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("mockjson/library/detail_game_success.json".getJson())
        )

        onView(withId(R.id.tvGameName)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGameName)).check(matches(withText("Jogo XPTO")))
        onView(withId(R.id.btPerformLoan)).check(matches(isEnabled()))
    }*/

    private fun loadContent(asset: String): String {
        val assets = InstrumentationRegistry.getInstrumentation().context.assets

        return assets.open(asset).reader().readText()
    }

}