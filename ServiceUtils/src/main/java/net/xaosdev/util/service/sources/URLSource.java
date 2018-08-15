package net.xaosdev.util.service.sources;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * This source is a convenience class for creating a source from a URLClassLoader.
 *
 * This class does NOT perform any safekeeping of its inputs.  It exists solely to provide an easy-to-use
 * interface for creating a source from a URL without having to write the common scaffold code.  As such,
 * this class will likely throw exceptions if the inputs lead to invalid endpoints that are not valid
 * sources for loading classes from.  Please see the URLClassLoader API to determine what measure should
 * be taken... or triple check and verify your inputs!!!
 *
 * Additionally, this source will, by default, use an internal version of the URLClassLoader that will exclude
 * services that would be found via the primordial ClassLoader.  This behaviour can be overridden by passing
 * in a custom URLClassLoader of the user's choice.
 */
public final class URLSource extends ClassLoaderSource {

    //region Constructors (Public)

    /**
     * Creates a new URLSource from a given URL.
     *
     * The created URLClassLoader uses the default ClassLoader.
     * @param url the URL to load classes from.
     */
    public URLSource(final URL url) {
        super(new URLClassLoader(new URL[] { url }));
    }

    /**
     * Creates a new URLSource from a given URL and parent ClassLoader.
     *
     * @param url the URL to load classes from.
     * @param classLoader the ClassLoader to treat as the parent loader.
     */
    public URLSource(final URL url, final ClassLoader classLoader) {
        super(new URLClassLoader(new URL[] { url }, classLoader));
    }

    /**
     * Creates a new URLSource with the given URLClassLoader.
     *
     * In most, if not all cases, the user should NOT use this constructor as it can result in the mingling
     * of Services loaded from different sources.
     * @param classLoader the URLClassLoader to use to load classes.
     */
    public URLSource(final URLClassLoader classLoader) {
        super(classLoader);
    }

    //endregion
}
