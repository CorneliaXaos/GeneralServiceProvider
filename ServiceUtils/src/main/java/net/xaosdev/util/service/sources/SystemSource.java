package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Source;

/**
 * A convenience class for quickly creating a source that points to the application's main classpath.
 *
 * This source will ONLY load service providers that are on the running JVM's application classpath.
 * Additionally, this source will implement a Singleton pattern so that duplicate calls to this class
 * result in the same source every time.
 */
public final class SystemSource extends ClassLoaderSource {

    //region Fields (Private)

    /**
     * The instantiated source, or null if not created yet.
     */
    private static SystemSource source = null;

    //endregion

    //region Constructors (Private)

    /**
     * Creates a system source.
     */
    private SystemSource() {
        super(ClassLoader.getSystemClassLoader());
    }

    //endregion

    //region Interface (Public)

    /**
     * Lazily instantiates the SystemSource singleton instance and returns it.
     *
     * @return the SystemSource instance.
     */
    public static Source getSource() {
        if (source == null) {
            source = new SystemSource();
        }
        return source;
    }

    //endregion
}
