package fr.jhelp.tools.utilities.coroutine

import kotlinx.coroutines.sync.Semaphore

class Locker
{
    private val semaphore = Semaphore(1)

    suspend fun lock()
    {
        this.semaphore.acquire()
    }

    fun unlock()
    {
        this.semaphore.release()
    }
}