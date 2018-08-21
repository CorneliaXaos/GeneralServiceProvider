package testing.consumer.impl;

import testing.producer.spi.TestServiceInstalled;

public class TestServiceImpl4 implements TestServiceInstalled {

    @Override
    public boolean returnTrue() {
        return true;
    }
}
