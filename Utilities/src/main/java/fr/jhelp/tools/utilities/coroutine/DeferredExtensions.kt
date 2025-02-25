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

/**
 * Do an action after the result of deferred is available
 * @param coroutineDispatcher Context where the action will be done
 * @param action Action to do
 * @return Deferred of combination this deferred follow by the action
 */
fun <P : Any, R : Any> Deferred<P>.then(coroutineDispatcher: CoroutineDispatcher, action: (P) -> R): Deferred<R> =
    CoroutineScope(coroutineDispatcher).async { action(this@then.await()) }

/**
 * Do an action after the result of deferred is available
 * @param action Action to do
 * @return Deferred of combination this deferred follow by the action
 */
fun <P : Any, R : Any> Deferred<P>.then(action: (P) -> R): Deferred<R> =
    CoroutineScope(Dispatchers.Default).async { action(this@then.await()) }

/**
 * Reduce a deferred of deferred to a single deferred
 */
fun <T : Any> Deferred<Deferred<T>>.reduce(): Deferred<T> =
    CoroutineScope(Dispatchers.Immediate).async { this@reduce.await().await() }

/**
 * Do an action after the result of deferred is available
 * @param coroutineDispatcher Context where the action will be done
 * @param action Action to do
 * @return Deferred of combination this deferred follow by the action
 */
fun <P : Any, R : Any> Deferred<P>.thenReduce(coroutineDispatcher: CoroutineDispatcher,
                                              action: (P) -> Deferred<R>): Deferred<R> =
    CoroutineScope(coroutineDispatcher).async { action(this@thenReduce.await()) }.reduce()

/**
 * Do an action after the result of deferred is available
 * @param action Action to do
 * @return Deferred of combination this deferred follow by the action
 */
fun <P : Any, R : Any> Deferred<P>.thenReduce(action: (P) -> Deferred<R>): Deferred<R> =
    CoroutineScope(Dispatchers.Default).async { action(this@thenReduce.await()) }.reduce()

/**
 * Do an action after the result of deferred is available and succeed
 * @param coroutineDispatcher Context where the action will be done
 * @param consumer Action to do
 */
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

/**
 * Do an action after the result of deferred is available and succeed
 * @param consumer Action to do
 */
fun <T : Any> Deferred<T>.onSuccess(consumer: (T) -> Unit)
{
    this.onSuccess(Dispatchers.Default, consumer)
}

/**
 * Do an action after the result of deferred is available and succeed
 * @param coroutineDispatcher Context where the action will be done
 * @param consumer Action to do
 */
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

/**
 * Do an action after the result of deferred is available and succeed
 * @param consumer Action to do
 */
suspend fun <T : Any> Deferred<T>.onSuccessSuspend(consumer: suspend (T) -> Unit)
{
    this.onSuccessSuspend(Dispatchers.Default, consumer)
}

/**
 * Do an action after the result of deferred is available and failed
 * @param coroutineDispatcher Context where the action will be done
 * @param consumer Action to do
 */
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

/**
 * Do an action after the result of deferred is available and failed
 * @param consumer Action to do
 */
fun <T : Any> Deferred<T>.onFailure(consumer: (Exception) -> Unit)
{
    this.onFailure(Dispatchers.Default, consumer)
}

/**
 * Do an action after the result of deferred is available and failed
 * @param coroutineDispatcher Context where the action will be done
 * @param consumer Action to do
 */
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

/**
 * Do an action after the result of deferred is available and failed
 * @param consumer Action to do
 */
suspend fun <T : Any> Deferred<T>.onFailureSuspend(consumer: suspend (Exception) -> Unit)
{
    this.onFailureSuspend(Dispatchers.Default, consumer)
}

/**
 * Combine two deferred.
 *
 * It waits the two deferred to finish and combine the result in unique deferred
 * @param coroutineDispatcher Context where the action will be done
 * @param deferred Deferred to combine with
 * @param combination Combination of the two deferred
 * @return Deferred of combination this deferred and given one follow by the action
 */
fun <P1 : Any, P2 : Any, R : Any> Deferred<P1>.combine(coroutineDispatcher: CoroutineDispatcher,
                                                       deferred: Deferred<P2>,
                                                       combination: (P1, P2) -> R): Deferred<R> =
    CoroutineScope(coroutineDispatcher).async {
        val result1 = this@combine.await()
        val result2 = deferred.await()
        combination(result1, result2)
    }

/**
 * Combine two deferred.
 *
 * It waits the two deferred to finish and combine the result in unique deferred
 * @param deferred Deferred to combine with
 * @param combination Combination of the two deferred
 * @return Deferred of combination this deferred and given one follow by the action
 */
fun <P1 : Any, P2 : Any, R : Any> Deferred<P1>.combine(deferred: Deferred<P2>,
                                                       combination: (P1, P2) -> R): Deferred<R> =
    this.combine(Dispatchers.Default, deferred, combination)

/**
 * Convert a deferred to a result
 *
 * This method block the current thread until the deferred is finished.
 */
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

/**
 * Convert a deferred to an optional
 *
 * This method block the current thread until the deferred is finished.
 */
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

/**
 * Joins several deferred tasks, awaiting their completion.
 *
 * @param coroutineDispatcher The context in which the waiting will be performed.
 * @param deferredList The list of deferred tasks to join.
 * @return A deferred that completes when all the provided deferred tasks have finished.
 */
fun joinAll(coroutineDispatcher: CoroutineDispatcher, vararg deferredList: Deferred<*>): Deferred<Unit> =
    CoroutineScope(coroutineDispatcher).async {
        for (deferred in deferredList)
        {
            deferred.join()
        }
    }

/**
 * Joins several deferred tasks, awaiting their completion.
 *
 * @param deferredList The list of deferred tasks to join.
 * @return A deferred that completes when all the provided deferred tasks have finished.
 */
fun joinAll(vararg deferredList: Deferred<*>): Deferred<Unit> =
    joinAll(Dispatchers.Default, *deferredList)
