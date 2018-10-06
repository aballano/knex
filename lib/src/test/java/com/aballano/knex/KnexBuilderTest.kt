package com.aballano.knex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.aballano.knex.exception.KnexRendererNotFoundException
import com.nhaarman.mockito_kotlin.doReturn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.Date

@Config(sdk = [19], constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class KnexBuilderTest {
    private val mockedContext: Context = mock()
    private val mockedLayoutInflater: LayoutInflater = mock()
    private val mockedRendererView: View = mock()
    private val mockedParent: ViewGroup = mock {
        on { context } doReturn mockedContext
    }

    @Test fun `should build viewholder with provided renderer`() {
        val renderer = ObjectKnexRenderer(mockedRendererView)
        val factory = { renderer }

        val knexBuilder = KnexBuilder.create()
              .bind(Number::class, factory)
              .bind(String::class) { StringKnexRenderer() }
              .bind(1) { BooleanTypeKnexRenderer() }
              .build()
              .knexBuilder

        val viewHolder = knexBuilder.buildKnexViewHolder(mockedParent, mockedLayoutInflater, factory.type())

        assertEquals(renderer, viewHolder.knexRenderer)
    }

    @Test(expected = KnexRendererNotFoundException::class)
    fun `should not find renderer`() {
        val factory = { ObjectKnexRenderer(mockedRendererView) }

        val knexBuilder = KnexBuilder.create()
              .bind(Number::class, factory)
              .bind(String::class) { StringKnexRenderer() }
              .bind(1) { BooleanTypeKnexRenderer() }
              .build()
              .knexBuilder

        knexBuilder.buildKnexViewHolder(mockedParent, mockedLayoutInflater, factory.type() + 1)
    }

    @Test fun `should get right class binding factory`() {
        val knexBuilder = KnexBuilder.create()
              .bind(Number::class) { ObjectKnexRenderer() }
              .bind(String::class) { StringKnexRenderer() }
              .bind(1) { BooleanTypeKnexRenderer() }
              .build()
              .knexBuilder

        assertEquals(StringKnexRenderer::class, knexBuilder.getKnexFactory("").kclass())
    }

    @Test fun `should get right type binding factory`() {
        val knexBuilder = KnexBuilder.create()
              .bind(Number::class) { ObjectKnexRenderer() }
              .bind(String::class) { StringKnexRenderer() }
              .bind(1) { BooleanTypeKnexRenderer() }
              .build()
              .knexBuilder

        assertEquals(BooleanTypeKnexRenderer::class, knexBuilder.getKnexFactory(KnexContent("a", 1)).kclass())
    }

    @Test(expected = KnexRendererNotFoundException::class)
    fun `should not find factory`() {
        val knexBuilder = KnexBuilder.create()
              .bind(Number::class) { ObjectKnexRenderer() }
              .bind(String::class) { StringKnexRenderer() }
              .bind(1) { BooleanTypeKnexRenderer() }
              .build()
              .knexBuilder

        knexBuilder.getKnexFactory(Date()).kclass()
    }

    @Test fun `should return type of factory`() {
        val renderer = ObjectKnexRenderer(mockedRendererView)

        val knexBuilder = KnexBuilder.create()
              .bind(String::class) { renderer }
              .build()
              .knexBuilder

        val factory = knexBuilder.getKnexFactory("")
        val viewType = knexBuilder.getItemViewType("")

        assertEquals(factory.type(), viewType)
    }

    @Test fun `should add prototype and configure renderer binding for type`() {
        val type = 1
        val knexBuilder = KnexBuilder.create()
              .bind(type) { ContentObjectKnexRenderer() }
              .build()
              .knexBuilder

        assertEquals(ContentObjectKnexRenderer::class,
              knexBuilder.getKnexFactory(KnexContent(Any(), type)).kclass())
    }

    @Test(expected = KnexRendererNotFoundException::class)
    fun `should fail for wrong type`() {
        val type = 1
        val anotherType = 2
        val knexBuilder = KnexBuilder.create()
              .bind(type) { ObjectKnexRenderer() }
              .bind(anotherType) { ObjectKnexRenderer() }
              .build()
              .knexBuilder

        knexBuilder.getKnexFactory(KnexContent(Any(), -1))
    }

    @Test fun `should add prototype and configure renderer binding for type with multiple prototypes`() {
        val knexBuilder = KnexBuilder.create()
              .bind(Int::class) { IntKnexRenderer() }
              .bind(Boolean::class) { BooleanKnexRenderer() }
              .bind(String::class) { StringKnexRenderer() }
              .build()
              .knexBuilder

        assertEquals(IntKnexRenderer::class, knexBuilder.getKnexFactory(1).kclass())
    }

    @Test fun `should check bindings that inherit parent class`() {
        val knexBuilder = KnexBuilder.create()
              .bind(ParentClass::class) { ObjectKnexRenderer() }
              .build()
              .knexBuilder

        assertEquals(ObjectKnexRenderer::class, knexBuilder.getKnexFactory(ChildClass()).kclass())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw illegal argument exception for object binding`() {
        KnexBuilder.create()
              .bind(Any::class) { ObjectKnexRenderer() }
              .build()
    }

    @Test fun `should create empty adapter`() {
        val adapter = KnexBuilder.create()
              .bind(String::class) { ObjectKnexRenderer() }
              .build()

        assertTrue(adapter.getCollection().isEmpty())
    }

    @Test fun `should create adapter with items`() {
        val list = mutableListOf("1", "2", "3")
        val adapter = KnexBuilder.create(::StringKnexRenderer)
              .buildWith(list)

        assertEquals(list, adapter.getCollection())
    }

    open class ParentClass internal constructor()

    class ChildClass internal constructor() : ParentClass()

    class BooleanTypeKnexRenderer : KnexRenderer<KnexContent<Boolean>>() {
        override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = mock<View>()
        override fun render(content: KnexContent<Boolean>, position: Int, payloads: List<*>) {}
    }

    class BooleanKnexRenderer : KnexRenderer<Boolean>() {
        override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = mock<View>()
        override fun render(content: Boolean, position: Int, payloads: List<*>) {}
    }

    class IntKnexRenderer : KnexRenderer<Int>() {
        override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = mock<View>()
        override fun render(content: Int, position: Int, payloads: List<*>) {}
    }

    class StringKnexRenderer : KnexRenderer<String>() {
        override fun inflate(inflater: LayoutInflater, parent: ViewGroup) = mock<View>()
        override fun render(content: String, position: Int, payloads: List<*>) {}
    }
}

private fun KnexFactory<*>.kclass() = this()::class