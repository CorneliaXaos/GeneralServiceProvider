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

package net.xaosdev.util.service.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * Isolates a ServiceLoader so that it may ONLY load services that are accessible from the provided ClassLoader
 * and NOT the parent ClassLoaders.
 * @param <S> the type of services to load.
 */
public final class IsolatedServiceLoader<S> implements Iterable<S>{

    //region Fields (Private)

    /**
     * The ClassLoader to isolate to.
     */
    private final ClassLoader classLoader;

    /**
     * The ServiceLoader performing the loading.
     */
    private final ServiceLoader<S> serviceLoader;

    //endregion

    // region Constructors (Private)

    /**
     * Creates a new IsolatedServiceLoader.
     *
     * @param classLoader the ClassLoader to isolate to.
     * @param serviceLoader the ServiceLoader performing the loading.
     */
    private IsolatedServiceLoader(final ClassLoader classLoader, ServiceLoader<S> serviceLoader) {
        this.classLoader = classLoader;
        this.serviceLoader = serviceLoader;
    }

    //endregion

    //region Interface (Public)

    /**
     * Creates a new IsolatedServiceLoader for specified service using specified ClassLoader.
     *
     * @param clazz the Class identifying the service to load.
     * @param classLoader the ClassLoader to load services with.
     * @param <T> the type of service to load.
     * @return an IsolatedService loader loading services of type S using the provided ClassLoader.
     */
    public static <T> IsolatedServiceLoader<T> load(Class<T> clazz, ClassLoader classLoader) {
        final ServiceLoader<T> loader = ServiceLoader.load(clazz, classLoader);
        return new IsolatedServiceLoader<>(classLoader, loader);
    }

    /**
     * Wraps the iterator of a ServiceLoader to isolate (ignore) services not governed by the input ClassLoader.
     * @return an iterator over services of type S that pass isolation checks.
     */
    public Iterator<S> iterator() {
        return new Iterator<S>() {

            /**
             * An active iterator reference for loading services.
             */
            private Iterator<S> iterator = serviceLoader.iterator();

            /**
             * The next, valid service found by this loader.
             */
            private S next = null;

            @Override
            public boolean hasNext() {
                if (next != null) {
                    return true;
                }

                do {
                    if (!iterator.hasNext()) {
                        return false;
                    }
                    next = iterator.next();
                } while (next.getClass().getClassLoader() != classLoader);

                return true;
            }

            @Override
            public S next() {
                if (hasNext()) {
                    S retVal = next;
                    next = null;
                    return retVal;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    //endregion
}
