package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.Source;
import org.junit.Test;
import testing.producer.spi.TestServiceInstalled;

import static org.junit.Assert.*;

public class ExtensionsSourceTest {

    @Test
    public void getSource() {
        // Arrange
        final Source source = ExtensionsSource.getSource();
        final Service<TestServiceInstalled> service = new Service<>(TestServiceInstalled.class);
        service.addSource(source);

        // Act / Assert
        assert(service.getServiceStream().count() == 1);
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