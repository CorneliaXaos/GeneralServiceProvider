package net.xaosdev.util.service.impl;

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