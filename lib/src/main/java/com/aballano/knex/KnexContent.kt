package com.aballano.knex

/**
 * Wrapper to use with [KnexBuilder] for type bindings.
 * @param item Content of the wrapper.
 * @param type The type within the list.
 */
open class KnexContent<out T : Any>(val item: T, val type: Int) {

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is KnexContent<*> -> false
        else -> type == other.type && item == other.item
    }

    override fun hashCode() = 31 * item.hashCode() + type

    override fun toString() = item.toString()
}

infix fun <T : Any> T.asContentType(type: Int) = KnexContent(this, type)
