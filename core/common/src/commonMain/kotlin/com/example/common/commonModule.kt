package com.example.common

import kotlinx.coroutines.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun commonModule() = module {
    single(named(MyDispatchers.IO)) { provideIoDispatcher() }

    single(named(MyDispatchers.Default)) { provideDefaultDispatcher() }
    single { provideDefaultDispatcher() }

    single {
        provideApplicationScope(
            get(named(MyDispatchers.Default))
        )
    }

}

internal fun provideIoDispatcher() = Dispatchers.IO
internal fun provideDefaultDispatcher() = Dispatchers.Default
internal fun provideApplicationScope(dispatcher: CoroutineDispatcher): CoroutineScope =
    CoroutineScope(SupervisorJob() + dispatcher)


enum class MyDispatchers {
    IO, Default
}