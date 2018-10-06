package com.aballano.knex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import assertk.assert
import assertk.assertions.isNotNull
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class KnexRendererTest {

    private val mockedContext: Context = mock()
    private val renderer: ObjectKnexRenderer = spy(ObjectKnexRenderer())
    private val mockedLayoutInflater: LayoutInflater = mock()
    private val mockedView: View = mock()
    private val mockedParent: ViewGroup = mock {
        on { context } doReturn mockedContext
    }

    @Test fun `should inflate view using layout inflater and parent after on create call`() {
        givenARendererInflatingView(mockedView)

        onCreateRenderer()

        verify(renderer).inflate(mockedLayoutInflater, mockedParent)
    }

    @Test fun `should set up view with the inflated view after on create call`() {
        givenARendererInflatingView(mockedView)

        onCreateRenderer()

        verify(renderer).setUpView()
    }

    @Test fun `should return non null context after creation`() {
        givenARendererInflatingView(mockedView)
        assertk.assert {
            renderer.context
        }.thrownError {
            this.actual is UninitializedPropertyAccessException
        }

        renderer.onCreate(mockedLayoutInflater, mockedParent)

        assert(renderer.context).isNotNull()
    }

    private fun onCreateRenderer() {
        renderer.onCreate(mockedLayoutInflater, mockedParent)
    }

    private fun givenARendererInflatingView(view: View?) {
        whenever(renderer.inflate(mockedLayoutInflater, mockedParent)).thenReturn(view)
    }
}
