package testing.producer.spi;

public interface TestPermissions {

    void testCreatingService();

    void testAddingSources(final Object object);

    void testGettingSources(final Object object);

    void testRemovingSources(final Object object);

    void testGettingServiceStream(final Object object);
}
