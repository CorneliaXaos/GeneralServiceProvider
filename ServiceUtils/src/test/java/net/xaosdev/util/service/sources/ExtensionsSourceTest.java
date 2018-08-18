package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.Source;
import org.junit.Ignore;
import org.junit.Test;
import testing.producer.spi.TestService;

import static org.junit.Assert.*;

public class ExtensionsSourceTest {

    @Ignore("Unsure how to test via the extensions ClassLoader.")
    @Test
    public void getSource() {
        // Arrange
        final Source source = ExtensionsSource.getSource();
        final Service<TestService> service = new Service<>(TestService.class);
        service.addSource(source);

        // Act / Assert
        assert(service.getServiceStream().count() == 0); // Unsure how to test with providers installed
        service.getServiceStream().forEach(impl -> {
            assert(impl.returnTrue());
        });
    }

    @Test
    public void getSourceDuplicatesAreSame() {
        // Arrange
        final Source a = ExtensionsSource.getSource();
        final Source b = ExtensionsSource.getSource();

        // Act - not needed
        // Assert
        assertEquals(a, b);
    }
}