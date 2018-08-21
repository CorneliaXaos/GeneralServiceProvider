/*
 * Copyright 2018 Cornelia Ada Schultz
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.xaosdev.util.service.sources;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ClassLoaderSourceTest {

    private ClassLoader current;
    private ClassLoaderSource defaultSource;
    private ClassLoaderSource constructedSource;

    @Before
    public void setUp() throws Exception {
        // Arrange - common
        current = Thread.currentThread().getContextClassLoader();
        defaultSource = new ClassLoaderSource();
        constructedSource = new ClassLoaderSource(current);
    }

    @Test
    public void getUUID() {
        // Arrange - empty
        // Act - empty
        // Assert
        assertNotEquals(defaultSource.getUUID(), constructedSource.getUUID());
    }

    @Test
    public void getClassLoader() {
        // Arrange - empty
        // Act - empty
        // Assert
        assertEquals(current, defaultSource.getClassLoader());
        assertEquals(current, constructedSource.getClassLoader());
    }

    @Test
    public void equals() {
        // Arrange - empty
        // Act - empty
        // Assert
        assertEquals(defaultSource, defaultSource);
        assertEquals(constructedSource, constructedSource);
        assertNotEquals(defaultSource, constructedSource);
    }
}