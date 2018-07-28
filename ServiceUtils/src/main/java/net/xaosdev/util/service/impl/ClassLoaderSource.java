package net.xaosdev.util.service.impl;

import net.xaosdev.util.service.Source;

import java.util.UUID;

/**
 * A simple Source that uses a user-provided ClassLoader.
 */
public class ClassLoaderSource implements Source {

    //region Fields (Private)

    /**
     * The UUID identifying this source.
     */
    private final UUID uuid;

    /**
     * The ClassLoader this source pulls from.
     */
    private final ClassLoader classLoader;

    //endregion

    //region Constructors (Public)

    /**
     * Creates a new ClassLoader source by using the current thread's ClassLoader.
     */
    public ClassLoaderSource() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates a new ClassLoader source from a user-provided ClassLoader.
     * @param classLoader the user-provided ClassLoader.
     */
    public ClassLoaderSource(final ClassLoader classLoader) {
        uuid = UUID.randomUUID();
        this.classLoader = classLoader;
    }

    //endregion

    //region Interface (Source)

    /**
     * @inheritDoc
     */
    public final UUID getUUID() {
        return uuid;
    }

    /**
     * @inheritDoc
     */
    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    //endregion

    //region Interface (Object)

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ClassLoaderSource)) {
            return false;
        }

        final ClassLoaderSource other = (ClassLoaderSource) o;

        return other.uuid.equals(uuid);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    //endregion
}
