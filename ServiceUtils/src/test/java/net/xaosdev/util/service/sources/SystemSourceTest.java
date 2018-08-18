package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.Source;
import org.junit.Test;
import testing.producer.spi.TestService;

import static org.junit.Assert.*;

public class SystemSourceTest {

    @Test
    public void getSource() {
        // Arrange
        final Source source = SystemSource.getSource();
        final Service<TestService> service = new Service<>(TestService.class);
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
        final Source a = SystemSource.getSource();
        final Source b = SystemSource.getSource();

        // Act - not needed
        // Assert
        assertEquals(a, b);
    }
}