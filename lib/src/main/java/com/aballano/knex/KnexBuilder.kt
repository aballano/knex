package com.aballano.knex

import aballano.kotlinmemoization.memoize
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aballano.knex.exception.KnexRendererNotFoundException
import kotlin.reflect.KClass

typealias KnexFactory<T> = () -> KnexRenderer<T>

fun <T : Any> KnexFactory<T>.type() = hashCode()

/**
 * Class created to work as builder for Renderer objects. This class provides methods to create a
 * Renderer instances using a fluent API.
 *
 * The library users have to extend RendererBuilder and create a new one with prototypes. The
 * RendererBuilder implementation will have to declare the mapping between objects from the
 * List and Renderer instances passed to the prototypes collection.
 *
 * This class is not going to implement the view recycling if is used with the RecyclerView widget
 * because RecyclerView class already implements the view recycling for us.
 */
open class KnexBuilder private constructor(
    protected val classBindings: MutableMap<KClass<Any>, KnexFactory<*>>,
    protected val typeBindings: SparseArray<KnexFactory<KnexContent<Any>>>
) {

    /**
     * Main method of this class related to RecyclerView widget. This method is the responsible of
     * create a new Knex instance with all the needed information to implement the rendering.
     * This method will validate all the attributes passed in the builder constructor and will create
     * a RendererViewHolder instance.
     *
     * This method is used with RecyclerView because the view recycling mechanism is implemented out
     * of this class and we only have to return new RendererViewHolder instances.

     * @return ready to use RendererViewHolder instance.
     */
    open fun buildKnexViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater, viewType: Int): KnexViewHolder =
        getKnexRendererByViewType(viewType).let {
            it.onCreate(layoutInflater, parent)

            @Suppress("UNCHECKED_CAST") KnexViewHolder(it as KnexRenderer<Any>)
        }

    private fun getKnexRendererByViewType(viewType: Int): KnexRenderer<*> = searchCachedKnexFactoryByViewType(viewType)()

    private val searchCachedKnexFactoryByViewType: (viewType: Int) -> KnexFactory<*> by lazy {
        fun searchKnexFactoryByViewType(viewType: Int): KnexFactory<*> =
            (typeBindings.values + classBindings.values).find { it.type() == viewType }
                ?: throw KnexRendererNotFoundException("No prototype was found for the viewType $viewType")

        ::searchKnexFactoryByViewType.memoize()
    }

    /**
     * Return the item view type used by the adapter to implement recycle mechanism.
     *
     * @param content to be rendered.
     *
     * @return an integer that represents the renderer inside the adapter.
     */
    internal open fun getItemViewType(content: Any): Int = getKnexFactory(content).type()

    /**
     * Method to be implemented by the RendererBuilder subtypes. In this method the library user will
     * define the mapping between content and renderer class.
     *
     * @param content used to map object to Renderers.
     * @return the class associated to the renderer.
     */
    internal fun getKnexFactory(content: Any): KnexFactory<*> = when (content) {
        is KnexContent<*> -> typeBindings[content.type]
        else -> {
            classBindings.forEach { (key, value) ->
                // If kotlin types are the same or the equivalent Java versions are
                if (content::class == key || key.java.isAssignableFrom(content::class.java)) return value
            }
            null
        }
    } ?: throw KnexRendererNotFoundException("No prototype was found for the class ${content::class}")

    interface ExtendedRendererBuilder<T : Any> {
        fun <Subtype : Type, Type : Any> bind(clx: KClass<Subtype>, factory: KnexFactory<Type>):
            BindedExtendedRendererBuilder<T>

        fun <Type : KnexContent<Any>> bind(type: Int, factory: KnexFactory<Type>): BindedExtendedRendererBuilder<T>
    }

    interface BindedExtendedRendererBuilder<T : Any> : ExtendedRendererBuilder<T> {
        fun build(): KnexAdapter<T>
        fun buildWith(collection: MutableList<T>): KnexAdapter<T>
    }

    class Builder<T : Any> : BindedExtendedRendererBuilder<T> {
        override fun build(): KnexAdapter<T> = buildWith(ArrayList(10))

        override fun buildWith(collection: MutableList<T>): KnexAdapter<T> =
            KnexAdapter(KnexBuilder(classBinding, typeBindings), collection)

        private val classBinding: MutableMap<KClass<Any>, KnexFactory<*>> = HashMap()
        private val typeBindings: SparseArray<KnexFactory<KnexContent<Any>>> = SparseArray()

        /**
         * Given a class configures the classBinding between a class and a [KnexRenderer].
         *
         * @param clx Class to bind.
         * @param factory used to generate a Renderer.
         */
        override fun <Subtype : Type, Type : Any> bind(clx: KClass<Subtype>, factory: KnexFactory<Type>):
            BindedExtendedRendererBuilder<T> = this.also {
            if (clx == Any::class || clx == Object::class) {
                throw IllegalArgumentException(
                    "Making a bind to the Any/Object class means that every item will be mapped " +
                        "to the specified Renderer and thus all other bindings are invalidated. Please use the standard " +
                        "constructor for that"
                )
            }
            @Suppress("UNCHECKED_CAST") classBinding.put(clx as KClass<Any>, factory)
        }

        /**
         * Binds a custom type to a given [KnexRenderer].
         *
         * @param type Integer type.
         * @param factory used to generate a Renderer.
         */
        override fun <Type : KnexContent<Any>> bind(type: Int, factory: KnexFactory<Type>):
            BindedExtendedRendererBuilder<T> = this.also {
            @Suppress("UNCHECKED_CAST") typeBindings.put(type, factory as KnexFactory<KnexContent<Any>>)
        }
    }

    companion object {
        /**
         * Initializes a [KnexBuilder] without [KnexRenderer]s. When using this constructor some
         * classBinding configuration is needed.
         */
        fun create(): ExtendedRendererBuilder<Any> = Builder()

        inline fun <reified T : Any> create(noinline factory: KnexFactory<T>): BindedExtendedRendererBuilder<T> =
            Builder<T>().apply { bind(T::class, factory) }
    }

    @Suppress("UNCHECKED_CAST") private val <V> SparseArray<V>.values: MutableCollection<V>
        get() {
            fun <V> generateValues(sparseArray: SparseArray<V>) =
                (0 until sparseArray.size())
                    .map { sparseArray.valueAt(it) }
                    .toMutableList()
                    .also { SparseArrayValuesCache[sparseArray] = it }
            return SparseArrayValuesCache[this] as MutableCollection<V>? ?: generateValues(this)
        }

    private object SparseArrayValuesCache : HashMap<SparseArray<*>, MutableCollection<*>>()
}
