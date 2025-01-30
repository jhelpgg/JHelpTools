package fr.jhelp.tools.utilities.coroutine

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class FlowExtensionsTests
{
    @Test
    fun whenValueMatchDoInitializedWithNotMatchValue()
    {
        val collect = AtomicInteger(-1)
        val collected: () -> Int = {
            Thread.sleep(32)
            collect.get()
        }
        val flow = MutableStateFlow<Int>(0)
        flow.whenValueMatchDo({ value -> value == 1 }) { value -> collect.set(value) }
        Assert.assertEquals(-1, collected())
        flow.value = 0
        Assert.assertEquals(-1, collected())
        flow.value = 1
        Assert.assertEquals(1, collected())
        collect.set(-1)
        flow.value = 2
        Assert.assertEquals(-1, collected())
        flow.value = 1
        Assert.assertEquals("Already collected", -1, collected())
    }
    @Test
    fun whenValueMatchDoInitializedWithMatchValue()
    {
        val collect = AtomicInteger(-1)
        val collected: () -> Int = {
            Thread.sleep(32)
            collect.get()
        }
        val flow = MutableStateFlow<Int>(1)
        flow.whenValueMatchDo({ value -> value == 1 }) { value -> collect.set(value) }
        Assert.assertEquals(1, collected())
        collect.set(-1)
        flow.value = 2
        Assert.assertEquals(-1, collected())
        flow.value = 1
        Assert.assertEquals("Already collected", -1, collected())
    }
}