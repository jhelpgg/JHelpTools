package fr.jhelp.tools.utilities.collections.cache

import java.util.Optional

/**
 * Cache of elements
 */
abstract class Cache<K : Any, V : Any>
{
    abstract operator fun get(key : K) : Optional<V>

    operator fun set(key : K, value : V)
    {
        if (key in this)
        {
            this.replace(key, value)
        }
        else
        {
            this.add(key, value)
        }
    }

    abstract operator fun contains(key : K) : Boolean

    protected abstract fun replace(key : K, value : V)

    protected abstract fun add(key : K, value : V)
}