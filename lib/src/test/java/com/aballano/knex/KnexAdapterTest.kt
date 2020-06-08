package com.aballano.knex

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

private const val ANY_SIZE = 11
private const val ANY_POSITION = 2
private val ANY_OBJECT = Any()
private val ANY_OTHER_OBJECT = Any()
private val ANY_OBJECT_COLLECTION = listOf(Any(), Any())
private const val ANY_ITEM_VIEW_TYPE = 3

@Config(sdk = [19], constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class KnexAdapterTest {

    private val mockedKnexBuilder: KnexBuilder = mock()
    private val mockedCollection: MutableList<Any> = mock()
    private val mockedRenderer: ObjectKnexRenderer = mock()
    private val mockedKnexViewHolder: KnexViewHolder = mock {
        on { knexRenderer } doReturn mockedRenderer
    }
    private val mockedRecyclerView: RecyclerView = mock()
    private val mockedParent: ViewGroup = mock {
        on { context } doReturn RuntimeEnvironment.application
    }

    private val spyAdapter: KnexAdapter<Any> =
        spy(KnexAdapter(mockedKnexBuilder, mockedCollection))

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
        spyAdapter.onBindViewHolder(mockedKnexViewHolder, ANY_POSITION)

        verify(mockedRenderer).render(ANY_OBJECT, ANY_POSITION, emptyList<Any>())
    }

    @Test fun `should get renderer from view holder and render it on bind`() {
        whenever(mockedCollection[ANY_POSITION]).thenReturn(ANY_OBJECT)
        spyAdapter.onBindViewHolder(mockedKnexViewHolder, ANY_POSITION)

        verify(mockedRenderer).render(ANY_OBJECT, ANY_POSITION, emptyList<Any>())
    }

    @Test fun `should hook into recycler view`() {
        val adapter = KnexAdapter<Any>(mockedKnexBuilder).into(mockedRecyclerView)

        verify(mockedRecyclerView).adapter = adapter
    }

    @Test fun `should forward attach event`() {
        val adapter = KnexAdapter<Any>(mockedKnexBuilder).into(mockedRecyclerView)

        adapter.onViewAttachedToWindow(mockedKnexViewHolder)

        verify(mockedKnexViewHolder.knexRenderer).onAttached()
    }

    @Test fun `should forward detach event`() {
        val adapter = KnexAdapter<Any>(mockedKnexBuilder).into(mockedRecyclerView)

        adapter.onViewDetachedFromWindow(mockedKnexViewHolder)

        verify(mockedKnexViewHolder.knexRenderer).onDetached()
    }
}
