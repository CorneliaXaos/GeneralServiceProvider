package testing.consumer.impl;

import net.xaosdev.util.service.Service;
import net.xaosdev.util.service.sources.SystemSource;
import testing.producer.spi.TestPermissions;
import testing.producer.spi.TestService;

public class TestPermissionsImpl implements TestPermissions {

    @Override
    public void testCreatingService() {
        final Service<TestService> service = new Service<>(TestService.class);
    }

    @Override
    public void testAddingSources(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.addSource(SystemSource.getSource());
    }

    @Override
    public void testGettingSources(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.getSources();
    }

    @Override
    public void testRemovingSources(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.removeSource(SystemSource.getSource());
    }

    @Override
    public void testGettingServiceStream(final Object object) {
        final Service<TestService> service = (Service<TestService>) object;
        service.getServiceStream();
    }
}
