@file:Suppress("MemberVisibilityCanPrivate")

package com.aballano.knex

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.ArrayList

/**
 * RecyclerView.Adapter extension created to work with KnexRenderer instances without the need of creating a new Adapter
 * every time.
 */
open class KnexAdapter<T : Any>(internal val knexBuilder: KnexBuilder,
                       private val collection: MutableList<T> = ArrayList(10)) :
      RecyclerView.Adapter<KnexViewHolder>() {

    init {
        setHasStableIds(true)
    }

    fun into(recyclerView: RecyclerView) = this.also {
        recyclerView.adapter = this
    }

    override fun getItemCount() = collection.size

    /**
     * @see MutableList.get
     */
    fun getItem(position: Int) = collection[position]

    override fun getItemId(position: Int) = collection[position].hashCode().toLong()

    override fun getItemViewType(position: Int) = knexBuilder.getItemViewType(getItem(position))

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
          knexBuilder.buildKnexViewHolder(viewGroup, LayoutInflater.from(viewGroup.context), viewType)

    override fun onBindViewHolder(viewHolder: KnexViewHolder, position: Int) {
        onBindViewHolder(viewHolder, position, emptyList())
    }

    override fun onBindViewHolder(viewHolder: KnexViewHolder, position: Int, payloads: List<Any>) {
        viewHolder.knexRenderer.render(getItem(position), position, payloads)
    }

    /**
     * @see MutableList.add
     */
    fun add(element: T) = collection.add(element)

    /**
     * @see MutableList.add
     * @see RecyclerView.Adapter.notifyItemInserted
     */
    fun addAndNotify(element: T) = add(element).also {
        notifyItemInserted(collection.size)
    }

    /**
     * Convenient add method that also supports negative index to specify
     * that the addition should be done at the end of the list.
     *
     * @see MutableList.add
     */
    fun add(index: Int, element: T) {
        if (index < 0) {
            add(element)
        } else {
            collection.add(index, element)
        }
    }

    /**
     * Convenient add method that also supports negative index to specify
     * that the addition should be done at the end of the list.
     *
     * @see MutableList.add
     * @see RecyclerView.Adapter.notifyItemInserted
     */
    fun addAndNotify(index: Int, element: T) {
        add(index, element)
        notifyItemInserted(if (index < 0) collection.size else index)
    }

    /**
     * @see MutableList.set
     */
    fun update(index: Int, element: T) = collection.set(index, element)

    /**
     * @see MutableList.set
     * @see RecyclerView.Adapter.notifyItemChanged
     */
    fun updateAndNotify(index: Int, element: T, payload: Any? = null) = update(index, element).also {
        notifyItemChanged(index, payload)
    }

    /**
     * @see KnexAdapter.removeAt
     * @see KnexAdapter.add
     */
    fun move(currentPosition: Int, newPosition: Int, element: T) {
        removeAt(currentPosition)
        add(newPosition, element)
    }

    /**
     * @see KnexAdapter.move
     * @see RecyclerView.Adapter.notifyItemMoved
     */
    fun moveAndNotify(currentPosition: Int, newPosition: Int, element: T) {
        move(currentPosition, newPosition, element)
        notifyItemMoved(currentPosition, newPosition)
    }

    /**
     * @see MutableList.remove
     */
    fun remove(element: Any) = collection.remove(element)

    /**
     * @see MutableList.remove
     * @see RecyclerView.Adapter.notifyItemRemoved
     */
    fun removeAndNotify(element: Any) = removeAtAndNotify(collection.indexOf(element))

    /**
     * @see MutableList.remove
     */
    fun removeAt(location: Int) = collection.removeAt(location)

    /**
     * @see MutableList.remove
     * @see RecyclerView.Adapter.notifyItemRemoved
     */
    fun removeAtAndNotify(indexOf: Int) = removeAt(indexOf).also {
        notifyItemRemoved(indexOf)
    }

    /**
     * @see MutableList.addAll
     */
    fun addAll(elements: Collection<T>) = collection.addAll(elements)

    /**
     * @see MutableList.addAll
     */
    fun addAll(index: Int, elements: Collection<T>) = collection.addAll(index, elements)

    /**
     * @see MutableList.addAll
     * @see RecyclerView.Adapter.notifyItemRangeInserted
     */
    fun addAllAndNotify(elements: Collection<T>): Boolean {
        val size = collection.size
        val result = addAll(elements)
        notifyItemRangeInserted(size, elements.size)
        return result
    }

    /**
     * @see MutableList.addAll
     * @see RecyclerView.Adapter.notifyItemRangeInserted
     */
    fun addAllAndNotify(index: Int, elements: Collection<T>) = addAll(index, elements).also {
        notifyItemRangeInserted(index, elements.size)
    }

    /**
     * @see MutableList.removeAll
     */
    fun removeAll(elements: Collection<Any>) = collection.removeAll(elements)

    /**
     * @see MutableList.removeAll
     * @see Adapter.notifyDataSetChanged
     */
    fun removeAllAndNotify(elements: Collection<Any>) = removeAll(elements).also {
        notifyDataSetChanged()
    }

    /**
     * @see MutableList.clear
     */
    fun clear() {
        collection.clear()
    }

    /**
     * @see MutableList.clear
     * @see Adapter.notifyDataSetChanged
     */
    fun clearAndNotify() {
        clear()
        notifyDataSetChanged()
    }

    /**
     * @see MutableList.indexOf
     */
    fun indexOf(element: Any) = collection.indexOf(element)

    /**
     * @see MutableList.contains
     */
    operator fun contains(element: Any) = collection.contains(element)

    /**
     * @see MutableList.containsAll
     */
    fun containsAll(element: Collection<Any>) = collection.containsAll(element)

    fun getCollection() = collection
}
