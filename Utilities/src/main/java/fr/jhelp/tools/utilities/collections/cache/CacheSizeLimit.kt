package fr.jhelp.tools.utilities.collections.cache

import java.util.Optional
import kotlin.math.max

class CacheSizeLimit<K : Any, V : Any>(limit: Int = 32) : Cache<K, V>()
{
    private val limit = max(16, limit)
    private val cache = HashMap<K, CacheSizeLimitElement<V>>()

    override fun get(key: K): Optional<V> =
        Optional.ofNullable(this.cache[key]).map { element ->
            element.lastUsageTime = System.currentTimeMillis()
            element.value
        }

    override fun contains(key: K): Boolean =
        this.cache.containsKey(key)

    override fun replace(key: K, value: V)
    {
        val element = this.cache[key] ?: return
        element.value = value
        element.lastUsageTime = System.currentTimeMillis()
    }

    override fun add(key: K, value: V)
    {
        if (this.cache.size >= this.limit)
        {
            val oldestKey = this.cache.minByOrNull { (_, element) -> element.lastUsageTime }!!.key
            this.cache.remove(oldestKey)
        }

        this.cache[key] = CacheSizeLimitElement(value, System.currentTimeMillis())
    }
}