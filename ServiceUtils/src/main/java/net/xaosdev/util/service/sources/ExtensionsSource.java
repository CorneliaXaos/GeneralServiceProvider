/*
 * Copyright 2018 Cornelia Ada Schultz
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.xaosdev.util.service.sources;

import net.xaosdev.util.service.Source;

/**
 * A convenience class for quickly creating a source that points to the current JVM's extension ClassLoader.
 *
 * This source will ONLY load service providers that are located within the running JVM's extensions collection.
 * Additionally, this source will implement a Singleton pattern so that duplicate calls to this class result in the
 * same source every time.
 */
public class ExtensionsSource extends ClassLoaderSource {

    //region Fields (Private)

    /**
     * The instantiated source, or null if not created yet.
     */
    private static ExtensionsSource source;

    //endregion

    //region Constructors (Private)

    /**
     * Creates an extensions source.
     */
    private ExtensionsSource() {
        super(getExtensionsClassLoader());
    }

    //endregion

    //region Interface (Public)

    /**
     * Lazily instantiates the ExtensionsSource singleton instance and returns it.
     *
     * @return the ExtensionsSource instance.
     */
    public static Source getSource() {
        if (source == null) {
            source = new ExtensionsSource();
        }
        return source;
    }

    //endregion

    //region Interface (Private)

    /**
     * Locates the extension ClassLoader using a common approach.
     *
     * The extensions ClassLoader should be the last parent ClassLoader in a hierarchy extending from the system one.
     * @return the Extensions ClassLoader.
     */
    private static ClassLoader getExtensionsClassLoader() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        while (loader.getParent() != null) {
            loader = loader.getParent();
        }
        return loader;
    }

    //endregion
}
