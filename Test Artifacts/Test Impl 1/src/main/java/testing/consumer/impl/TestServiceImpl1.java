package testing.consumer.impl;

import testing.producer.spi.TestService;

public class TestServiceImpl1 implements TestService {

    @Override
    public boolean returnTrue() {
        return true;
    }
}
