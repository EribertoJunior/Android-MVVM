package com.example.androidmvvm

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidmvvm.model.repository.repository_impl.RepoDataRepository
import com.example.androidmvvm.view.RepoActivity
import net.vidageek.mirror.dsl.Mirror
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.After
import org.junit.Before
import java.io.IOException
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor




@RunWith(AndroidJUnit4::class)
class RepoActivityTest {

    @get:Rule
    val mActivityRule = IntentsTestRule(RepoActivity::class.java, false, true)

    private var server = MockWebServer()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        server.shutdown()
    }

    private fun setupServerUrl() {
        val url = server.url("/").toString()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val usersApi = RepoDataRepository()

        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create<RepoDataRepository>(RepoDataRepository::class.java)

        setField(usersApi, "api", api)
    }

    private fun setField(target: Any, fieldName: String, value: Any) {
        Mirror()
            .on(target)
            .set()
            .field(fieldName)
            .withValue(value)
    }

    @Test
    fun givenInitialState_when_shold() {
        onView(withId(R.id.recyclerViewRepositorios)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefresh_repositorios)).check(matches(isDisplayed()))
    }


}