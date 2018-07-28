package net.xaosdev.util.service;

import java.util.UUID;

/**
 * A representation of where services may be loaded from.
 *
 * A source is used to identify / create a ClassLoader for loading potential plugin classes.  This object should be
 * immutable after its construction and the source should NOT change with subsequent calls to `getClassLoader`.
 */
public interface Source {

    /**
     * Gets the UUID used for uniquely identifying this source.
     * @return the UUID used for uniquely identifying this source.
     */
    UUID getUUID();

    /**
     * Gets the ClassLoader this source uses to acquire service classes.
     * @return the ClassLoader this source uses to acquire service classes.
     */
    ClassLoader getClassLoader();
}
