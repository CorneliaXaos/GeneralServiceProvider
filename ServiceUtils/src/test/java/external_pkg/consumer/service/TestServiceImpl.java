package external_pkg.consumer.service;

import external_pkg.producer.spi.TestService;

public class TestServiceImpl implements TestService {

    @Override
    public boolean returnTrue() {
        return true;
    }
}
