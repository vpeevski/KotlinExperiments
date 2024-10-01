package com.kotlinexperiments.coroutines.dump

import com.coroutines.dump.Counter
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.debug.DebugProbes
//import kotlinx.coroutines.debug.DebugProbes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CounterTest : FreeSpec(
    {
        beforeTest {
//            coroutineDebugProbes = true
//            DebugProbes.install()
        }
        afterTest {
//            DebugProbes.uninstall()
        }
        "inc counter" {
            runTest {
                val counter = Counter()
                counter.counterState.first() shouldBe 0
                val deferredInc = async {
                    runCatching {
                        counter.incCounter()
                    }
                }
                deferredInc.invokeOnCompletion {
                    DebugProbes.dumpCoroutines()
                }
            }
//            deferredInc.await()
//            counter.counterState.first() shouldBe 1
        }

    })
