/*
 * Copyright 2024 AndroidDev Discord Dev Team
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
 */
package lifecycle

import lifecycle.test.ConcreteLifecycle
import org.junit.jupiter.api.assertThrows
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LifecycleTest {
    private lateinit var lifecycle: ConcreteLifecycle

    @BeforeTest
    fun setUp() {
        lifecycle = ConcreteLifecycle()
    }

    @AfterTest
    fun tearDown() {
        try {
            lifecycle.destroy()
        } catch (ignored: IllegalStateException) {}
    }

    @Test
    fun `onCreate called when created`() {
        lifecycle.create()
        assertEquals(1, lifecycle.onCreateCallCount)
    }

    @Test
    fun `onDestroy called when destroyed`() {
        lifecycle.create()
        lifecycle.destroy()
        assertEquals(1, lifecycle.onDestroyCallCount)
    }

    @Test
    fun `lifecycle cannot be created after being destroyed`() {
        lifecycle.create()
        lifecycle.destroy()
        assertThrows<IllegalStateException> {
            lifecycle.create()
        }
    }

    @Test
    fun `lifecycle cannot be created repeatedly`() {
        lifecycle.create()
        assertThrows<IllegalStateException> {
            lifecycle.create()
        }
    }

    @Test
    fun `lifecycle cannot be destroyed repeatedly`() {
        lifecycle.create()
        lifecycle.destroy()
        assertThrows<IllegalStateException> {
            lifecycle.destroy()
        }
    }
}
