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
