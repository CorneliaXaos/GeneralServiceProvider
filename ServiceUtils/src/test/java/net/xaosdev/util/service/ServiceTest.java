package net.xaosdev.util.service;

import testing.producer.spi.TestService;
import net.xaosdev.util.service.sources.ClassLoaderSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ServiceTest {

    private Service<TestService> testingService;
    private List<Source> defaultSources;

    @Before
    public void setUp() throws Exception {
        // Arrange - common
        testingService = new Service<>(TestService.class);
        defaultSources = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            ClassLoaderSource source = new ClassLoaderSource();
            defaultSources.add(source);
            testingService.addSource(source);
        }
    }

    @Test
    public void addSourceNotPresent() {
        // Arrange
        final ClassLoaderSource toAdd = new ClassLoaderSource();

        // Act
        testingService.addSource(toAdd);

        // Assert
        assert(testingService.getSources().contains(toAdd));
    }

    @Test (expected = IllegalArgumentException.class)
    public void addSourcePresent() {
        // Arrange - empty
        // Act
        testingService.addSource(defaultSources.get(0));

        // Assert - not needed
    }

    @Test
    public void getSourcesRead() {
        // Arrange - empty
        // Act
        final Collection<Source> sources = testingService.getSources();

        // Assert
        assert(defaultSources.containsAll(sources));
        assert(sources.containsAll(defaultSources));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getSourcesModifyFails() {
        // Arrange - empty
        // Act
        Collection<Source> sources = null;
        try {
            sources = testingService.getSources();
        } catch (Exception e) {
            fail("getSources should return something first");
        }

        sources.clear(); // should throw UnsupportedOperationException

        // Assert - not needed
    }

    @Test
    public void removeSourceUsingSource() {
        // Arrange
        final Source toRemove = defaultSources.get(0);
        final ClassLoaderSource newSource = new ClassLoaderSource();

        // Act
        boolean success = testingService.removeSource(toRemove);
        boolean failure = testingService.removeSource(newSource);

        // Assert
        assert(success);
        assertFalse(failure);
        assert(!testingService.getSources().contains(toRemove));
    }

    @Test
    public void removeSourceUsingUUID() {
        // Arrange
        final Source toRemove = defaultSources.get(0);

        // Act
        final Source removed = testingService.removeSource(toRemove.getUUID());

        // Assert
        assert(toRemove == removed);
        assert(!testingService.getSources().contains(toRemove));
    }

    @Test
    public void getServiceStream() {
        // Arrange
        final Service<TestService> service = new Service<>(TestService.class);
        final ClassLoaderSource defaultSource = new ClassLoaderSource();
        service.addSource(defaultSource);

        // Act / Assert
        assert(service.getServiceStream().count() == 1);
        service.getServiceStream().forEach((impl) -> {
            assert(impl.returnTrue());
        });
    }
}