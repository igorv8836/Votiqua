package com.example.orbit_mvi.viewmodel

import kotlinx.coroutines.flow.*

internal fun <T> StateFlow<T>.onEach(block: (T) -> Unit): StateFlow<T> =
    object : StateFlow<T> {
        override val replayCache: List<T>
            get() = this@onEach.replayCache

        override val value: T
            get() = this@onEach.value

        override suspend fun collect(collector: FlowCollector<T>): Nothing {
            this@onEach.collect {
                block(it)
                collector.emit(it)
            }
        }
    }
