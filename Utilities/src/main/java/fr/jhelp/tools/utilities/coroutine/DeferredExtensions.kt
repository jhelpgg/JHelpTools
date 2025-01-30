package fr.jhelp.tools.utilities.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Optional

fun <P : Any, R : Any> Deferred<P>.then(coroutineDispatcher: CoroutineDispatcher, action: (P) -> R): Deferred<R> =
    CoroutineScope(coroutineDispatcher).async { action(this@then.await()) }

fun <P : Any, R : Any> Deferred<P>.then(action: (P) -> R): Deferred<R> =
    CoroutineScope(Dispatchers.Default).async { action(this@then.await()) }

fun <T : Any> Deferred<Deferred<T>>.reduce(): Deferred<T> =
    CoroutineScope(Dispatchers.Immediate).async { this@reduce.await().await() }

fun <P : Any, R : Any> Deferred<P>.thenReduce(coroutineDispatcher: CoroutineDispatcher,
                                              action: (P) -> Deferred<R>): Deferred<R> =
    CoroutineScope(coroutineDispatcher).async { action(this@thenReduce.await()) }.reduce()

fun <P : Any, R : Any> Deferred<P>.thenReduce(action: (P) -> Deferred<R>): Deferred<R> =
    CoroutineScope(Dispatchers.Default).async { action(this@thenReduce.await()) }.reduce()

fun <T : Any> Deferred<T>.onSuccess(coroutineDispatcher: CoroutineDispatcher, consumer: (T) -> Unit)
{
    CoroutineScope(coroutineDispatcher).launch(coroutineDispatcher) {
        try
        {
            consumer(this@onSuccess.await())
        }
        catch (_: Exception)
        {
        }
    }
}

fun <T : Any> Deferred<T>.onSuccess(consumer: (T) -> Unit)
{
    this.onSuccess(Dispatchers.Default, consumer)
}

suspend fun <T : Any> Deferred<T>.onSuccessSuspend(coroutineDispatcher: CoroutineDispatcher,
                                                   consumer: suspend (T) -> Unit)
{
    withContext(coroutineDispatcher) {
        try
        {
            consumer(this@onSuccessSuspend.await())
        }
        catch (_: Exception)
        {
        }
    }
}

suspend fun <T : Any> Deferred<T>.onSuccessSuspend(consumer: suspend (T) -> Unit)
{
    this.onSuccessSuspend(Dispatchers.Default, consumer)
}

fun <T : Any> Deferred<T>.onFailure(coroutineDispatcher: CoroutineDispatcher, consumer: (Exception) -> Unit)
{
    CoroutineScope(coroutineDispatcher).launch(coroutineDispatcher) {
        try
        {
            this@onFailure.await()
        }
        catch (exception: Exception)
        {
            consumer(exception)
        }
    }
}

fun <T : Any> Deferred<T>.onFailure(consumer: (Exception) -> Unit)
{
    this.onFailure(Dispatchers.Default, consumer)
}

suspend fun <T : Any> Deferred<T>.onFailureSuspend(coroutineDispatcher: CoroutineDispatcher,
                                                   consumer: suspend (Exception) -> Unit)
{
    withContext(coroutineDispatcher) {
        try
        {
            this@onFailureSuspend.await()
        }
        catch (exception: Exception)
        {
            consumer(exception)
        }
    }
}

suspend fun <T : Any> Deferred<T>.onFailureSuspend(consumer: suspend (Exception) -> Unit)
{
    this.onFailureSuspend(Dispatchers.Default, consumer)
}

fun <P1 : Any, P2 : Any, R : Any> Deferred<P1>.combine(coroutineDispatcher: CoroutineDispatcher,
                                                       deferred: Deferred<P2>,
                                                       combination: (P1, P2) -> R): Deferred<R> =
    CoroutineScope(coroutineDispatcher).async {
        val result1 = this@combine.await()
        val result2 = deferred.await()
        combination(result1, result2)
    }

fun <P1 : Any, P2 : Any, R : Any> Deferred<P1>.combine(deferred: Deferred<P2>,
                                                       combination: (P1, P2) -> R): Deferred<R> =
    this.combine(Dispatchers.Default, deferred, combination)

val <T : Any> Deferred<T>.result: Result<T>
    get() =
        runBlocking {
            try
            {
                Result.success(this@result.await())
            }
            catch (exception: Exception)
            {
                Result.failure(exception)
            }
        }

val <T : Any> Deferred<T>.optional: Optional<T>
    get() =
        runBlocking {
            try
            {
                Optional.of(this@optional.await())
            }
            catch (exception: Exception)
            {
                Optional.empty()
            }
        }

fun joinAll(coroutineDispatcher: CoroutineDispatcher, vararg deferredList: Deferred<*>): Deferred<Unit> =
    CoroutineScope(coroutineDispatcher).async {
        for (deferred in deferredList)
        {
            deferred.join()
        }
    }

fun joinAll(vararg deferredList: Deferred<*>): Deferred<Unit> =
    joinAll(Dispatchers.Default, *deferredList)
