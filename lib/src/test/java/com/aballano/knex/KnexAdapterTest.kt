package com.aballano.knex

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.*

@Config(sdk = [19], constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class KnexAdapterTest {

    lateinit var spyAdapter: KnexAdapter<Any>

    @Mock lateinit var mockedKnexBuilder: KnexBuilder
    @Mock lateinit var mockedCollection: MutableList<Any>
    @Mock lateinit var mockedParent: ViewGroup
    @Mock lateinit var mockedRenderer: ObjectKnexRenderer
    @Mock lateinit var mockedKnexViewHolder: KnexViewHolder
    @Mock lateinit var mockedRecyclerView: RecyclerView

    companion object {
        private const val ANY_SIZE = 11
        private const val ANY_POSITION = 2
        private val ANY_OBJECT = Any()
        private val ANY_OTHER_OBJECT = Any()
        private val ANY_OBJECT_COLLECTION = LinkedList<Any>()
        private const val ANY_ITEM_VIEW_TYPE = 3
    }

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(mockedParent.context).thenReturn(RuntimeEnvironment.application)

        spyAdapter = spy(KnexAdapter(mockedKnexBuilder, mockedCollection))
    }

    @Test fun `should return the collection`() {
        assertEquals(mockedCollection, spyAdapter.getCollection())
    }

    @Test fun `should return collection size on get count`() {
        whenever(mockedCollection.size).thenReturn(ANY_SIZE)

        assertEquals(ANY_SIZE.toLong(), spyAdapter.itemCount.toLong())
    }

    @Test fun `should return item at collection position on get item`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
    }

    @Test fun `should return position as item id`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
        assertEquals(ANY_OBJECT.hashCode().toLong(), spyAdapter.getItemId(ANY_POSITION))
    }

    @Test fun `should delegate into renderer builder to get item view type`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
        whenever(mockedKnexBuilder.getItemViewType(ANY_OBJECT)).thenReturn(ANY_ITEM_VIEW_TYPE)

        assertEquals(ANY_ITEM_VIEW_TYPE.toLong(), spyAdapter.getItemViewType(ANY_POSITION).toLong())
    }

    @Test fun `should build renderer using all needed dependencies`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
        whenever(mockedKnexBuilder.buildKnexViewHolder(any(), any(), any())).doReturn(mockedKnexViewHolder)

        spyAdapter.onCreateViewHolder(mockedParent, ANY_ITEM_VIEW_TYPE)

        verify(mockedKnexBuilder).buildKnexViewHolder(eq(mockedParent), any(), eq(ANY_ITEM_VIEW_TYPE))
    }

    @Test fun `should add element to collection`() {
        whenever(mockedCollection.size).thenReturn(ANY_SIZE)
        spyAdapter.addAndNotify(ANY_OBJECT)

        verify(mockedCollection).add(ANY_OBJECT)
        verify(spyAdapter).notifyItemInserted(ANY_SIZE)
    }

    @Test fun `should add element at position to collection`() {
        spyAdapter.addAndNotify(0, ANY_OBJECT)

        verify(mockedCollection).add(0, ANY_OBJECT)
        verify(spyAdapter).notifyItemInserted(0)
    }

    @Test fun `should add element at end position to collection`() {
        whenever(mockedCollection.size).thenReturn(ANY_SIZE)
        spyAdapter.addAndNotify(-1, ANY_OBJECT)

        verify(mockedCollection).add(ANY_OBJECT)
        verify(spyAdapter).notifyItemInserted(ANY_SIZE)
    }

    @Test fun `should add all elements to collection`() {
        whenever(mockedCollection.size).thenReturn(ANY_SIZE)
        spyAdapter.addAllAndNotify(ANY_OBJECT_COLLECTION)

        verify(mockedCollection).addAll(ANY_OBJECT_COLLECTION)
        verify(spyAdapter).notifyItemRangeInserted(ANY_SIZE, ANY_OBJECT_COLLECTION.size)
    }

    @Test fun `should add all elements at position to collection`() {
        spyAdapter.addAllAndNotify(0, ANY_OBJECT_COLLECTION)

        verify(mockedCollection).addAll(0, ANY_OBJECT_COLLECTION)
        verify(spyAdapter).notifyItemRangeInserted(0, ANY_OBJECT_COLLECTION.size)
    }

    @Test fun `should remove element from collection`() {
        spyAdapter.remove(ANY_OBJECT)
        verify(mockedCollection).remove(ANY_OBJECT)
    }

    @Test fun `should remove element from collection and notify`() {
        whenever(mockedCollection.indexOf(ANY_OBJECT)).thenReturn(ANY_SIZE)
        spyAdapter.removeAndNotify(ANY_OBJECT)

        verify(mockedCollection).removeAt(ANY_SIZE)
        verify(spyAdapter).notifyItemRemoved(ANY_SIZE)
    }

    @Test fun `should update element at position from collection`() {
        spyAdapter.updateAndNotify(0, ANY_OBJECT)

        verify(mockedCollection)[0] = ANY_OBJECT
        verify(spyAdapter).notifyItemChanged(0, null)
    }

    @Test fun `should update element at position from collection and pass payload`() {
        spyAdapter.updateAndNotify(0, ANY_OBJECT, ANY_OTHER_OBJECT)

        verify(mockedCollection)[0] = ANY_OBJECT
        verify(spyAdapter).notifyItemChanged(0, ANY_OTHER_OBJECT)
    }

    @Test fun `should move element from one position to another from collection`() {
        spyAdapter.moveAndNotify(0, 1, ANY_OBJECT)

        verify(mockedCollection).removeAt(0)
        verify(mockedCollection).add(1, ANY_OBJECT)
        verify(spyAdapter).notifyItemMoved(0, 1)
    }

    @Test fun `should remove all elements from collection`() {
        spyAdapter.removeAllAndNotify(ANY_OBJECT_COLLECTION)

        verify(mockedCollection).removeAll(ANY_OBJECT_COLLECTION)
        verify(spyAdapter).notifyDataSetChanged()
    }

    @Test fun `should clear elements from collection`() {
        spyAdapter.clearAndNotify()

        verify(mockedCollection).clear()
        verify(spyAdapter).notifyDataSetChanged()
    }

    @Test fun `should get index from collection`() {
        spyAdapter.indexOf(ANY_OBJECT)

        verify(mockedCollection).indexOf(ANY_OBJECT)
    }

    @Test fun `should contain all items in collection`() {
        spyAdapter.containsAll(ANY_OBJECT_COLLECTION)

        verify(mockedCollection).containsAll(ANY_OBJECT_COLLECTION)
    }

    @Test fun `should contain item in collection`() {
        spyAdapter.contains(ANY_OBJECT)

        verify(mockedCollection).contains(ANY_OBJECT)
    }

    @Test fun `should get renderer from view holder and pass content on bind`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
        whenever(mockedKnexViewHolder.knexRenderer).thenReturn(mockedRenderer)

        spyAdapter.onBindViewHolder(mockedKnexViewHolder, ANY_POSITION)

        verify(mockedRenderer).render(ANY_OBJECT, ANY_POSITION, emptyList<Any>())
    }

    @Test fun `should get renderer from view holder and render it on bind`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
        whenever(mockedKnexViewHolder.knexRenderer).thenReturn(mockedRenderer)

        spyAdapter.onBindViewHolder(mockedKnexViewHolder, ANY_POSITION)

        verify(mockedRenderer).render(ANY_OBJECT, ANY_POSITION, emptyList<Any>())
    }

    @Test(expected = NullPointerException::class)
    fun `should throw exception if null renderer`() {
        spyAdapter.onBindViewHolder(mockedKnexViewHolder, ANY_POSITION)
    }

    @Test fun `should hook into recycler view`() {
        val adapter = KnexAdapter<Any>(mockedKnexBuilder)
        adapter.into(mockedRecyclerView)

        verify(mockedRecyclerView).adapter = adapter
    }
}