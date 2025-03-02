/*
 * Copyright 2021-2024 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package com.example.orbit_mvi.viewmodel

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import org.orbitmvi.orbit.idling.IdlingResource

class AndroidIdlingResource : IdlingResource {

    private val counter = atomic(0)
    private val idle = atomic(true)

    private val job = atomic<Job?>(null)

    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun increment() {
        if (counter.getAndIncrement() == 0) {
            job.value?.cancel()
        }
        idle.value = false
    }

    override fun decrement() {
        if (counter.decrementAndGet() == 0) {
            job.getAndSet(
                coroutineScope.launch {
                    delay(MILLIS_BEFORE_IDLE)
                    idle.value = true
                }
            )?.cancel()
        }
    }

    override fun close() {
        job.value?.cancel()
        coroutineScope.cancel()
    }

    companion object {
        private const val MILLIS_BEFORE_IDLE = 100L
    }
}
