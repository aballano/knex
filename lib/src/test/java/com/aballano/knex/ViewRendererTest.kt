package com.aballano.knex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Config(sdk = [19], constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class ViewKnexRendererTest {

    private val mockedInflater: LayoutInflater = mock()
    private val mockedParent: ViewGroup = mock {
        on { context } doReturn RuntimeEnvironment.application
    }

    @Test fun `should call inflate function with context`() {
        val latch = CountDownLatch(1)
        val renderer = ViewKnexRenderer<Any, View>(
              initFunction = { mock<View>().apply { latch.countDown() } },
              renderFunction = { _, _ -> }
        )

        renderer.onCreate(mockedInflater, mockedParent)

        assert(latch.await(1, TimeUnit.SECONDS))
    }

    @Test fun `should call render function with content and root view`() {
        val latch = CountDownLatch(1)
        val renderer = ViewKnexRenderer<Any, View>(
              initFunction = { mock() },
              renderFunction = { _, _ -> latch.countDown() }
        )

        renderer.onCreate(mockedInflater, mockedParent)
        renderer.render("", 1, emptyList<Any>())

        assert(latch.await(1, TimeUnit.SECONDS))
    }
}